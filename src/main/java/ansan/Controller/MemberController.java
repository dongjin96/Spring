package ansan.Controller;


import ansan.Domain.Dto.MemberDto;
import ansan.Service.MemberService;
import ansan.Service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class MemberController {
    @Autowired
    MemberService memberService;
    @Autowired
    HttpServletRequest request; //요청객체 [JSP :내장객체(request)와동일하다]
    @GetMapping("/member/login") //로그인페이지 연결
    public String login() {
        return "member/login";
    }

    @GetMapping("/member/signup")//회원가입페이지 연결
    public String signup() {

        memberService.getmember();
        return "member/signup";
    }


    // 회원가입 처리 연결
    @PostMapping("/member/signupcontroller")
    public String signupcontroller(MemberDto memberDto ,
                                   @RequestParam("address1") String address1 ,
                                   @RequestParam("address2") String address2 ,
                                   @RequestParam("address3") String address3 ,
                                   @RequestParam("address4") String address4 ){


        memberDto.setM_address( address1+"/"+address2+"/"+address3+"/"+address4 );


        // 자동주입 : form 입력한 name 과 dto의 필드명 동일하면 자동주입 // 입력이 없는 필드는 초기값[ 문자=null , 숫자 = 0 ]
        memberService.membersignup(memberDto);
        return "redirect:/";  // 회원가입 성공시 메인페이지 연결
    }

/*    @PostMapping("/member/logincontroller") //사용안하는이유 [스프링 시큐리티 사용시 로그인 처리 메소드 제공받기 때문에 사용x]
    @ResponseBody
    public String logincontroller( @RequestBody MemberDto memberDto){
        // 폼 사용시에는 자동주입 O
        // AJAX 사용시에는 자동주입 X -> @RequestBody
        MemberDto loginDto =   memberService.login( memberDto );
        if( loginDto !=null ){
            HttpSession session = request.getSession();
            session.setAttribute("logindto",loginDto);
            System.out.println("Login success");
            return "1";
        }else{
            System.out.println("Login fail");
            return "2";
        }
        // 타임리프를 설치했을경우  RETRUN URL , HTML
        // html 혹은 url 아닌 값 반환할때  @ResponseBody
    }*/
//    @GetMapping("/member/logout")
//    public String logout(){
//        HttpSession session = request.getSession();
//        session.setAttribute( "logindto" , null);   // 기존 세션을 null 로 변경
//        return "redirect:/"; // 로그아웃 성공시 메인페이지로 이동
//    }
    //회원정보 찾기 페이지 연결
    @GetMapping("/member/findid")
    public String findid(){
        return "member/findid";
    }


    @PostMapping("/member/findidcontroller")
    public String findidcontroller(MemberDto memberDto, Model model){
        String result = memberService.findid(memberDto);
        if(result!=null) {
            String msg = "회원님의 아이디 :" + result;
            model.addAttribute("findidmsg",msg);
        }else {
            String msg="동일한 회원정보가 없습니다";
            model.addAttribute("findidmsg", msg);
        }
        return"member/findid";

    }
    @PostMapping("/member/findpasswordcontroller")
    public String findpassword(MemberDto memberDto, Model Model){
        boolean result = memberService.findpassword(memberDto);
        if(result){
            String msg ="해당 이메일로 임시 비밀번호 발송했습니다.";
            Model.addAttribute("findpwmsg",msg);
        }else{
            String msg ="동일한 회원정보가 없습니다";
            Model.addAttribute("findpwmsg",msg);
        }
        return"member/findid";

    }
    //아이디 중복체크
    @PostMapping("/member/idcheck")
    @ResponseBody
    public int idcheck(@RequestParam("m_id") String m_id){

        boolean result = memberService.idcheck(m_id);
        if(result) {
            return 1;
        }else{
            return 2;
        }
    }
    //이메일 중복체크
    @GetMapping("/member/emailcheck")
    @ResponseBody
    public String emailcheck(@RequestParam("m_email")String m_id){

        boolean result = memberService.emailcheck(m_id);
        if(result) {
            return "1";
        }else{
            return "2";
        }
    }
    //마이인포
    @GetMapping("/member/info")
    public String emailcheck(Model model){
        //로그인 세션호출
        HttpSession session = request.getSession();
        MemberDto loginDto
                = (MemberDto)session.getAttribute("logindto");
         //2. 세션에 회원번호 service에 전달해서 동일한 회원번호 에 회원정보 가져오가
        MemberDto memberDto = memberService.getmemberDto(loginDto.getM_num());
        //3. 찾은 회원정보를 model 인터페이스를 이용한 view 전달하기
        model.addAttribute("memberDto",memberDto);
        return "member/info";
        }

    // 회원삭제 처리
    @GetMapping("/member/mdelete")
    @ResponseBody
    public int mdelete(
            @RequestParam("passwordconfirm") String passwordconfirm ){

        // 1. 세션 호출
        HttpSession session = request.getSession();
        MemberDto memberDto = (MemberDto) session.getAttribute("logindto");
        // 2. service에 로그인된 회원번호 , 확인패스워드
        boolean result =  memberService.delete( memberDto.getM_num() , passwordconfirm );
        // 3. 결과 를 ajax에게 응답
        if( result ){ return 1;}
        else{return 2;}

    }
    //방 쪽지 확인 페이지
    @Autowired
    private RoomService roomService;
    @GetMapping("/member/notelist")
    public String notelist(Model model){
        model.addAttribute("rooms", roomService.getmyroomlist());
        model.addAttribute("notes",roomService.getmynotelist());
        return "member/notelist";
    }

    //답변변
    @Transactional
    public String notereplywrite(@RequestParam("nnum")int nnum,
                                 @RequestParam("nreply")String nreply){
        roomService.notereplywrite(nnum,nreply);
        return "1";
    }
}


