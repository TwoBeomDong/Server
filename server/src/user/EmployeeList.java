package user;

import java.util.Vector;

/**
 * @author dongyeonkim
 * @version 1.0
 * @created 14-5-2024 ���� 6:43:14
 */
public class EmployeeList {

	private Vector<Employee> employeeList;

	public EmployeeList(){
		this.employeeList = new Vector<>();
		
		// test data
		Employee test = new Employee();
		test.setId("1234");
		test.setPassword("5678");
		this.employeeList.add(test);
	}

	public Vector<Employee> getEmployeeList(){
		return this.employeeList;
	}
	public boolean addEmployee(Employee employee) {
		return true;
	}
	public boolean deleteEmployee(String id) {
		return true;
	}
	public boolean checkPassword(String id, String password) {
		Employee e = this.getEmployee(id);
		return e.getPassword().equals(password);
	}
	public Employee getEmployee(String id) {
		for(Employee e : this.employeeList)
			if(e.equals(id)) return e;
		return null;
	}
}