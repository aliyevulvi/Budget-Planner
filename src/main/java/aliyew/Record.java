package aliyew;

public class Record {

    private int recordId = 0;
    private String recordName = "undefined";
    private String creationDate = "undefined";
    private double recordIncome = 0;
    private double recordSaving = 0;
    private boolean recordSync = false;

    public Record() {
        
    }

    public Record(String name, double income, double saving) {
        this.recordName = name;
        this.recordIncome = income;
        this.recordSaving = saving;
    }

    public Record(int id, String name, String ts) {
        this.recordId = id;
        this.recordName = name;
        this.creationDate = ts;
    }

    public Record(int id, String name, String ts, double income, double saving) {
        this.recordId = id;
        this.recordName = name;
        this.creationDate = ts;
        this.recordIncome = income;
        this.recordSaving = saving;
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

    public double getRecordIncome() {
        return this.recordIncome;
    }

    public double getRecordSaving() {
        return this.recordSaving;
    }

    public boolean getRecordSync() {
        return this.recordSync;
    }

    public void setSync(boolean bool) {
        this.recordSync = bool;
    }

    public void setIncome(double amt) {
        this.recordIncome += amt;
    }

    public void setName(String name) {
        this.recordName = name;
    }

    public void setSaving(double amt) {
        this.recordSaving = amt;
    }

    public void setId(int id) {
        this.recordId = id;
    }

    public void setDate() {
        this.creationDate = java.sql.Timestamp.from(java.time.Instant.now())+"";
    }

    

    public void getRecordInfo() {

        System.out.println("[ ----------------------------------- ]");
        System.out.printf("[ %-15s%-20s ]\n", "Record Name: ",this.getRecordName());
        System.out.printf("[ %-15s%-20s ]\n", "Record ID : ", this.getRecordId()+"");
        System.out.printf("[ %-15s%-20s ]\n", "Income : ", this.getRecordIncome()+"");
        System.out.printf("[ %-15s%-20s ]\n", "Saving : ", this.getRecordSaving()+"");
        System.out.printf("[ %-15s%-20s ]\n", "Created : ", this.getCreationDate().substring(0, 16)+"");
        System.out.printf("[ %-15s%-20s ]\n", "Sync : ", (this.recordSync ? "True" : "False") );
        System.out.println("[ ----------------------------------- ]");

    }

}
