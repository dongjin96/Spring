function rdelete(rnum){

    $.ajax({
       url:"/admin/delete",
       data:{"rnum":rnum},
       success: function(data){

        if(data=1){

        location.href="admin/roomlist";
       }
       }
});
}
function activeupdate(rnum,upactive){
   $.ajax({
    url:"/admin/activeupdate",
    data :{"rnum":rnum,"upactive":upactive},
    success: function(data){
    alert(data);
      /*  if(data=1){
            location.href="/admin/roomlist";
        }else{
        $("#activemsg").html("현재 동일한 상태 입니다. ");
        }*/
    }
   });
}
function roomupdate(field , filecontents , rnum){

    if( field == 'raddress'  ){ // 주소 변경
        alert("주소 버튼 클릭");
        $("#updatemsg").html(
         '<input type="text" id="sample5_address" placeholder="주소" name="raddress" class="form-control">' +
         '<input type="button" onclick="sample5_execDaumPostcode()" value="주소 검색" class="form-control"><br>' +
         '<div id="map" style="width:300px;height:300px;margin-top:10px;display:none"></div>' +
         '<input type="hidden" name="addressy"  id="addressy">' +
         '<input type="hidden" name="addressx"  id="addressx">' +
         '<button onclick=update("+rnum+")>수정</button> </div>'
         );

    }else if( field == 'rimg' ){ // 이미지 변경
        alert("이미지버튼 클릭 ");
    }
    else{
         $("#updatemsg").html(
               "<p> 내용수정후 수정버튼 눌러주세요! </p> "+
               "<div> <input type='hidden' value="+field+" id='field' >  </div>" +
               "<div> <input type='text' value="+filecontents+" id='newcontents' > " +
               "<button onclick=update("+rnum+")>수정</button> </div>"
         );
    }
}

 function update(rnum){
    var newcontents = $("#newcontents").val();
    var field = $("field").val();
    $.ajax({
        url:"/admin/update",
        data:{"rnum":rnum,"field":field,"newcontents":newcontents},
        success:function(data){
            if(data==1){
                location.herf="/admin/roomlist";
            }else{
                $("#deletemsg").html("[방수정 실패].");
            }
        }
    });
 }