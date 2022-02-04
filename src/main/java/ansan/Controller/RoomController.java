package ansan.Controller;

import ansan.Domain.Entity.Room.RoomEntity;
import ansan.Service.RoomService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller //view<-- --> Conroller[매핑]
@RequestMapping("/room")  // 중복 url
public class RoomController {
    @Autowired
     private  RoomService roomService;

    @GetMapping("/write")// 이동
    public String write() {
        return "room/roomwrite";//타임리프 반환[앞에/제거
    }//

    @PostMapping("/writecontroller")//처리
    public String writecontroller(RoomEntity roomEntity
                                , @RequestParam("file")List<MultipartFile>files
                                , @RequestParam("addressy") Double addressy
                                , @RequestParam("addressx") Double addressx){
        roomEntity.setRaddress( roomEntity.getRaddress()+"," + addressy + "," + addressx  );
         System.out.println(roomEntity.toString());
        roomService.write(roomEntity,files);
        return "main";
        }
     // 룸 보기 페이지 이동
    @GetMapping("/roomview")//이동
    public String roomview(){
        return "room/roomview";}//타임리프


    //json 반환[지도에 띄우고자 하는 방 응답하기]
    @GetMapping("/chicken.json")
    @ResponseBody
    public JSONObject chicken(){
    // MAP<---> JSON [키: 값]=> 엔트리
        //중첩이 가능하다
        //"positions": [{"lat": 37.27943075229118,"lng": 127.01763998406159},
        // 제이슨오브젝트=={키 : 리스트{"키":값1,"키":값2,"키":값3}}
            //map={키: 값}
        // map객체={"키":List[map객체,map객체,map객체]}

    JSONObject jsonObject = new JSONObject(); //제이슨전체
    JSONArray jsonArray = new JSONArray();//제이슨 안에들어가는 리스트

    List<RoomEntity>roomlist=roomService.getroomlist();//모든방[위도,경도 포함]
    for(RoomEntity roomEntity : roomlist){
        JSONObject data = new JSONObject();// 리스트 안에 들어가는 키'값
        data.put("lat",roomEntity.getRaddress().split(",")[1]);//주소[0],위도[1],경도[2]
        data.put("lng",roomEntity.getRaddress().split(",")[2]);
        data.put("rnum",roomEntity.getRnum());
        //data.put("lng",roomEntity.getRoomimgEntities().get(0).getRimg());
                            //룸엔티티-> 룸이미지리스트-> 룸이미지엔티티->룸이미지엔티티 이미지한개 입니다~!

        jsonArray.add(data);

    }
        jsonObject.put("positions",jsonArray );
        return jsonObject;
    }

    //방번호를 이용한 방정보 html
    @GetMapping("/getroom")
    /*@RequestBody 일이나 이로 반환 */
    public String getroom(@RequestParam("rnum")int rnum, Model model){
        model.addAttribute("room",roomService.getroom(rnum));

        return "room/room"; //room html 반환
    }

    // 방번호를 이용한 방상태 변경
    @GetMapping("activeupdate")
    @ResponseBody
    public String activeupdate(@RequestParam("rnum")int rnum,
                               @RequestParam("upactive")String upactive){
       boolean result = roomService.activeupdate(rnum,upactive);
       if(result){return "1";}
       else{return"2";}
    }

    //문의 등록
    @GetMapping("/notewrite")
    @ResponseBody
    public String notewrite(@RequestParam("rnum")int rnum,
                            @RequestParam("ncontents")String ncontents){
       boolean result = roomService.notewrite(rnum,ncontents);
       if(result){return "1";}else{return"2";}

    }

    //읽음 처리 업데이트
    @GetMapping("/nreadupdate")
    @ResponseBody
    public void nreadupdate(@RequestParam("nnum")int nnum){
        roomService.nreadupdate(nnum);
    }

    }

