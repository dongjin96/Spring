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
function boardwrite(){
    //form 태그 가져오기
    var formData = new FormData(form);
    // 폼을 컨트롤러에게 비동기 전송
    $.ajax({
        type:"POST",
        url : "/board/boardwritecontroller",
        data : formData,
        processData : false,
        contentType : false, //첨부파일 보낼떄.
        success : function(data){
        if(data == 1){
            location.href="/board/boardlist";
        }

        }
    });
}