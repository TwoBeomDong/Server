package insuranceProduct;

import java.util.Vector;

/**
 * @author dongyeonkim
 * @version 1.0
 * @created 14-5-2024 ���� 6:43:15
 */
public class InsuranceProduct {
	private class StandardRate {
		// 요율을 final로 선언하기 위해 구조체 클래스를 만듦.
		public final float rate;
		public StandardRate(float rate) {
			this.rate = rate;
		}
	}

	private BasicInsuranceInfo basicInsuranceInfo;
	private MemberPaperForm memberPaperForm;
	private Vector<StatusChangeInfo> statusChangeList = new Vector<StatusChangeInfo>();
	private InsuranceStatus currentStatus;
	private ProductApprovalPaper productApprovalPaper;
	private int insuranceID;
	private StandardRate standardRate;

	public InsuranceProduct(BasicInsuranceInfo basicInsuranceInfo, MemberPaperForm memberPaperForm, int insuranceID){
		this.basicInsuranceInfo = basicInsuranceInfo;
		this.memberPaperForm = memberPaperForm;
		this.insuranceID = insuranceID;
	}

	public float getStandardRate() {
		if(this.standardRate != null) return this.standardRate.rate;
		return -1;
	}
	public boolean setStandardRate(float standardRate) {
		if(this.standardRate == null) {
			this.standardRate = new StandardRate(standardRate);
			return true;
		}
		return false;
	}
	
	public InsuranceStatus getCurrentStatus() {
		return currentStatus;
	}
	private void setCurrentStatus(InsuranceStatus currentStatus) {
		this.currentStatus = currentStatus;
	}
	public void changeStatus(StatusChangeInfo statusChangeInfo) {
		this.statusChangeList.add(statusChangeInfo);
		this.setCurrentStatus(statusChangeInfo.getInsuranceStatus());
	}

	public void setBasicInsuranceInfo(BasicInsuranceInfo basicInsuranceInfo){
		this.basicInsuranceInfo = basicInsuranceInfo;
	}
	public BasicInsuranceInfo getBasicInsuranceInfo() {
		return this.basicInsuranceInfo;
	}

	public void setMemberPaperForm(MemberPaperForm memberPaperForm){
		this.memberPaperForm = memberPaperForm;
	}
	public MemberPaperForm getMemberPaperForm(){
		return this.memberPaperForm;
	}

	public void setProductApprovalPaper(ProductApprovalPaper productApprovalPaper){
		this.productApprovalPaper = productApprovalPaper;
	}
	public ProductApprovalPaper getProductApprovalPaper(){
		return this.productApprovalPaper;
	}
	
	public int getID() {
		return this.insuranceID;
	}
	public boolean equals(int id) {
		return this.insuranceID == id;
	}
	public String toString() {
		return this.insuranceID + "\t:" + this.basicInsuranceInfo.getName();
	}
	
	public void finalize() throws Throwable {

	}
}