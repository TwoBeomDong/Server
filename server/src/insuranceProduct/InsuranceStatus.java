package insuranceProduct;


/**
 * @author dongyeonkim
 * @version 1.0
 * @created 14-5-2024 ���� 6:43:15
 */
public enum InsuranceStatus {
	adminApprovalWait("관리자 승인 대기"),
	adminDeny("관리자 거부"),
	adminReview("관리자 검토"),
	FSSConsent("금융감독원 인가"),
	FSSDeny("금융감독원 거부"),
	FSSApproval("금융감독원 승인"),
	trainRequest("교육 의뢰"),
	trainProgress("교육 진행"),
	trainWait("교육 대기"),
	;
	private String name;
	private InsuranceStatus(String name) {
		this.name = name;
	}
	public String getName() {
		return this.name;
	}
}