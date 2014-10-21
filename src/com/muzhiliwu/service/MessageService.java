package com.muzhiliwu.service;

import java.util.ArrayList;
import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;

import com.muzhiliwu.list.PageResult;
import com.muzhiliwu.model.MessComment;
import com.muzhiliwu.model.MessPraise;
import com.muzhiliwu.model.Message;
import com.muzhiliwu.model.User;
import com.muzhiliwu.utils.DateUtils;
import com.muzhiliwu.utils.NumGenerator;

@IocBean
public class MessageService {
	@Inject
	private Dao dao;

	/**
	 * 发表一条留言
	 * 
	 * @param publisher
	 *            发表者
	 * @param content
	 *            发表内容
	 * @param type
	 *            发表类型
	 * @return
	 */
	public boolean publishMessage(User publisher, String content, String type) {
		Message msg = new Message();
		msg.setId(NumGenerator.getUuid());
		msg.setContent(content);
		msg.setDate(DateUtils.now());
		// msg.setPublisherId(publisher.getId());//这是联结的id,插入时会[自动补全]
		msg.setType(type);

		List<Message> msgs = new ArrayList<Message>();
		msgs.add(msg);
		publisher.setMyMessages(msgs);

		// dao.insert(msg);
		// 仅仅插入映射字段
		dao.insertLinks(publisher, "myMessages");
		return true;
	}

	public PageResult<Message> getMyMessage(User user, Pager page) {
		PageResult<Message> pr = new PageResult<Message>();
		// User u = dao.fetch(User.class, user.getId());
		// dao.fetchLinks(u, "myMessages");

		//
		List<Message> msgs = dao.query(
				Message.class,
				Cnd.where("publisherId", "=", user.getId()).orderBy()
						.desc("date"), page);
		for (int i = 0; i < msgs.size(); i++) {
			// 加载发表者
			dao.fetchLinks(msgs.get(i), "publisher");
			// 加载点赞者
			dao.fetchLinks(msgs.get(i), "praises", Cnd.orderBy().desc("date"));
			// 加载评论者
			dao.fetchLinks(msgs.get(i), "comments", Cnd.orderBy().desc("date"));
			// 加载每条评论的父评论
			for (int j = 0; j < msgs.get(i).getComments().size(); j++) {
				dao.fetchLinks(msgs.get(i).getComments().get(j), "");
			}
		}
		// pr.setResults();
		pr.setPage(page);
		return pr;
	}

	/**
	 * 留言点赞
	 * 
	 * @param msg
	 * @param praiser
	 * @return
	 */
	public boolean praiseMessage(Message msg, User praiser) {
		MessPraise praise = new MessPraise();
		praise.setId(NumGenerator.getUuid());
		praise.setDate(DateUtils.now());
		// praise.setMessId(msg.getId());//这是联结的id,插入(insertLinks)一条点赞时会[自动补全]
		// praise.setPraiserId(praiserId);//这是联结id,插入(insertLinks)点赞者时会[自动补全]
		praise.setPraiser(praiser);

		List<MessPraise> praises = new ArrayList<MessPraise>();
		praises.add(praise);
		msg.setPraises(praises);

		// 插入一条点赞~
		dao.insertLinks(msg, "praises");
		// 插入点赞评论者~
		dao.insertLinks(praise, "praiser");
		return true;
	}

	/**
	 * 留言墙评论
	 * 
	 * @param msg
	 *            要评论的留言
	 * @param commenter
	 *            评论者
	 * @param content
	 *            评论内容
	 * @param fatherId
	 *            父评论id,若为空,说明此评论为祖宗评论,否则为某条评论的子评论
	 * @return
	 */
	public boolean commentMessage(Message msg, User commenter, String content,
			String fatherCommenterId) {
		MessComment comment = new MessComment();
		comment.setId(NumGenerator.getUuid());
		comment.setCommenter(commenter);
		// comment.setCommenterId(commenterId);//联结id,插入(insertLinks)评论者时会[自动补全]
		comment.setContent(content);
		comment.setDate(DateUtils.now());
		// comment.setFatherCommentId(fatherCommentId);//联结id,更新(updateLinks)其子评论时会[自动补全]
		// comment.setMessCommentId(messCommentId);//联结id,更新(updateLinks)其父评论时时会[自动补全]
		// comment.setMessId(messId);//联结id,插入(insertLinks)时会[自动补全]
		// comment.setSons(sons);//请忽略它
		// comment.setFather(father);//请忽略它

		List<MessComment> comments = new ArrayList<MessComment>();
		comments.add(comment);
		msg.setComments(comments);

		dao.insertLinks(msg, "comments");// 插入评论信息
		dao.insertLinks(comment, "commenter");// 插入评论者信息

		// 如果父评论存在,则为父评论插入子评论
		if (!Strings.isBlank(fatherCommenterId)) {
			// 获取父评论
			User father = dao.fetch(User.class, fatherCommenterId);
			comment.setFatherCommenter(father);// 为儿子设置父亲
			// 因为comment已经存在,所以执行的是更新操作而不是插入操作
			dao.updateLinks(comment, "father");
		}
		return true;
	}
}
