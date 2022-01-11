package ansan.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class indexController {
  //메인페이지매핑
    @GetMapping("/")
    public String main(){
        return "main";
    }
}
