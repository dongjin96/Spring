function bdelete(b_num){
    //키워드 이름확인 // 같은 명칭을 가지고있는 키워드확인 {중복체크}
$.ajax({
    url:"/board/boarddelete",
    data:{"b_num":b_num},
    success: function(result){
    if(result==1){
    location.href="/board/boardlist";
    }else{
     alert("이미 삭제된게시물 혹은 오류발생");
    }
    }
});

}
