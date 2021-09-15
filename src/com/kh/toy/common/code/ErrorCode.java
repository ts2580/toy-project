package com.kh.toy.common.code;

public enum ErrorCode {
	
	DATABASE_ACCESS_ERROR("DB와 통신중 에러 발생"),
	VALIDATOR_FIAL_ERROR("부적절한 양식의 데이터임"),
	MAIL_SEND_FAIL_EXCEPTION("이메일 발송중 에러"),
	HTTP_CONNECT_ERROR("HTTP통신 중 에러가 발생"),
	AYTENTICATION_FAILED_ERROR("유효하지 않은 인증입니다"),
	UNAUTHORIZED_PAGE_ERROR("접근 권한이 없는페이지 입니다"),
	FAILED_FILE_UPLOAD("파일 업로드에 실패하였음");
	
	public final String MESSAGE;
	public final String URL;

	private ErrorCode(String msg) {
			this.MESSAGE = msg;
			this.URL = "/index";
	}
	
	private ErrorCode(String msg, String url) {
		this.MESSAGE = msg;
		this.URL = url;
}
}
