package com.kh.toy.common.exception;

public class PageNotFoundException extends RuntimeException{

	private static final long serialVersionUID = -1313747291601761454L;

		public PageNotFoundException() {
			// 스택트레이스 날려버림
			this.setStackTrace(new StackTraceElement[0]);
		}
}
