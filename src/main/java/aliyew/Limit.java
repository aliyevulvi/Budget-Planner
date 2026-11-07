package aliyew;

public class Limit {
	private String limitCat = "undefined";
	private double limitAmt = 0;
	private String limitRecordName = "undefined";
	
	public Limit() {
	    
	}
	
	public Limit(String cat, double amt, String recordName) {
	    this.limitCat = cat;
	    this.limitAmt = amt;
	    this.limitRecordName = recordName;
	}
	
	public String getLimitCat() {
	    return this.limitCat;
	}
	
	public double getLimitAmt() {
	    return this.limitAmt;
	}
	
	public String getLimitRecordName() {
	    return this.limitRecordName;
	}
	
	public void setLimitCat(String cat) {
	    this.limitCat = cat;
	}
	
	public void setLimitAmt(double amt) {
	    this.limitAmt = amt;
	}
	
	public void setLimitRecordId(String recordName) {
	    this.limitRecordName = recordName;
	}
	
	public String toString() {
	    return this.limitCat + " | " + this.limitAmt + " | " + this.limitRecordName;
	}
	
	public static Limit fromString(String limStr) {
	    String[] limParts = limStr.split("\\|");
	    return new Limit(limParts[0].trim(), Double.parseDouble(limParts[1].trim()), limParts[2].trim());
	}
}