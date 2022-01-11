/* 다음주소 api */
function sample4_execDaumPostcode() {
    new daum.Postcode({
           oncomplete: function(data) {
               // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

               // 도로명 주소의 노출 규칙에 따라 주소를 표시한다.
               // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
               var roadAddr = data.roadAddress; // 도로명 주소 변수
               var extraRoadAddr = ''; // 참고 항목 변수

               // 법정동명이 있을 경우 추가한다. (법정리는 제외)
               // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
               if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                   extraRoadAddr += data.bname;
               }
               // 건물명이 있고, 공동주택일 경우 추가한다.
               if(data.buildingName !== '' && data.apartment === 'Y'){
                  extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
               }
               // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
               if(extraRoadAddr !== ''){
                   extraRoadAddr = ' (' + extraRoadAddr + ')';
               }

               // 우편번호와 주소 정보를 해당 필드에 넣는다.
               document.getElementById('sample4_postcode').value = data.zonecode;
               document.getElementById("sample4_roadAddress").value = roadAddr;
               document.getElementById("sample4_jibunAddress").value = data.jibunAddress;

               // 참고항목 문자열이 있을 경우 해당 필드에 넣는다.
               if(roadAddr !== ''){
                   document.getElementById("sample4_extraAddress").value = extraRoadAddr;
               } else {
                   document.getElementById("sample4_extraAddress").value = '';
               }

               var guideTextBox = document.getElementById("guide");
               // 사용자가 '선택 안함'을 클릭한 경우, 예상 주소라는 표시를 해준다.
               if(data.autoRoadAddress) {
                   var expRoadAddr = data.autoRoadAddress + extraRoadAddr;
                   guideTextBox.innerHTML = '(예상 도로명 주소 : ' + expRoadAddr + ')';
                   guideTextBox.style.display = 'block';

               } else if(data.autoJibunAddress) {
                   var expJibunAddr = data.autoJibunAddress;
                   guideTextBox.innerHTML = '(예상 지번 주소 : ' + expJibunAddr + ')';
                   guideTextBox.style.display = 'block';
               } else {
                   guideTextBox.innerHTML = '';
                   guideTextBox.style.display = 'none';
               }
           }
       }).open();
}
/* 다음주소 api end */

