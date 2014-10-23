package com.muzhiliwu.service;

import java.util.ArrayList;
import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;

import com.muzhiliwu.model.Share;
import com.muzhiliwu.model.ShareComment;
import com.muzhiliwu.model.SharePraise;
import com.muzhiliwu.model.ShareUnreadReply;
import com.muzhiliwu.model.User;
import com.muzhiliwu.utils.DateUtils;
import com.muzhiliwu.utils.NumGenerator;

@IocBean
public class ShareService {
	@Inject
	private Dao dao;

	/**
	 * 发布或修改一条分享
	 * 
	 * @param sharer
	 *            分享者
	 * @param msg
	 *            一条留言
	 * @return
	 */
	public boolean publishOrUpdateShare(User sharer, Share share) {
		if (Strings.isBlank(share.getId())) {// 发布分享
			share.setId(NumGenerator.getUuid());
			share.setDate(DateUtils.now());
			share.setSharerId(sharer.getId());
			share.setType(Share.From_Own);// 原创
			dao.insert(share);
		} else {// 修改分享
			share.setDate(DateUtils.now());
			share.setSharerId(sharer.getId());
			dao.update(share);
		}
		return true;
	}

	/**
	 * 收藏一个分享
	 * 
	 * @param collecter
	 *            收藏者
	 * @param share
	 *            一条分享
	 * @param fromer
	 *            分享来源者
	 * @return
	 */
	public boolean collectShare(User collecter, Share share, User fromer) {
		if (okCollect(collecter, share)) {
			share = dao.fetch(Share.class, share.getId());
			Share collect = new Share();
			collect.setId(NumGenerator.getUuid());// 主键
			collect.setDate(DateUtils.now());// 收藏的时间

			collect.setFromerId(fromer.getId());// 收藏的来源者
			collect.setSharerId(collecter.getId());// 收藏者
			collect.setTitle(share.getTitle());// 收藏的标题
			collect.setContent(share.getContent());// 收藏的内容
			collect.setCollectId(share.getId());// 收藏的分享对应的id

			collect.setType(Share.From_Other);// 分享的类型:来自收藏
			dao.insert(collect);
			return true;
		}
		return false;
	}

	/**
	 * 检查是否已经收藏
	 * 
	 * @param share
	 *            一个分享
	 * @param fromer
	 *            分享来源
	 * @return
	 */
	public boolean okCollect(User collecter, Share share) {
		Share tmp = dao.fetch(
				Share.class,
				Cnd.where("sharerId", "=", collecter.getId()).and("collectId",
						"=", share.getId()));
		return tmp == null ? true : false;
	}

	/**
	 * 点赞某条分享
	 * 
	 * @param share
	 *            被点赞的分享
	 * @param praiser
	 *            点赞者
	 * @return
	 */
	public boolean praiseShare(Share share, User praiser) {
		if (okPraise(share.getId(), praiser.getId())) {// 点赞
			SharePraise praise = new SharePraise();
			praise.setId(NumGenerator.getUuid());
			praise.setDate(DateUtils.now());
			praise.setShareId(share.getId());// 这是联结的id
			praise.setPraiserId(praiser.getId());// 这是联结id
			changePraiseNumber(share, 1);
			dao.insert(praise);
			return true;
		} else {// 取消点赞
			SharePraise praise = dao.fetch(
					SharePraise.class,
					Cnd.where("shareId", "=", share.getId()).and("praiserId",
							"=", praiser.getId()));
			dao.delete(SharePraise.class, praise.getId());
			changePraiseNumber(share, -1);
			return false;
		}
	}

	// 点赞数增减
	private void changePraiseNumber(Share share, int i) {
		share = dao.fetch(Share.class, share.getId());
		share.setPraiseNum(share.getPraiseNum() + i);
		dao.update(share);
	}

	/**
	 * 检查是否已点赞
	 * 
	 * @param shareId
	 *            留言的id
	 * @param pariserId
	 *            点赞者的id
	 * @return
	 */
	public boolean okPraise(String shareId, String praiserId) {
		SharePraise praise = dao.fetch(
				SharePraise.class,
				Cnd.where("shareId", "=", shareId).and("praiserId", "=",
						praiserId));
		return praise == null ? true : false;
	}

