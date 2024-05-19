package insuranceProduct;

import java.time.LocalDate;

import user.Employee;

/**
 * @author dongyeonkim
 * @version 1.0
 * @created 14-5-2024 ���� 6:43:15
 */
public class StatusChangeInfo {
	private InsuranceStatus insuranceStatus;
	private Employee personInCharge;
	private LocalDate changeDate;

	public StatusChangeInfo(){

	}

	public InsuranceStatus getInsuranceStatus() {
		return insuranceStatus;
	}

	public void setInsuranceStatus(InsuranceStatus insuranceStatus) {
		this.insuranceStatus = insuranceStatus;
	}

	public Employee getPersonInCharge() {
		return personInCharge;
	}

	public void setPersonInCharge(Employee personInCharge) {
		this.personInCharge = personInCharge;
	}

	public LocalDate getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(LocalDate changeDate) {
		this.changeDate = changeDate;
	}
	
	public void finalize() throws Throwable {
		
	}

}