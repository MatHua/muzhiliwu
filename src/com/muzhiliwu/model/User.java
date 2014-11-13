package com.muzhiliwu.model;

import java.util.List;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Many;
import org.nutz.dao.entity.annotation.ManyMany;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

import com.muzhiliwu.model.gift.OrderForm;
import com.muzhiliwu.model.gift.ShoppingCart;

@Table("t_user")
@TableIndexes({ @Index(name = "idx_user", fields = { "code" }, unique = false) })
public class User extends IdEntity {
	public static final String Man = "man";// 男性
	public static final String Woman = "woman";// 女性
	public static final String Secret = "secret";// 保密

	@Column
	private String code;// 账号
	@Column
	private String pass;// 密码
	@Column
	@ColDefine(type = ColType.TEXT)
	private String mood;// 心情
	@Column
	private String photo;// 头像
	@Column
	private String name;// 姓名
	@Column
	private int age;// 年龄
	@Column
	private String birth;// 生日
	@Column
	private String sex;// 性别
	@Column
	private String star;// 星座
	@Column
	private String school;// 学校
	@Column
	private String college;// 学院
	@Column
	private String major;// 专业
	@Column
	private String grade;// 班级
	@Column
	private String phone;// 手机
	@Column
	private String email;// 邮箱
	@Column
	private String emotion;// 情感状况
	@Column
	private int muzhiCoin;// 拇指币

	@Column
	private int popularityValue;// 当前人气值
	@Column
	private int popularityRank;// 人气等级
	@Column
	private int popularityTop;// 当前级数人气最高分

	@Column
	private int sendGiftValue;// 当前级数送出礼物数
	@Column
	private int sendGiftRank;// 送出礼物等级
	@Column
	private int sendGiftTop;// 当前级数送出礼物最多分

	// *********************留言************************************************
	@Column
	private int messNum;// 留言数
	@Many(target = Message.class, field = "publisherId")
	private List<Message> myMessages;// 我的留言,便于找出用户自己发表的留言
	// ________________________________________________________________________

	// ********************许愿**************************************************
	@Column
	private int wishNum;// 许愿数
	@Many(target = Wish.class, field = "wisherId")
	private List<Wish> myWishes;// 我的许愿,便于找出用户自己许的愿

	private Wish newlyWish;// 最近的许愿

	@Column
	private int wantorNum;// 愿望实现的申请者数
	@ManyMany(target = User.class, relation = "t_wish_realization_of_wantor", from = "wisherId", to = "wantorId")
	private List<User> myWishWantor;// 想要帮忙实现愿望的人

	@Column
	private int wishCollectNum;// 收藏数
	@Many(target = WishCollect.class, field = "collecterId")
	private List<WishCollect> myWishCollectes;// 我收集的愿望,便于找出用户所收集到的愿望
	// ________________________________________________________________________

	// ************************分享墙*********************************************
	@Column
	private int shareNum;// 分享数
	@Many(target = Share.class, field = "sharerId")
	private List<Share> myShares;// 我的分享,便于找出用户自己发表的分享

	// ________________________________________________________________________

	// ************************购物车*********************************************
	@Many(target = OrderForm.class, field = "buyerId")
	private List<OrderForm> orders;

	// __________________________________________________________________________

	private int unreadReplyNum;// 记录总的未读信息数
	@Many(target = UnreadReply.class, field = "receiverId")
	private List<UnreadReply> myUnreadReplies;// 便于找出我在分享墙的未读回复

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMood() {
		return mood;
	}

	public void setMood(String mood) {
		this.mood = mood;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getStar() {
		return star;
	}

	public void setStar(String star) {
		this.star = star;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getCollege() {
		return college;
	}

	public void setCollege(String college) {
		this.college = college;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmotion() {
		return emotion;
	}

	public void setEmotion(String emotion) {
		this.emotion = emotion;
	}

	public List<Message> getMyMessages() {
		return myMessages;
	}

	public void setMyMessages(List<Message> myMessages) {
		this.myMessages = myMessages;
	}

	public List<Wish> getMyWishes() {
		return myWishes;
	}

	public void setMyWishes(List<Wish> myWishes) {
		this.myWishes = myWishes;
	}

	public List<Share> getMyShares() {
		return myShares;
	}

	public void setMyShares(List<Share> myShares) {
		this.myShares = myShares;
	}

	public List<WishCollect> getMyWishCollectes() {
		return myWishCollectes;
	}

	public void setMyWishCollectes(List<WishCollect> myWishCollectes) {
		this.myWishCollectes = myWishCollectes;
	}

	public int getMuzhiCoin() {
		return muzhiCoin;
	}

	public void setMuzhiCoin(int muzhiCoin) {
		this.muzhiCoin = muzhiCoin;
	}

	public int getPopularityValue() {
		return popularityValue;
	}

	public void setPopularityValue(int popularityValue) {
		this.popularityValue = popularityValue;
	}

	public int getPopularityRank() {
		return popularityRank;
	}

	public void setPopularityRank(int popularityRank) {
		this.popularityRank = popularityRank;
	}

	public int getSendGiftValue() {
		return sendGiftValue;
	}

	public void setSendGiftValue(int sendGiftValue) {
		this.sendGiftValue = sendGiftValue;
	}

	public int getSendGiftRank() {
		return sendGiftRank;
	}

	public void setSendGiftRank(int sendGiftRank) {
		this.sendGiftRank = sendGiftRank;
	}

	public int getMessNum() {
		return messNum;
	}

	public void setMessNum(int messNum) {
		this.messNum = messNum;
	}

	public int getWishNum() {
		return wishNum;
	}

	public void setWishNum(int wishNum) {
		this.wishNum = wishNum;
	}

	public int getWantorNum() {
		return wantorNum;
	}

	public void setWantorNum(int wantorNum) {
		this.wantorNum = wantorNum;
	}

	public List<User> getMyWishWantor() {
		return myWishWantor;
	}

	public void setMyWishWantor(List<User> myWishWantor) {
		this.myWishWantor = myWishWantor;
	}

	public int getWishCollectNum() {
		return wishCollectNum;
	}

	public void setWishCollectNum(int wishCollectNum) {
		this.wishCollectNum = wishCollectNum;
	}

	public int getShareNum() {
		return shareNum;
	}

	public void setShareNum(int shareNum) {
		this.shareNum = shareNum;
	}

	public int getPopularityTop() {
		return popularityTop;
	}

	public void setPopularityTop(int popularityTop) {
		this.popularityTop = popularityTop;
	}

	public int getSendGiftTop() {
		return sendGiftTop;
	}

	public void setSendGiftTop(int sendGiftTop) {
		this.sendGiftTop = sendGiftTop;
	}

	public int getUnreadReplyNum() {
		return unreadReplyNum;
	}

	public void setUnreadReplyNum(int unreadReplyNum) {
		this.unreadReplyNum = unreadReplyNum;
	}

	public List<UnreadReply> getMyUnreadReplies() {
		return myUnreadReplies;
	}

	public void setMyUnreadReplies(List<UnreadReply> myUnreadReplies) {
		this.myUnreadReplies = myUnreadReplies;
	}

	public Wish getNewlyWish() {
		return newlyWish;
	}

	public void setNewlyWish(Wish newlyWish) {
		this.newlyWish = newlyWish;
	}

	public List<OrderForm> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderForm> orders) {
		this.orders = orders;
	}

}
