package com.kh.toy.common.exception;

import com.kh.toy.common.code.ErrorCode;

public class DataAccessException extends HandleableException {
	
	private static final long serialVersionUID = 52158746;
	
	// 예외처리가 강제되지 않는 UncheckedException
	public DataAccessException(Exception e) {
		super(ErrorCode.DATABASE_ACCESS_ERROR);
	}

}
