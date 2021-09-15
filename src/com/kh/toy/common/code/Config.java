package com.kh.toy.common.code;

public enum Config {
	
	DOMAIN("http://localhost:9090/"),
	COMPANY_EMAIL("trstyq@gmail.com"),
	SMTP_AUTHENTICATION_ID("trstyq@gmail.com"),
	SMTP_PASSWORD("4921904tr"),
	UPLOAD_PATH("C:\\CODE\\upload\\");
	
	public final String DESC;
	
	private Config(String desc) {
		this.DESC = desc;
	}

}
