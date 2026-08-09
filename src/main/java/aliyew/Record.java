package aliyew;

public class Record {

    private int recordId = 0;
    private String recordName = "undefined";
    private String creationDate = "undefined";
    private int totalIncome = 0;

    public Record(String name, int income) {
        this.recordName = name;
        this.totalIncome = income;
    }

    public Record(int id, String name, String ts) {
        this.recordId = id;
        this.recordName = name;
        this.creationDate = ts;
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

    public int getRecordIncome() {
        return this.totalIncome;
    }

    public void getRecordInfo() {

        System.out.println("[ ----------------------------------- ]");
        System.out.printf("[ %-35s ]\n", "Record Name: " + this.getRecordName());
        System.out.printf("[ %-35s ]\n", "Record ID : " + this.getRecordId());
        System.out.printf("[ %-35s ]\n", "Income : " + this.getRecordIncome());
        System.out.printf("[ %-35s ]\n", "Created : " + this.getCreationDate().substring(0, 16));
        System.out.println("[ ----------------------------------- ]");

    }

}
