package user;

import java.util.Vector;

/**
 * @author dongyeonkim
 * @version 1.0
 * @created 14-5-2024 ���� 6:43:14
 */
public class CustomerList {

	private Vector<Customer> customerList;

	public CustomerList(){
		this.customerList = new Vector<>();
		
		//test data
		Customer test = new Customer();
		test.setId("abc");
		test.setPassword("def");
		test.setPaymentBankAccount(12345678);
	}

	public void finalize() throws Throwable {

	}

	public Vector<Customer> getCustomerList(){
		return this.customerList;
	}
	public boolean addCustomer(Customer customer) {
		return true;
	}
	public boolean deleteCustomer(String id) {
		return true;
	}
}