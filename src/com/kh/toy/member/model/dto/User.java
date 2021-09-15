package com.kh.toy.member.model.dto;

public class User {
	//Builder 패턴 공부용. 사용은 안할꺼임
	
	// 객체 생성패턴
	// 1. 점층적 생성자 패턴
	// 생성자의 매개변수를 통해 객체의 속성을 초기화하고 생성하는 패턴
	// 단점 : 코드만 보고 생성자의 매개변수가 어떤 객체의 속성을 초기화하는 값인지 알기 어렵다.
	//   	  가독성 개구림
	
	// 2. Java Bean 패턴(필드변수는 private, getter, setter, toString 등등. 검색해보던가)
	// getter/setter
	// 단점 : public 메서드인 setter를 사용해 속성을 초기화 하기 떄문에, 변경 불가능한(immutable) 객체를 만들지 못한다.
	
	// 3. ***builder 패턴
	// 가독성과 변경불가능한 객체를 만들 수 없다는 단점을 극복한 디자인 패턴.
	// Effective Java에서 제시된 Builder 패턴.
	private String userId;
	private String password;
	private String email;
	private String tell;
	
	// 생성자는 당연히 private처리. 외부에서 직접 객체생성 차단.
	// 객체의 생성은 오직 Factory Class인 Builder를 통해서만 생성
	
	private User(UserBuilder builder) {
		this.userId = builder.userId;
		this.password = builder.password;
		this.email = builder.email;
		this.tell = builder.tell;
	}
	
	// userBuilder를 반환.
	public static UserBuilder builder() {
		return new UserBuilder();
	}
	
	
	// 생성될 User객체의 속성을 초기화 하기 위한 값을 전달 받고, User의 인스턴스를 생성해줄 inner class
	public static class UserBuilder {
		
		private String userId;
		private String password;
		private String email;
		private String tell;
		
		public UserBuilder userId(String userId) {
			this.userId = userId;
			return this;
			//userId 속성이 초기화 된 UserBuilder
		}
		
		public UserBuilder password(String password) {
			this.password = password;
			return this;
		}
		
		public UserBuilder email(String email) {
			this.email = email;
			return this;
		}
		
		public UserBuilder tell(String tell) {
			this.tell = tell;
			return this;
		}
		
		public User build() {
			return new User(this);
		}
		
		
	}


	@Override
	public String toString() {
		return "User [getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}
	
	/*
	 * public void setTell(String tell) {
	 *  바뀌어도 되는값은 이렇게 setter만들어줘
	 * }
	 */
	
	
	
}
