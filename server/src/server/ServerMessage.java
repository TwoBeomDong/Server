package server;

public enum ServerMessage {
	Unauthorized_access("인가되지 않은 접근입니다."),
	Irregular_request("비정상적인 요청입니다."),
	Error_occurred("현재 서버가 불안정합니다. 잠시 후 다시 시도해주세요."),
	Password_setted("비밀번호가 등록되었습니다."),
	Password_different("비밀번호가 다릅니다."),
	
	Insurance_register_complete("보험 등록이 정상적으로 요청되었습니다. 관리자 승인 이후 금융감독원으로 인계됩니다."),
	Insurance_deny_complete("보험 승인이 정상적으로 거부되었습니다."),
	Insurance_approve_admin_complete("정상적으로 승인되었습니다."),
	Standard_rate_decide_complete("요율이 정상적으로 확정되었습니다."),
	Standard_rate_decide_failed("해당 보험은 이미 요율이 확정되었습니다."),
	Product_approval_paper_set_complete("상품 인가 품의서 작성이 완료되었습니다. 금융감독원으로 상품이 인계됩니다."),
	;
		
	private final String message;
	ServerMessage(String message) {
		this.message = message;
	}
		public String getMessage() {
		return this.message;
	}
}
