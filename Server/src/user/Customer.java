package user;


/**
 * @author dongyeonkim
 * @version 1.0
 * @created 14-5-2024 ���� 6:43:14
 */
public class Customer extends User {

	private int paymentBankAccount;

	public Customer(){

	}

	
	public int getPaymentBankAccount() {
		return paymentBankAccount;
	}


	public void setPaymentBankAccount(int paymentBankAccount) {
		this.paymentBankAccount = paymentBankAccount;
	}


	public void finalize() throws Throwable {
		super.finalize();
	}

}