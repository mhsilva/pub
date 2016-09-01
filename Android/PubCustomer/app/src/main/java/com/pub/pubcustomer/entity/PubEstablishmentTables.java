package com.pub.pubcustomer.entity;

import java.io.Serializable;

public class PubEstablishmentTables implements Serializable {

	private String tableNumber;
	private boolean available;

	public String getTableNumber() {
		return tableNumber;
	}

	public void setTableNumber(String tableNumber) {
		this.tableNumber = tableNumber;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}
}
