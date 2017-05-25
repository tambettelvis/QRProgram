package com.tanilsoo.tambet_telvis;

public class Order {
	
	
	private int id;
	private String name;
	private int length;
	private int diameter;
	private String woodType;
	private int amtOfPacks;
	private int price;
	private String additionalInfo;
	private int packsDone;
	
	public Order(int id, String name, int length, int diameter, String woodType, int amtOfPacks, int price,
			String additionalInfo, int packsDone) {
		this.id = id;
		this.name = name;
		this.length = length;
		this.diameter = diameter;
		this.woodType = woodType;
		this.amtOfPacks = amtOfPacks;
		this.price = price;
		this.additionalInfo = additionalInfo;
		this.packsDone = packsDone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getDiameter() {
		return diameter;
	}

	public void setDiameter(int diameter) {
		this.diameter = diameter;
	}

	public String getWoodType() {
		return woodType;
	}

	public void setWoodType(String woodType) {
		this.woodType = woodType;
	}

	public int getAmtOfPacks() {
		return amtOfPacks;
	}

	public void setAmtOfPacks(int amtOfPacks) {
		this.amtOfPacks = amtOfPacks;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public int getPacksDone() {
		return packsDone;
	}

	public void setPacksDone(int packsDone) {
		this.packsDone = packsDone;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}
