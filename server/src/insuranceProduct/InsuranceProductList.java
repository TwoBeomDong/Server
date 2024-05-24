package insuranceProduct;

import java.time.LocalDate;
import java.util.Vector;

/**
 * @author dongyeonkim
 * @version 1.0
 * @created 14-5-2024 ���� 6:43:15
 */
public class InsuranceProductList {

	private Vector<InsuranceProduct> freshInsuranceProductList;
	private Vector<InsuranceProduct> RegularInsuranceProductList;

	public InsuranceProductList() {
		this.freshInsuranceProductList = new Vector<>();
		this.RegularInsuranceProductList = new Vector<>();
	}

	public void finalize() throws Throwable {

	}

	public boolean addFreshInsutranceProduct(BasicInsuranceInfo basicInsuranceInfo, MemberPaperForm memberPaperForm) {
		int id = this.freshInsuranceProductList.size() + this.RegularInsuranceProductList.size();
		InsuranceProduct insuranceProduct = new InsuranceProduct(basicInsuranceInfo, memberPaperForm, id);
		
		// 상태 저장부 -> 나중에 빌더 패턴 쓰거나 다른 방식으로 수정하기.
		StatusChangeInfo statusChangeInfo = new StatusChangeInfo();
		statusChangeInfo.setChangeDate(LocalDate.now());
		statusChangeInfo.setPersonInCharge(null);
		statusChangeInfo.setInsuranceStatus(InsuranceStatus.adminApprovalWait);
		insuranceProduct.changeStatus(statusChangeInfo);
		
		this.freshInsuranceProductList.add(insuranceProduct);
		
		return true;
	}

	public boolean registerRegularInsutranceProduct(InsuranceProduct insuranceProduct) {
		for(InsuranceProduct fresh : freshInsuranceProductList) {
			if(fresh.equals(insuranceProduct.getID())) {
				this.freshInsuranceProductList.remove(insuranceProduct);
				this.RegularInsuranceProductList.add(insuranceProduct);
				return true;
			}
		}
		return false;
	}

	public Vector<InsuranceProduct> getFreshInsuranceList() {
		return this.freshInsuranceProductList;
	}
	public String getFreshInsuranceString() {
		return this.getInsuranceString(this.freshInsuranceProductList);
	}
	public InsuranceProduct getFreshInsurance(int id) {
		return this.getInsurance(this.freshInsuranceProductList, id);
	}
	
	public Vector<InsuranceProduct> getRegularInsuranceList() {
		return this.RegularInsuranceProductList;
	}
	public String getRegularInsuranceString() {
		return this.getInsuranceString(this.RegularInsuranceProductList);
	}
	public InsuranceProduct getRegularInsurance(int id) {
		return this.getInsurance(this.RegularInsuranceProductList, id);
	}
	
	private String getInsuranceString(Vector<InsuranceProduct> list) {
		String retStr = "";
		for(InsuranceProduct i : list) {
			retStr += i.toString()+"\n";
		}
		return retStr;
	}
	private InsuranceProduct getInsurance(Vector<InsuranceProduct> list, int id) {
		for(InsuranceProduct i : list) {
			if(i.equals(id)) return i;
		}
		return null;
	}
}