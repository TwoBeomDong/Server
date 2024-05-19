package insuranceProduct;

/**
 * @author dongyeonkim
 * @version 1.0
 * @created 14-5-2024 ���� 6:43:14
 */
public class BasicInsuranceInfo {
	private String name;
	private InsuranceType type;
	private TermPeriod termPeriod;
	private boolean approvalStatus;

	public BasicInsuranceInfo(String name, InsuranceType insuranceType, TermPeriod termPeriod){
		this.name = name;
		this.type = insuranceType;
		this.termPeriod = termPeriod;
		this.approvalStatus = false;
	}
	public void approval() {
		this.approvalStatus = true;
	}
	public boolean isApproval() {
		return this.approvalStatus;
	}
	public String getName() {
		return name;
	}

	public InsuranceType getType() {
		return type;
	}

	public TermPeriod getTermPeriod() {
		return termPeriod;
	}

	public String toString() {
		String retStr = "";
		retStr += "보험명\t: "+this.name+"\n";
		retStr += "보험종목\t: "+this.type.getName()+"\n";
		retStr += "계약단위\t: "+this.termPeriod.getName()+"\n";
		return retStr;
	}
	
	public void finalize() throws Throwable {
		
	}

}