package contractInsurance;

import java.sql.Date;

import insuranceProduct.InsuranceProduct;
import user.Customer;

/**
 * @author dongyeonkim
 * @version 1.0
 * @created 14-5-2024 ���� 7:03:55
 */
public class ContractInsurance {

	/**
	 * @author dongyeonkim
	 * @version 1.0
	 * @created 14-5-2024 ���� 7:03:55
	 */

	private Date contractDate;
	private Customer customer;
	private Date expireDate;
	private InsuranceProduct insuranceProduct;

	private ContractInsurance(){

	}

	public void finalize() throws Throwable {

	}
	
	public class ContractInsutanceBuilder {

		private ContractInsurance contractInsurance;

		public ContractInsutanceBuilder(){
			this.contractInsurance = new ContractInsurance(); 
		}

		public ContractInsutanceBuilder setContractDate(){
			return this;
		}

		public ContractInsutanceBuilder setCustomer(){
			return this;
		}

		public ContractInsutanceBuilder setExpireDate(){
			return this;
		}

		public ContractInsutanceBuilder setInsuranceProd(){
			return this;
		}

	}
}