    var map = new kakao.maps.Map(document.getElementById('map'), { // 지도를 표시할 div
          center : new kakao.maps.LatLng(36.2683, 127.6358), // 지도의 중심좌표
          level : 14 // 지도의 확대 레벨 숫자가 낮을수록 확대된다
      });

      // 마커 클러스터러를 생성합니다
      // 마커 클러스터러를 생성할 때 disableClickZoom 값을 true로 지정하지 않은 경우
      // 클러스터 마커를 클릭했을 때 클러스터 객체가 포함하는 마커들이 모두 잘 보이도록 지도의 레벨과 영역을 변경합니다
      // 이 예제에서는 disableClickZoom 값을 true로 설정하여 기본 클릭 동작을 막고
      // 클러스터 마커를 클릭했을 때 클릭된 클러스터 마커의 위치를 기준으로 지도를 1레벨씩 확대합니다
        // 클러스터러 = 마커들을 추가해야한다
   var clusterer = new kakao.maps.MarkerClusterer({
        map: map, // 마커들을 클러스터로 관리하고 표시할 지도 객체
        averageCenter: true, // 클러스터에 포함된 마커들의 평균 위치를 클러스터 마커 위치로 설정
        minLevel: 10, // 클러스터 할 최소 지도 레벨
        disableClickZoom: true // 클러스터 마커를 클릭했을 때 지도가 확대되지 않도록 설정한다
    });

    // 데이터를 가져오기 위해 jQuery를 사용합니다
    // 데이터를 가져와 마커를 생성하고 클러스터러 객체에 넘겨줍니다
    //json 을 넘겨줄 url매핑  (통신)
    // json받을 url매핑[요청]
    $.get("/room/chicken.json", function(data) {
//    alert(data);//제이슨 객체  경도 위도 다있을듯
        // 데이터에서 좌표 값을 가지고 마커를 표시합니다
        // 마커 클러스터러로 관리할 마커 객체는 생성할 때 지도 객체를 설정하지 않습니다
        var markers = $(data.positions).map(function(i, position) {

        var marker =  new kakao.maps.Marker({
                position : new kakao.maps.LatLng(position.lat, position.lng)
            });
            // 마커에 클릭 이벤트를 등록한다 (우클릭 : rightclick)
                  kakao.maps.event.addListener(marker, 'click', function() {
                            // alert('마커를 클릭했습니다!'); 클릭했을때 떠서 확인하는용도
                  //사이드바 열기<js에서 버튼 클릭 하기>
                  $("#sidebartoggle").trigger('click');
                   //사이드바 내용물에 html넣기
                    $.ajax({
                        url:'/room/getroom',
                        data:{"rnum" : position.rnum},
                         success: function(data) {
                             $("#contents").html(data);
                              }
                    });
                /* $("#contents").html("하하하하");*/
                /*// 커스텀 오버레이를 생성하고 지도에 표시한다
                var customOverlay = new kakao.maps.CustomOverlay({
                    map: map,
                    content: "<div style='padding:0 5px;background:#fff;'>방등록번호 :"+position.rnum+"></div>",
                    position: new kakao.maps.LatLng(position.lat, position.lng), // 커스텀 오버레이를 표시할 좌표
                    xAnchor: 0.5, // 컨텐츠의 x 위치
                    yAnchor: 0 // 컨텐츠의 y 위치
                });*/





                  });

            return marker;
        });

        // 클러스터러에 마커들을 추가합니다
        clusterer.addMarkers(markers);
    });

    // 마커 클러스터러에 클릭이벤트를 등록합니다
    // 마커 클러스터러를 생성할 때 disableClickZoom을 true로 설정하지 않은 경우
    // 이벤트 헨들러로 cluster 객체가 넘어오지 않을 수도 있습니다
    kakao.maps.event.addListener(clusterer, 'clusterclick', function(cluster) {

        // 현재 지도 레벨에서 1레벨 확대한 레벨
        var level = map.getLevel()-1;

        // 지도를 클릭된 클러스터의 마커의 위치를 기준으로 확대합니다
        map.setLevel(level, {anchor: cluster.getCenter()});
    });

    //문의 버튼 클릭 이벤트
    function notewrite(rnum){

       var ncontents= $("#ncontents").val();
        $.ajax({
            url:"/room/notewrite",
            data:{"rnum":rnum,"ncontents":ncontents},
            success:function(data){
            alert(data);
           if(data==1){
           alert("정상적으로 문의가 되었습니다")
           $("#ncontents").val("");//내용물 초기화
           $("#modallogin").modal("hide");//모다종류
           }else if(data==2){
           alert("로그인후 문의가 가능합니다")
           $("#ncontents").val("");//내용물 초기화
           $("#modallogin").modal("hide");//모달종류
           $("#modallogin").modal("show");//모달실행
           }

            }
        });
    }