/*아이디 유효성검사 시작*/
$(function(){

    $("#m_id").keyup(function(){
    var m_id=$("#m_id").val();
    var idj=/^[a-z0-9]{5,15}$/ //정규식 표현식 영소문자 5~15 글자만 허용
    if(!idj.test(m_id)){
         $("#idcheck").html("영소문자 5~15글자만 가능합니다")

    }else{

    //아이디 중복체크 비동기 통신을 시작한다
    $.ajax({
    url:"/member/idcheck",
    method:"POST",
    data:{"m_id":m_id},
    success:function(result){
        if(result==1){
        $("#idcheck").html("현재 사용중인 아이디 입니다")
         $('input[type="submit"]').prop('disabled', true);//전송버튼막기
        }else{
        $("#idcheck").html("사용가능");
        }

         } // success send
             }); // ajax 함수  end
         } // else end
      }); // 키보드이벤트 함수 end

/*아이디 유효성검사 끝*/
/*비밀번호 유효성검사 시작*/
    $("#m_password").keyup(function(){
        var pwj=/^(?=.*[A-Za-z])(?=.*\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\d~!@#$%^&*()+|=]{8,16}$/;
                // 숫자', '문자', '특수문자' 무조건 1개 이상, 비밀번호 '최소 8자에서 최대 16자'까지 허용
        var m_password = $("#m_password").val();
        if(!pwj.test(m_password)){
            $("#passwordcheck").html(" 숫자', '문자', '특수문자'포함 8자에서 최대 16자'까지 허용");
             $('input[type="submit"]').prop('disabled', true);//전송버튼막기
        }else{
            $("#passwordcheck").html("사용가능");
         }
    });

     // 패스워드 확인 유효성검사
        $("#m_passwordconfirm").keyup( function(){
                   var pwj = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\d~!@#$%^&*()+|=]{8,16}$/;
                   // 숫자', '문자', '특수문자' 무조건 1개 이상, 비밀번호 '최소 8자에서 최대 16자'까지 허용
                   var m_password = $("#m_password").val();
                   var m_passwordconfirm = $("#m_passwordconfirm").val();
                    if( !pwj.test(m_passwordconfirm ) ){
                         $("#passwordcheck").html("숫자', '문자', '특수문자' 포함 , '최소 8문자~16글자 허용.");

                    }else if(  m_password != m_passwordconfirm   ){
                         $("#passwordcheck").html("서로 패스워드가 다릅니다.");

                    }else{
                        $("#passwordcheck").html("사용가능");
                    }
            });

    /*비밀번호 유효성검사 끝*/


     /*이름유효성검사 시작*/
    // 이름 유효성검사
           $("#m_name").keyup( function(){
               var namej = /^[A-Za-z가-힣]{1,15}$/;	// 이름 정규표현식
                var m_name = $("#m_name").val();
               if( !namej.test(m_name ) ){
                    $("#namecheck").html("영대문자/한글 1~15 허용");

               }else{
                    $("#namecheck").html("사용가능");
               }
           });
    /*이름유효성검사 끝*/

    /*//성별 유효성검사
    if(!$("#m_sex1").checked|| $("#m_sex2").checked){
     $('input[type="submit"]').prop('disabled', true);//전송버튼막기
    }

    });*/
    //연락처 유효성검사
     $("#m_phone").keyup( function(){
     var phonej = /^01([0|1|6|7|8|9]?)-?([0-9]{3,4})-?([0-9]{4})$/; // 연락처
         var m_phone = $("#m_phone").val();
        if(!phonej.test(m_phone)){
        $("#phonecheck").html("000-0000-0000 형식으로 입력해주세요");

        }else{
              $("#phonecheck").html("사용가능");
        }
        });

    //주소 유효성검사

      // 이메일 유효성검사
            $("#m_email").keyup( function(){
                    var emailj = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
                    var m_email = $("#m_email").val();
                    if( !emailj.test(m_email ) ){
                      $("#emailcheck").html("이메일 형식으로 입력해주세요");

                      }else{
                      // 이메일 중복체크 비동기 통신
                              $.ajax({
                                 url : "/member/emailcheck" ,
                                 data :{ "m_email" : m_email } ,
                                 success : function( result ){
                                       if( result == 1 ){
                                          $("#emailcheck").html("현재 사용중인 이메일 입니다.");
                                       }else{
                                          $("#emailcheck").html("사용가능");
                                       }
                                   } // success send
                             });
                      }
            });
    //주소 /입력제한
     // 주소에 / 입력 제한
             $("#sample4_postcode").keyup( function(){
                    var address1 = $("#sample4_postcode").val();
                    if(  address1.indexOf("/") != -1 ){ $("#addresscheck").html(" 주소에 '/' 특수문자 포함 불가 "); return false;   }
                    if( address1 != null ) {  $("#addresscheck").html("사용가능"); }
             });
             $("#sample4_roadAddress").keyup( function(){
                      var address2 = $("#sample4_roadAddress").val();
                        if(  address2.indexOf("/") != -1 ){  $("#addresscheck").html("주소에 '/' 특수문자 포함 불가 "); return false;     }
                        if( address2 != null ) {  $("#addresscheck").html("사용가능"); }
             });
             $("#sample4_jibunAddress").keyup( function(){
                      var address3 = $("#sample4_jibunAddress").val();
                        if(  address3.indexOf("/") != -1 ){  $("#addresscheck").html(" 주소에  '/' 특수문자 포함 불가 "); return false;     }
                        if( address3 != null ) {  $("#addresscheck").html("사용가능"); }
               });
             $("#sample4_detailAddress").keyup( function(){
                       var address4 = $("#sample4_detailAddress").val();
                         if(  address4.indexOf("/") != -1 ){ $("#addresscheck").html("주소에  '/' 특수문자 포함 불가 "); return false;     }
                        if( address4 != null ) {  $("#addresscheck").html("사용가능"); }
              });

                $("#formsubmit").click( function(){

                             if(  $("#idcheck").html() != "사용가능" ){
                                 alert(" 아이디가 불가능합니다 . ");
                             }else if(  $("#passwordcheck").html() != "사용가능" ){
                                  alert(" 패스워드 불가능합니다 . ");
                              }
                              else if(  $("#namecheck").html() != "사용가능" ){
                                  alert(" 이름 불가능합니다 . ");
                               }
                               else if(  $("#phonecheck").html() != "사용가능" ){
                                     alert(" 연락처가 불가능합니다 . ");
                                }
                                else if(   $("#emailcheck").html() != "사용가능" ){
                                      alert(" 이메일 불가능합니다 . ");
                                }else if(   $("#addresscheck").html() != "사용가능" ){
                                          alert(" 주소소 불가합니다 . ");
                                  }else{
                                     $("form").submit(); // 모든 유효성검사 통과시 폼 전송
                                  }
                        });



});//함수 end
