package ansan.Controller;

import ansan.Domain.Dto.BoardDto;
import ansan.Domain.Dto.MemberDto;
import ansan.Service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@Controller
public class BoardController {

    @Autowired
    BoardService boardService;  // boardService 메소드 호출용 객체

    // http url 연결 게시물 전체 목록 페이지 이동
    @GetMapping("/board/boardlist")
    public String boardlist(Model model) {

        ArrayList<BoardDto> boardDtos = boardService.boardlist();

        model.addAttribute("BoardDtos", boardDtos);

        return "board/boardlist";  // 타임리프 를 통한 html 반환
    }

    @Autowired  //빈 생성 : 자동 메모리 할당
    HttpServletRequest request;

    // 게시물 쓰기 페이지로 이동
    @GetMapping("/board/boardwrite")
    public String boardwrite() {
        return "board/boardwrite";
    }

    //게시물 쓰기 처리
    @PostMapping("/board/boardwritecontroller")
    public String boardwritecontroller(@RequestParam("b_img")MultipartFile file) throws IOException {
        //파일처리[JSP(COS라이브러리)--->SPRING (MultipartFile클래스)]
        String dir = "C:\\Users\\505\\Desktop\\Spring\\Spring\\src\\main\\resources\\static\\upload";
        String filepath = dir +"\\"+file.getOriginalFilename(); // 저장경로 +form에서 첨부한 파일 이름 호출
        //file.getOriginalFilename() : form 첨부파일 호출
        file.transferTo(new File(filepath));//transferTO ㅣ 파일 저장 [예외처리해야해오!@]
        // 무조건 예 외 처리 해야한다
        //transferTo :파일 저장 [스트림 없이]
        //제목 과 내용도 호출
        String b_title;
        String b_content = request.getParameter("b_contents");
            b_title= request.getParameter("b_title");
            b_content=request.getParameter("b_contents");

        BoardDto boardDto = new BoardDto();
        boardDto.setB_title(b_title);
        boardDto.setB_contents(b_content);
        boardDto.setB_img(file.getOriginalFilename());// form에서 첨부한 파일이름호출

        System.out.println(boardDto.toString());
        //세션
        HttpSession session = request.getSession();
        //세션 호출
        MemberDto memberDto = (MemberDto) session.getAttribute("logindto");
        boardDto.setB_write(memberDto.getM_id());
        boardService.boardwrite(boardDto);
        return "redirect:/board/boardlist"; // 글쓰기 성공시 게시판 목록이동
    }

    // 게시물 보기 페이지 이동
    @GetMapping("/board/boardview/{b_num}") // GET 방식으로 URL 매핑[연결]
    public String boardview(@PathVariable("b_num") int b_num, Model model) {

        BoardDto boardDto = boardService.getboard(b_num);

        model.addAttribute("boardDto", boardDto);
        return "board/boardview";
    }

    //게시물 삭제 처리
    @GetMapping("/board/boarddelete")
    @ResponseBody
    public int boarddelete(@RequestParam("b_num") int b_num) {
        boolean result = boardService.delete(b_num);
        if (result) {
            return 1;
        } else {
            return 2;
        }


    }
    // @GetMapping( "/board/boardupdate/{변수}/{변수}/{변수}" )
    @GetMapping("/board/boardupdate/{b_num}")
    //수정페이지 이동
   public String boardupdate(@PathVariable("b_num")int b_num,Model model){
       BoardDto boardDto = boardService.getboard(b_num);
        model.addAttribute("boardDto",boardDto);
        return "board/boardupdate";
    }
    //수정 처리
    @PostMapping("/board/boardcontroller")
    public String boardcontroller(BoardDto boardDto){
        boolean result = boardService.update(boardDto);
        return "redirect:/board/boardview/"+boardDto.getB_num();
    }
}