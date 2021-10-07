/* 
   	1. 이미 존재하는 아이디로 사용자가 가입할 때
   	2. 비번은 영숫특조합 8자
   	3. 전화번호는 숫자만 입력 
   	*/
   	
   	/* 이렇게 즉시실행함수로 안 감싸면 전역에 있는 confirmId 조작 가능 */
   (() => {
	   let confirmId = '';
		
		document.querySelector("#btnIdCheck").addEventListener('click',e => {
		
			// Id 속성값이 지정되어 있으면 id값으로 해당 요소객체 호출 가능
			let Id = userId.value;
		   
		   	// 서버야, 이 아이디가 DB에 존재하니?
				   
			if(Id){
				fetch('/member/id-check?userId=' + Id)
				.then(response => response.text())
				.then(text =>{

					if(text == 'avilable'){
						document.querySelector("#idCheck").innerHTML='사용 가능한 아이디 입니다';
						confirmId = id;
					}else if(text == 'disable'){
						document.querySelector("#idCheck").innerHTML='사용 불가능한 아이디 입니다';
					}else{
						document.querySelector("#idCheck").innerHTML='시스템 장애입니다';
					}
				})
			}
		});
	   
	   document.querySelector("#frm_join").addEventListener('submit', e => {
			
			let pwReg = /(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^a-zA-Zㄱ-힣0-9])(?=.{8,})/;
			let tellReg= /^\d{9,11}$/;
			
			/*	if(confirmId != userId.value){
				e.preventDefault();
				document.querySelector("#idCheck").innerHTML='아이디 중복검사를 통과하지 못했음';
			};
			
			
			if(!pwReg.test(password.value)){
				e.preventDefault();
				document.querySelector("#pwCheck").innerHTML='비번은 숫자, 영자, 특문 조합의 8자리 이상';
			};
			
			if(!tellReg.test(tell.value)){
				e.preventDefault();
				document.querySelector("#tellCheck").innerHTML='폰 번호 양식이 맞지 않음';
			};
			
			필터 걸어서 서버단에서 걸렀음. 클라이언트 단에 남긴건 아이디 중복검사.
			아이디 중복검사는 체크버튼땜에 살려둔다.
			
			
			
			
		})
	   
   })();