package com.kh.toy.common.code;

public enum MemberGrade {
	
	// 이넘(enumerated type) : 열거형
	// 서로 연관된 상수들의 집합.
	// 서로 연관된 상수들을 하나의 묶음으로 다루기 위한 enum만의 문법적 형식과 메서드를 제공
	
	// ENUM은 내부적으로  CLASS
	// ME00("일반,1") -> public static final String ME00 = new MemberGrade("일반",1);
	
	//회원등급코드 ME00은 INFO가 '일반'이고 연장가능횟수 1회이다.
	
	ME00("일반","user", 1),
	ME01("성실","user", 2),
	ME02("우수","user", 3),
	ME03("VIP","user", 4),
	
	AD00("super", "admin", 9999), // 슈퍼 관리자
	AD01("member", "admin", 9999), // 회원 관리자
	AD02("board", "admin", 9999); // 게시판 관리자
	
	public final String DESC;
	public final String ROLE;
	public final int EXTENDABLE_CNT;
	// 묶을거 두개 선언
	// 매개변수있는 생성자랑 게터 생성후 
	// 멤버메뉴에서 이걸 불러와
	// 아니면 final로 선언해서 그걸 퍼오던가.
	
	private MemberGrade(String desc, String role, int extendableCnt) {
		this.DESC = desc;
		this.ROLE = role;
		this.EXTENDABLE_CNT = extendableCnt;
	}

}
