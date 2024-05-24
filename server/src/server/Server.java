package server;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.Key;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.crypto.spec.SecretKeySpec;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

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
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import user.Authority;
import user.CustomerList;
import user.EmployeeList;
import user.User;

@Aspect
public class Server extends UnicastRemoteObject implements ServerIF{
	//attribute
	private static final long serialVersionUID = 1L;
    private static final String SECRET_KEY = "secretKey"; // 토큰 비밀 키
    private static final long EXPIRATION_TIME_MS = 1800000; // 토큰 유효시간 (30분)
    //
	private CustomerList customerList;
	private EmployeeList employeeList;
	// data에서 불러오는 게 아니라서 임시로 new 해뒀습니다.
	private InsuranceProductList insuranceProductList;
	private ContractInsuranceList ContractInsuranceList;

	private static final Logger logger = Logger.getLogger(Server.class.getName());
	protected Server() throws RemoteException, SecurityException, IOException {
		super();
		insuranceProductList = new InsuranceProductList();
		this.setLogger();
	}

	@Override
	public OutputMessage registerInsuranceProduct(String insuranceName, InsuranceType insuranceType, TermPeriod termPeriod, LinkedHashMap<String,String> BasicPaperList)throws RemoteException{
		// 사용자 입력 정보 검증은 나중에 추가
		
		// ----- debug print -----
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

		return new OutputMessage(ServerMessage.Insurance_register_complete);
	}
	@Override
	public OutputMessage getFreshInsuranceList()throws RemoteException{
		System.out.println(this.insuranceProductList.getFreshInsuranceString());
		return new OutputMessage(this.insuranceProductList.getFreshInsuranceString());
	}
	@Override
	public OutputMessage getFreshInsuranceProcessList(int insuranceId)throws RemoteException{
		// output	: 특정 보험에서 처리할 수 있는 일 리스트	
		//ex_보험 관리자 승인 / 보험 교육 승인
		String temp = "0\t: 보험 승인";
		return new OutputMessage(temp);
	}
	@Override
	public OutputMessage getBasicInsuranceInfo(int insuranceId)throws RemoteException {
		InsuranceProduct insurance = this.insuranceProductList.getFreshInsurance(insuranceId); 
		if(insurance == null) { // 검토
			return new OutputMessage(ServerMessage.Irregular_request);
		}
		return new OutputMessage(insurance.getBasicInsuranceInfo().toString());
	}
	@Override
	public OutputMessage approvalBasicInsuranceInfo(int insuranceId, boolean approvalStatus)throws RemoteException {
		System.out.println("approvalBasicInsuranceInfo");
		InsuranceProduct insurance = this.insuranceProductList.getFreshInsurance(insuranceId);
		if(insurance == null) return new OutputMessage(ServerMessage.Irregular_request);
		if(approvalStatus) {
			insurance.getBasicInsuranceInfo().approval();
			return new OutputMessage(ServerMessage.Insurance_approve_admin_complete);
		}else {
			StatusChangeInfo statusChangeInfo = new StatusChangeInfo();
			statusChangeInfo.setChangeDate(LocalDate.now());
			statusChangeInfo.setPersonInCharge(null);
			statusChangeInfo.setInsuranceStatus(InsuranceStatus.adminDeny);
			insurance.changeStatus(statusChangeInfo);
			return new OutputMessage(ServerMessage.Insurance_deny_complete);
		}
	}
	@Override
	public OutputMessage getMemberPaperForm(int insuranceId)throws RemoteException {
		InsuranceProduct insurance = this.insuranceProductList.getFreshInsurance(insuranceId); 
		if(insurance == null) { // 검토
			return new OutputMessage(ServerMessage.Irregular_request);
		}
		return new OutputMessage(insurance.getMemberPaperForm().toString());
	}
	@Override
	public OutputMessage approvalMemberPaperForm(int insuranceId, boolean approvalStatus)throws RemoteException {
		InsuranceProduct insurance = this.insuranceProductList.getFreshInsurance(insuranceId); 
		if(insurance == null) return new OutputMessage(ServerMessage.Irregular_request);
		if(approvalStatus) {
			insurance.getMemberPaperForm().approval();
			StatusChangeInfo statusChangeInfo = new StatusChangeInfo();
			statusChangeInfo.setChangeDate(LocalDate.now());
			statusChangeInfo.setPersonInCharge(null);
			statusChangeInfo.setInsuranceStatus(InsuranceStatus.adminReview);
			insurance.changeStatus(statusChangeInfo);
			
			return new OutputMessage(ServerMessage.Insurance_approve_admin_complete);
		}else {
			StatusChangeInfo statusChangeInfo = new StatusChangeInfo();
			statusChangeInfo.setChangeDate(LocalDate.now());
			statusChangeInfo.setPersonInCharge(null);
			statusChangeInfo.setInsuranceStatus(InsuranceStatus.adminDeny);
			insurance.changeStatus(statusChangeInfo);
			
			return new OutputMessage(ServerMessage.Insurance_deny_complete);
		}
		
	}
	@Override
	public OutputMessage decideStandardRate(int insuranceId, float standardRate)throws RemoteException {
		InsuranceProduct insurance = this.insuranceProductList.getFreshInsurance(insuranceId);
		if(insurance != null) {
			if(insurance.setStandardRate(standardRate)) {
				return new OutputMessage(ServerMessage.Standard_rate_decide_complete); 
			}
			else {
				return new OutputMessage(ServerMessage.Standard_rate_decide_failed); 
			}
		}
		return new OutputMessage(ServerMessage.Irregular_request); 
	}
	@Override
	public OutputMessage setProductApprovalPaper(int insuranceId, String info)throws RemoteException {
		// 우선 간단하게 문자열로 받기.
		InsuranceProduct insurance = this.insuranceProductList.getFreshInsurance(insuranceId); 
		if(insurance != null) {
			ProductApprovalPaper pap = new ProductApprovalPaper(info);
			insurance.setProductApprovalPaper(pap);
			
			StatusChangeInfo statusChangeInfo = new StatusChangeInfo();
			statusChangeInfo.setChangeDate(LocalDate.now());
			statusChangeInfo.setPersonInCharge(null);
			statusChangeInfo.setInsuranceStatus(InsuranceStatus.FSSConsent);
			insurance.changeStatus(statusChangeInfo);
			
			return new OutputMessage(ServerMessage.Product_approval_paper_set_complete);
		}
		return new OutputMessage(ServerMessage.Irregular_request);
	}
	
