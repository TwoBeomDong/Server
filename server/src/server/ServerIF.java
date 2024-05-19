package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedHashMap;

import insuranceProduct.InsuranceType;
import insuranceProduct.TermPeriod;

public interface ServerIF extends Remote {
	OutputMessage registerInsuranceProduct(String insuranceName, InsuranceType insuranceType, TermPeriod termPeriod, LinkedHashMap<String,String> BasicPaperList)throws RemoteException;
	OutputMessage getFreshInsuranceList()throws RemoteException;
	OutputMessage getFreshInsuranceProcessList(int insuranceId)throws RemoteException;
	OutputMessage getBasicInsuranceInfo(int insuranceId)throws RemoteException;
	OutputMessage approvalBasicInsuranceInfo(int insuranceId, boolean approvalStatus)throws RemoteException;
	OutputMessage getMemberPaperForm(int insuranceId)throws RemoteException;
	OutputMessage approvalMemberPaperForm(int insuranceId, boolean approvalStatus)throws RemoteException;
	OutputMessage decideStandardRate(int insuranceId, float standardRate)throws RemoteException;
	OutputMessage setProductApprovalPaper(int insuranceId, String info)throws RemoteException;
}
