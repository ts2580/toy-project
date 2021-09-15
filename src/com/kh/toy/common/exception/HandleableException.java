package com.kh.toy.common.exception;

import com.kh.toy.common.code.ErrorCode;

public class HandleableException extends RuntimeException{
	
	private static final long serialVersionUID = 7847451612288681161L;
	public ErrorCode error;

	// 1. 콘솔에 로그(사실 자동으로 써짐)
	// 2. result.jsp 사용해 사용자에게 msg 띄워주기, 
	//    그리고 url 재지정. 
	public HandleableException(ErrorCode error) {
		this.error = error;
		this.setStackTrace(new StackTraceElement[0]);
	}
	
	public HandleableException(ErrorCode error, Exception e) {
		this.error = error;
		e.printStackTrace();
		this.setStackTrace(new StackTraceElement[0]);
	}
	
	

}
