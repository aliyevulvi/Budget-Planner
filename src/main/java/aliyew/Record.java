package aliyew;

import java.util.UUID;
import java.util.ArrayList;
import java.time.Instant;

public class Record {

    private String recordId = "undefined";
    private String recordName = "undefined";
    private Instant recordCreationDate = null;
    private double recordIncome = 0;
    private double recordSaving = 0;
    private boolean recordSync = false;

    public Record() {
        
    }

    public Record(String name, double income, double saving) {
        this.recordName = name;
        this.recordIncome = income;
        this.recordSaving = saving;
        this.recordCreationDate = Instant.now();
        
        String uuidStr = UUID.randomUUID().toString();
        
        ArrayList<Record> allRecords = JsonManager.getRecords();
        
        for (int i = 0; i < allRecords.size(); i++) {
            if (allRecords.get(i).getRecordId().equals(uuidStr)) {
                uuidStr = UUID.randomUUID().toString();
                i = -1;
            }
        }
        
        this.recordId = uuidStr;
    }

    public Record(String id, String name, Instant date) {
        this.recordId = id;
        this.recordName = name;
        this.recordCreationDate = date;
    }

    public Record(String id, String name, Instant date, double income, double saving) {
        this.recordId = id;
        this.recordName = name;
        this.recordCreationDate = date;
        this.recordIncome = income;
        this.recordSaving = saving;
    }

    public String getRecordName() {
        return this.recordName;
    }

    public Instant getRecordCreationDate() {
        return this.recordCreationDate;
    }

    public String getRecordId() {
        return this.recordId;
    }

    public double getRecordIncome() {
        return this.recordIncome;
    }

    public double getRecordSaving() {
        return this.recordSaving;
    }

    public boolean getRecordSync() {
        return this.recordSync;
    }

    public void setRecordId(String id) {
        this.recordId = id;
    }

    public void setRecordName(String name) {
        this.recordName = name;
    }

    public void setRecordIncome(double amt) {
        this.recordIncome = amt;
    }

    public void setRecordSaving(double amt) {
        this.recordSaving = amt;
    }

    public void setRecordCreationDate(Instant date) {
        this.recordCreationDate = date;
    }

    public void setRecordSync(boolean bool) {
        this.recordSync = bool;
    }

}
