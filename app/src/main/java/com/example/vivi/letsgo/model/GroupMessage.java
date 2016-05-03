package com.example.vivi.letsgo.model;

import java.io.Serializable;

public class GroupMessage implements Serializable {
	private String name;
	private String sex;
	private String circle_class;
	private String headimg;
	private String postid;
	private String groupid;
	private String userid;
	private String content;
	private String opttime;
	private String img00;
	private String img01;
	private String img02;
	private String img03;
	private String img04;
	private String img05;
	private String img06;
	private String img07;
	private String img08;
	private String status;
	private String datasource;
	private String goodcnt;
	private String repostcnt;
	private String discusscnt;
	private String datatype;

	/*public GroupMessage(String content,String headimg,String img00,String img01,String img02,String img03,String img04,String img05,String img06,String img07,String img08,String name,String opttime,String postid){
		this.content = content;
		this.headimg = headimg;
		this.img00 = img00;
		this.img01 = img01;
		this.img02 = img02;
		this.img03 = img03;
		this.img04 = img04;
		this.img05 = img05;
		this.img06 = img06;
		this.img07 = img07;
		this.img08 = img08;
		this.name = name;
		this.opttime = opttime;
		this.postid = postid;
	}*/

	public String getContent() {
		return content;
	}

	public String getHeadimg() {
		return headimg;
	}

	public String getImg00() {
		return img00;
	}

	public String getImg01() {
		return img01;
	}

	public String getImg02() {
		return img02;
	}

	public String getImg03() {
		return img03;
	}

	public String getImg04() {
		return img04;
	}

	public String getImg05() {
		return img05;
	}

	public String getImg06() {
		return img06;
	}

	public String getImg07() {
		return img07;
	}

	public String getImg08() {
		return img08;
	}

	public String getName() {
		return name;
	}

	public String getOpttime() {
		return opttime;
	}

	public String getPostid() {
		return postid;
	}

}