	// 보험 가입부
	
	public void buyInsurance(){

	}
	
	/*
	 * 서버 메소드 처리를 위한 메타 메소드
	 * 
	 * 
	 */
	private boolean checkPassword(String id, String password) throws RemoteException{
//		return data.checkPassword(ID, PWD);
		if(this.employeeList.checkPassword(id, password)) return true;
		if(this.customerList.checkPassword(id, password)) return true;
		return false;
	}
	private User getUser(String id) {
		if(this.customerList.getCustomer(id) != null) {
			return this.customerList.getCustomer(id);
		}
		if(this.employeeList.getEmployee(id) != null) {
			return this.employeeList.getEmployee(id);
		}
		return null;
	}
	
	/*
	 * 기술적인 부분에 대한 코드
	 * 
	 * 1. 토큰 발행
	 * 2. 토큰 해석
	 * 3. aspect로 클라이언트 요청 검사
	 * 4. logger 세팅
	 */
    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    public String generateToken(String id, String password) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Key signingKey = getSigningKey();

        // 토큰 생성부
        return Jwts.builder()
                .setId(id) 												// JWT ID
                .setHeaderParam("password", password)					// 비밀번호
                .setIssuedAt(now) 										// 발행 시간
                .setExpiration(new Date(nowMillis + EXPIRATION_TIME_MS))// 만료 시간
                .signWith(signingKey, SignatureAlgorithm.HS256) 		// 서명 알고리즘 및 비밀 키
                .compact();
    }
    private Claims verifyToken(String jwt) {
        try {
            Key signingKey = getSigningKey();

            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(jwt);

            Claims claims = claimsJws.getBody();
            
            return claims;
        } catch (Exception e) {
            System.out.println("Invalid JWT token.");
            return null;
        }
    }
    
    @Around("execution(* *_Aspect(..)) && args(.., token, scope, logInfo)")
    public Object aroundMethodExecution(ProceedingJoinPoint joinPoint, String token, int scope, String logInfo) throws Throwable {
    	Claims claims = this.verifyToken(token);
    	if(claims == null) {
    		logger.info("Invalid JWT token Requested");
    		return new OutputMessage(ServerMessage.Irregular_request);
    	}
    	String id = claims.getId();
    	String password = claims.get("password", String.class);
    	int authority;
      if(this.checkPassword(id, password)) authority = this.getUser(id).getAuthority().getLevel();
      else authority = Authority.None.getLevel();
      
    	if (authority < scope) {
            logger.info("Unauthorized Access Occurred");
            return new OutputMessage(ServerMessage.Unauthorized_access);
        }
        Object result = joinPoint.proceed();
        return result;
    }
	public void setLogger() throws SecurityException, IOException {
        FileHandler fileHandler = new FileHandler("server.log");
        logger.addHandler(fileHandler);
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);
        fileHandler.setFormatter(new SimpleFormatter());
        
        InetAddress localHost = InetAddress.getLocalHost();
        String hostName = localHost.getHostName();
        logger.info("host name is "+hostName);
        logger.info("server start at "+LocalDateTime.now());
	}
}