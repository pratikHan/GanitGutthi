package com.example.kluggame2;

public class PlayerInfo {

	String name,age,gender;
	//ScoreCardModel sc;
	
	public PlayerInfo(){
		name=new String();
		age=new String();
		gender=new String();
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	/*public ScoreCardModel getSc() {
		return sc;
	}

	public void setSc(ScoreCardModel sc) {
		this.sc = sc;
	}*/

//	public PlayerInfo(String name, String age, String gender, ScoreCardModel sc) {
//		super();
//		this.name = name;
//		this.age = age;
//		this.gender = gender;
//		//this.sc = sc;
//	}

	public PlayerInfo(String name, String age, String gender) {
		super();
		this.name = name;
		this.age = age;
		this.gender = gender;
	}
	
	
}
