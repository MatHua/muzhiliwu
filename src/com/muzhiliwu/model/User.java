package com.muzhiliwu.model;

import java.util.List;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Many;
import org.nutz.dao.entity.annotation.Table;

@Table("t_user")
public class User extends IdEntity {
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
	private int integral;// 积分数

	@Many(target = Message.class, field = "publisherId")
	private List<Message> myMessages;// 我的留言,便于找出用户自己发表的留言
	@Many(target = MessComment.class, field = "commenterId")
	private List<MessComment> myMessComments;// 便于找到我在留言墙的评论及对应的回复
	@Many(target = MessUnreadReply.class, field = "receiverId")
	private List<MessUnreadReply> myMessUnreadReplies;// 便于找出我在留言墙的未读回复

	@Many(target = Wish.class, field = "wisherId")
	private List<Wish> myWishes;// 我的许愿,便于找出用户自己许的愿
	@Many(target = WishCollect.class, field = "collecterId")
	private List<WishCollect> myWishCollectes;// 我收集的愿望,便于找出用户所收集到的愿望

	@Many(target = Share.class, field = "sharerId")
	private List<Share> myShares;// 我的分享,便于找出用户自己发表的分享
	// @Many(target = ShareCollect.class, field = "collecterId")
	// private List<ShareCollect> myShareCollectes;// 我收集的分享,便于找到用户所收集的分享
	@Many(target = ShareComment.class, field = "commenterId")
	private List<MessComment> myShareComments;// 便于找到我在分享墙的评论及对应的回复
	@Many(target = ShareUnreadReply.class, field = "receiverId")
	private List<ShareUnreadReply> myShareUnreadReplies;// 便于找出我在分享墙的未读回复

	public List<MessComment> getMyMessComments() {
		return myMessComments;
	}

	public void setMyMessComments(List<MessComment> myMessComments) {
		this.myMessComments = myMessComments;
	}

	public List<MessComment> getMyShareComments() {
		return myShareComments;
	}

	public void setMyShareComments(List<MessComment> myShareComments) {
		this.myShareComments = myShareComments;
	}

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

	// public List<ShareCollect> getMyShareCollectes() {
	// return myShareCollectes;
	// }
	//
	// public void setMyShareCollectes(List<ShareCollect> myShareCollectes) {
	// this.myShareCollectes = myShareCollectes;
	// }

	public List<MessUnreadReply> getMyMessUnreadReplies() {
		return myMessUnreadReplies;
	}

	public void setMyMessUnreadReplies(List<MessUnreadReply> myMessUnreadReplies) {
		this.myMessUnreadReplies = myMessUnreadReplies;
	}

	public List<ShareUnreadReply> getMyShareUnreadReplies() {
		return myShareUnreadReplies;
	}

	public void setMyShareUnreadReplies(
			List<ShareUnreadReply> myShareUnreadReplies) {
		this.myShareUnreadReplies = myShareUnreadReplies;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

}