	/**
	 * 分享墙评论
	 * 
	 * @param share
	 *            要评论的分享
	 * @param commenter
	 *            评论者
	 * @param comment
	 *            一条评论信息
	 * @param fatherCommenter
	 *            该评论的父评论(因为该评论可能是一条回复别人评论的评论)
	 * @return
	 */
	public boolean commentShare(Share share, User commenter,
			ShareComment comment, User fatherCommenter) {
		comment.setId(NumGenerator.getUuid());
		comment.setCommenterId(commenter.getId());// 联结id
		comment.setDate(DateUtils.now());
		comment.setShareId(share.getId());// 联结id
		// 如果父评论存在,则为父评论插入子评论
		if (!Strings.isBlank(fatherCommenter.getId())) {
			comment.setFatherCommenterId(fatherCommenter.getId());
			createUnreadReply(commenter, fatherCommenter, comment);
		}
		dao.insert(comment);// 插入一条评论
		return true;
	}

	/**
	 * 创建一条未读信息
	 * 
	 * @param commenter
	 *            评论发表者
	 * @param fatherCommenter
	 *            被评论者
	 * @param comment
	 *            发表的评论
	 */
	private void createUnreadReply(User commenter, User fatherCommenter,
			ShareComment comment) {
		ShareUnreadReply unread = new ShareUnreadReply();
		unread.setContent(comment.getContent());
		unread.setDate(DateUtils.now());
		unread.setId(NumGenerator.getUuid());
		unread.setReceiverId(fatherCommenter.getId());
		unread.setReplierId(commenter.getId());
		unread.setState(ShareUnreadReply.NUREAD);
		dao.insert(unread);
	}

	/**
	 * 获取某一页分享
	 * 
	 * @param page
	 * @return
	 */
	public QueryResult getShares(Pager page, User user) {
		List<Share> shares = dao.query(Share.class, Cnd.orderBy().desc("date"),
				page);
		page.setRecordCount(dao.count(Share.class));
		Share share;
		List<Share> results = new ArrayList<Share>();
		for (int i = 0; i < shares.size(); i++) {
			// 如果该分享是收藏别人的,加载收藏的来源
			if (Share.From_Other.equals(shares.get(i).getType())) {
				dao.fetchLinks(shares.get(i), "fromerId");
			}
			// 加载点赞者
			dao.fetchLinks(shares.get(i), "praises", Cnd.orderBy().desc("date"));
			// 加载评论者
			dao.fetchLinks(shares.get(i), "comments", Cnd.orderBy()
					.desc("date"));
			// 加载每条评论的父评论
			for (int j = 0; j < shares.get(i).getComments().size(); j++) {
				dao.fetchLinks(shares.get(i).getComments().get(j),
						"fatherCommenter");
			}
		}
		return new QueryResult(shares, page);
	}

	/**
	 * 获得用户某一页的留言
	 * 
	 * @param user
	 *            留言发表者
	 * @param page
	 *            分页参数
	 * @return
	 */
	public QueryResult getMyShares(User user, Pager page) {
		List<Share> shares = dao
				.query(Share.class, Cnd.where("sharerId", "=", user.getId())
						.orderBy().desc("date"), page);
		page.setRecordCount(dao.count(Share.class,
				Cnd.where("sharerId", "=", user.getId())));
		Share share;
		List<Share> results = new ArrayList<Share>();
		for (int i = 0; i < shares.size(); i++) {
			// 如果该分享是收藏别人的,加载收藏的来源
			if (Share.From_Other.equals(shares.get(i).getType())) {
				dao.fetchLinks(shares.get(i), "fromerId");
			}
			// 加载点赞者
			dao.fetchLinks(shares.get(i), "praises", Cnd.orderBy().desc("date"));
			// 加载评论者
			dao.fetchLinks(shares.get(i), "comments", Cnd.orderBy()
					.desc("date"));
			// 加载每条评论的父评论
			for (int j = 0; j < shares.get(i).getComments().size(); j++) {
				dao.fetchLinks(shares.get(i).getComments().get(j),
						"fatherCommenter");
			}

		}
		return new QueryResult(shares, page);
	}

	/**
	 * 取消收藏
	 * 
	 * @param collecter
	 * @param share
	 * @return
	 */
	public boolean cancelCollectShare(User collecter, Share share) {
		// okCalcelCollect(collecter, share);
		return false;
	}
}
