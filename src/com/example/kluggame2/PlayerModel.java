package com.example.kluggame2;

public class PlayerModel {

	String name;
	int value;
	
	public PlayerModel(){}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public PlayerModel(String name, int value) {
		super();
		this.name = name;
		this.value = value;
	}
	
	
}
