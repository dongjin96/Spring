package ansan.Controller;


import ansan.Service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class indexController {
    //메인페이지매핑
    @GetMapping("/")
    public String main() {
        return "main";
    }

    //안익은 쪽지의 개수세기
    @Autowired
    private RoomService roomService;
    @GetMapping("/nreadcount")
    @ResponseBody
    public void nreadcount() { roomService.nreadcount();
    }


}