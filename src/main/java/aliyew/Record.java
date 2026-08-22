package aliyew;

public class Record {

    private int recordId = 0;
    private String recordName = "undefined";
    private String creationDate = "undefined";
    private double totalIncome = 0;
    private double record_saving = 0;
    private boolean isSync = false;

    public Record() {
        
    }

    public Record(String name, double income, double saving) {
        this.recordName = name;
        this.totalIncome = income;
        this.record_saving = saving;
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
        this.totalIncome = income;
        this.record_saving = saving;
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
        return this.totalIncome;
    }

    public double getRecordSaving() {
        return this.record_saving;
    }

    public void setSynced() {
        this.isSync = true;
    }

    public void getRecordInfo() {

        System.out.println("[ ----------------------------------- ]");
        System.out.printf("[ %-15s%-20s ]\n", "Record Name: ",this.getRecordName());
        System.out.printf("[ %-15s%-20s ]\n", "Record ID : ", this.getRecordId()+"");
        System.out.printf("[ %-15s%-20s ]\n", "Income : ", this.getRecordIncome()+"");
        System.out.printf("[ %-15s%-20s ]\n", "Saving : ", this.getRecordSaving()+"");
        System.out.printf("[ %-15s%-20s ]\n", "Created : ", this.getCreationDate().substring(0, 16)+"");
        System.out.println("[ ----------------------------------- ]");

    }

}
