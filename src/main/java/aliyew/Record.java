package aliyew;

import java.util.Scanner;

public class Record {
	private int recordId = 0;
	private String recordName = "undefined";
	private String creationDate = "undefined";
	private int totalIncome = 0;

	public Record(String name) {
		this.recordName = name;
	}

	public Record(int id, String name, String ts, int income) {
		this.recordId = id;
		this.recordName = name;
		this.creationDate = ts;
		this.totalIncome = income;
	}

	public String getRecordName() {

		return this.recordName;
	}

	public String getCreationDate() {

		return this.creationDate;
	}

	public int getRecordId() {
		return this.recordId;
	}

	public void getRecordInfo() {

		System.out.println("[ -------------------- ]");
		System.out.printf("[ %-20s ]\n", "Record : " + this.getRecordName());
		System.out.printf("[ %-20s ]\n", "Record ID : " + this.getRecordId());
		System.out.printf("[ %-20s ]\n", "Created : " + this.getCreationDate());
		System.out.println("[ -------------------- ]");
	}

}