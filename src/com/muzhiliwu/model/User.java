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
	public static final String Man = "男";// 男性
	public static final String Woman = "女";// 女性
	public static final String Secret = "保密";// 保密
	public static final int[] Popularity = { 0, 100, 200, 300, 400, 500, 600,
			700, 800, 900, 1000, 1100, 1200, 1300, 1400, 1500, 1600, 1700,
			1800, 1900, 2000, 2100, 2200, 2300, 2400, 2500, 2600, 2700, 2800,
			2900, 3000, 3100, 3200, 3300, 3400, 3500, 3600, 3700, 3800, 3900,
			4000 };
	public static final int[] SendGift = { 0, 2, 4, 6, 8, 10, 12, 14, 16, 18,
			20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 42, 44, 46, 48, 50, 52,
			54, 56, 58, 60, 62, 64, 66, 68, 70, 72, 74, 76, 78, 80 };

	@Column
	private String code;// 账号
	@Column
	private String pass;// 密码

	@Column
	private String photo;// 头像

	@Column
	private String nickName;// 昵称
	@Column
	@ColDefine(type = ColType.TEXT)
	private String sign;// 签名
	@Column
	private String emotion;// 情感状况
	@Column
	@ColDefine(type = ColType.TEXT)
	private String mood;// 心情

	@Many(target = UserTag.class, field = "userId")
	private List<UserTag> tags;// 用户标签

	@Column
	private String name;// 姓名
	@Column
	private String sex;// 性别
	@Column
	private String orientation;// 取向
	@Column
	private String birth;// 生日
	@Column
	private String star;// 星座

	@Column
	private String phone;// 手机
	@Column
	private String QQ;// QQ
	@Column
	private String myUrl;// 个人网址
	@Column
	private String email;// 邮箱
	@Column
	private String provinceName;// 省
	@Column
	private String cityName;// 城市
	@Column
	private String areaName;// 区
	@Column
	private String townName;// 镇
	@Column
	private String school;// 学校
	@Column
	private String college;// 学院
	@Column
	private String major;// 专业

	@Column
	private int age;// 年龄
	@Column
	private String grade;// 班级

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
	private int messNum;// 留言数
	@Many(target = Message.class, field = "publisherId")
	private List<Message> myMessages;// 我的留言,便于找出用户自己发表的留言
	// ________________________________________________________________________

	// ********************许愿**************************************************
	private int wishNum;// 许愿数
	@Many(target = Wish.class, field = "wisherId")
	private List<Wish> myWishes;// 我的许愿,便于找出用户自己许的愿

	private Wish newlyWish;// 最近的许愿

	private int wantorNum;// 愿望实现的申请者数
	@ManyMany(target = User.class, relation = "t_wish_realization_of_wantor", from = "wisherId", to = "wantorId")
	private List<User> myWishWantor;// 想要帮忙实现愿望的人

	private int wishShareNum;// 收藏数
	@Many(target = WishShare.class, field = "sharerId")
	private List<WishShare> myWishShares;// 我收集的愿望,便于找出用户所收集到的愿望
	// ________________________________________________________________________

	// ************************分享墙********************************************
	private int shareNum;// 分享数
	@Many(target = Share.class, field = "sharerId")
	private List<Share> myShares;// 我的分享,便于找出用户自己发表的分享

	// ________________________________________________________________________

	// ************************购物车*********************************************
	private int orderNum;// 订单数
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

	public int getWishShareNum() {
		return wishShareNum;
	}

	public void setWishShareNum(int wishShareNum) {
		this.wishShareNum = wishShareNum;
	}

	public List<WishShare> getMyWishShares() {
		return myWishShares;
	}

	public void setMyWishShares(List<WishShare> myWishShares) {
		this.myWishShares = myWishShares;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	public String getTownName() {
		return townName;
	}

	public void setTownName(String townName) {
		this.townName = townName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getMood() {
		return mood;
	}

	public void setMood(String mood) {
		this.mood = mood;
	}

	public List<UserTag> getTags() {
		return tags;
	}

	public void setTags(List<UserTag> tags) {
		this.tags = tags;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public String getQQ() {
		return QQ;
	}

	public void setQQ(String qQ) {
		QQ = qQ;
	}

	public String getMyUrl() {
		return myUrl;
	}

	public void setMyUrl(String myUrl) {
		this.myUrl = myUrl;
	}
}
