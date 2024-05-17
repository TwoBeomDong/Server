package server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.LinkedHashMap;

import contractInsurance.ContractInsuranceList;
import insuranceProduct.BasicInsuranceInfo;
import insuranceProduct.InsuranceProduct;
import insuranceProduct.InsuranceProductList;
import insuranceProduct.InsuranceStatus;
import insuranceProduct.InsuranceType;
import insuranceProduct.MemberPaperForm;
import insuranceProduct.ProductApprovalPaper;
import insuranceProduct.StatusChangeInfo;
import insuranceProduct.TermPeriod;
import user.CustomerList;
import user.EmployeeList;

public class Server extends UnicastRemoteObject implements ServerIF{
	
	private static final long serialVersionUID = 1L;

	private CustomerList customerList;
	private EmployeeList employeeList;
	// data에서 불러오는 게 아니라서 임시로 new 해뒀습니다.
	private InsuranceProductList insuranceProductList = new InsuranceProductList();
	private ContractInsuranceList ContractInsuranceList;

	protected Server() throws RemoteException{
		super();
	}
	
	public static void main(String[] args) {
		
		try {
			Server server = new Server();
			Naming.rebind("Server", server);
			
			System.out.println("Server is ready!!!");
		
			//data bind 비어있음
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void finalize() throws Throwable {

	}
	
	// return 값 String 변환
	public String registerInsuranceProduct(String insuranceName, InsuranceType insuranceType, TermPeriod termPeriod, LinkedHashMap<String,String> BasicPaperList)throws RemoteException{
		// 사용자 입력 정보 검증은 나중에 추가
		
		// ----- 확인용 print -----
        System.out.println("-----확인-----");
        System.out.println("보험 이름: " + insuranceName);
        System.out.println("선택된 보험 타입: " + insuranceType.getName());
        System.out.println("선택된 보험 기간: " + termPeriod.getName());
        BasicPaperList.forEach((key, value) -> System.out.println(key + ": " + value));
        System.out.println("-------------");
        // ----------
		BasicInsuranceInfo basicInsuranceInfo = new BasicInsuranceInfo(insuranceName, insuranceType, termPeriod);
		MemberPaperForm memberPaperForm = new MemberPaperForm(BasicPaperList);
		this.insuranceProductList.addFreshInsutranceProduct(basicInsuranceInfo, memberPaperForm);
		
		OutputMessage resultMessage = new OutputMessage(ServerMessage.Insurance_register_complete);
		return resultMessage.getMessage();
	}
	
	public String getFreshInsuranceList()throws RemoteException{
		OutputMessage resultMessage = new OutputMessage(this.insuranceProductList.getFreshInsuranceString());
		return resultMessage.getMessage();
	}
	public String getFreshInsuranceProcessList(int insuranceId)throws RemoteException{
		// output	: 특정 보험에서 처리할 수 있는 일 리스트	
		//ex_보험 관리자 승인 / 보험 교육 승인
		String temp = "0\t: 보험 승인";
		OutputMessage resultMessage = new OutputMessage(temp);
		return resultMessage.getMessage();
	}
	public String getBasicInsuranceInfo(int insuranceId)throws RemoteException {
		InsuranceProduct insurance = this.insuranceProductList.getFreshInsurance(insuranceId); 
		OutputMessage resultMessage;
		if(insurance == null) { // 검토
			resultMessage = new OutputMessage(ServerMessage.Irregular_request);
			return resultMessage.getMessage();
		}
		resultMessage = new OutputMessage(insurance.getBasicInsuranceInfo().toString());
		return resultMessage.getMessage();
	}
	public String approvalBasicInsuranceInfo(int insuranceId, boolean approvalStatus)throws RemoteException {
		InsuranceProduct insurance = this.insuranceProductList.getFreshInsurance(insuranceId); 
		OutputMessage resultMessage;
		if(approvalStatus && insurance != null) {
			insurance.getBasicInsuranceInfo().approval();
			resultMessage = new OutputMessage(ServerMessage.Insurance_approve_admin_complete);
			return resultMessage.getMessage();
		}
		resultMessage = new OutputMessage(ServerMessage.Irregular_request);
		return resultMessage.getMessage();
	}
	public String getMemberPaperForm(int insuranceId)throws RemoteException {
		InsuranceProduct insurance = this.insuranceProductList.getFreshInsurance(insuranceId); 
		OutputMessage resultMessage;
		if(insurance == null) { // 검토
			resultMessage = new OutputMessage(ServerMessage.Irregular_request);
			return resultMessage.getMessage();
		}
		resultMessage = new OutputMessage(insurance.getMemberPaperForm().toString());
		return resultMessage.getMessage();
	}
	public String approvalMemberPaperForm(int insuranceId, boolean approvalStatus)throws RemoteException {
		InsuranceProduct insurance = this.insuranceProductList.getFreshInsurance(insuranceId); 
		OutputMessage resultMessage;
		if(approvalStatus && insurance != null) {
			insurance.getMemberPaperForm().approval();
			StatusChangeInfo statusChangeInfo = new StatusChangeInfo();
			statusChangeInfo.setChangeDate(LocalDate.now());
			statusChangeInfo.setPersonInCharge(null);
			statusChangeInfo.setInsuranceStatus(InsuranceStatus.adminReview);
			insurance.changeStatus(statusChangeInfo);
			
			resultMessage = new OutputMessage(ServerMessage.Insurance_approve_admin_complete);
			return resultMessage.getMessage();
		}
		resultMessage = new OutputMessage(ServerMessage.Irregular_request);
		return resultMessage.getMessage();
	}
	public String decideStandardRate(int insuranceId, float standardRate)throws RemoteException {
		InsuranceProduct insurance = this.insuranceProductList.getFreshInsurance(insuranceId); 
		OutputMessage resultMessage;
		if(insurance != null) {
			if(insurance.setStandardRate(standardRate)) {
				 resultMessage = new OutputMessage(ServerMessage.Standard_rate_decide_complete); 
				return resultMessage.getMessage();
			}
			else {
				resultMessage = new OutputMessage(ServerMessage.Standard_rate_decide_failed); 
				return resultMessage.getMessage();
			}
		}
		resultMessage = new OutputMessage(ServerMessage.Irregular_request); 
		return resultMessage.getMessage();
	}
	public String setProductApprovalPaper(int insuranceId, String info)throws RemoteException {
		// 우선 간단하게 문자열로 받기.
		InsuranceProduct insurance = this.insuranceProductList.getFreshInsurance(insuranceId); 
		OutputMessage resultMessage;
		if(insurance != null) {
			ProductApprovalPaper pap = new ProductApprovalPaper(info);
			insurance.setProductApprovalPaper(pap);
			
			StatusChangeInfo statusChangeInfo = new StatusChangeInfo();
			statusChangeInfo.setChangeDate(LocalDate.now());
			statusChangeInfo.setPersonInCharge(null);
			statusChangeInfo.setInsuranceStatus(InsuranceStatus.FSSConsent);
			insurance.changeStatus(statusChangeInfo);
			
			resultMessage = new OutputMessage(ServerMessage.Product_approval_paper_set_complete);
			return resultMessage.getMessage();
		}
		resultMessage = new OutputMessage(ServerMessage.Irregular_request);
		return resultMessage.getMessage();
	}
	
	// 보험 가입부
	public void buyInsurance(){

	}
}