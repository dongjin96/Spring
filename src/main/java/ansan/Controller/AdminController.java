package ansan.Controller;

import ansan.Domain.Entity.Room.RoomEntity;
import ansan.Service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
@Controller
@RequestMapping(value="/admin")

public class AdminController {
    @Autowired
    private RoomService roomService;
    @GetMapping("/roomlist")
    public String roomlist(Model model){
        List<RoomEntity> roomEntities =roomService.getroomlist();
        model.addAttribute("roomEntities",roomEntities);
        return "admin/roomlist";
    }
    //삭제
    @GetMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam("rnum")int rnum){
        roomService.delete (rnum);
        return "1";
    }
    // 방번호를 이용한 방 상태 변경
    @GetMapping("/activeupdate")
    @ResponseBody
    public String activeupdate( @RequestParam("rnum") int rnum ,
                                @RequestParam("upactive") String upactive) {
        boolean result =
                roomService.activeupdate( rnum , upactive );
        if( result ){ return "1"; }
        else{ return  "2"; }
    }
    //방수정
    @GetMapping("/update")
    @ResponseBody
    public String update(@RequestParam("rnum")int rnum,
                         @RequestParam("field")String field,
                         @RequestParam("newcontents")String newcontents
                         ){
        roomService.update(rnum,field,newcontents);
        return "1";
    }


}
