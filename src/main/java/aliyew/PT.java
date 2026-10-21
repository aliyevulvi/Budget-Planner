package aliyew;

public class PT {
	private String PTCat;
	private double PTAmt;
	
	public PT() {
	    this.PTCat = "undefined";
		this.PTAmt = 0;
	}

	public PT(String cat, double amt) {
		this.PTCat = cat;
		this.PTAmt = amt;
	}

	public String getPTCat() {
		return PTCat;
	}

	public double getPTAmt() {
		return PTAmt;
	}

	public void setPTCat(String cat) {
	    this.PTCat = cat;
	}
	
	public void setPTAmt(double amt) {
	    this.PTAmt = amt;
	}
	
	public String toString() {
	    return this.PTCat+" | "+this.PTAmt;
	}
}