package ansan.Controller;

import ansan.Domain.Dto.BoardDto;
import ansan.Domain.Dto.MemberDto;
import ansan.Domain.Entity.board.BoardEntity;
import ansan.Service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ContentDisposition;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.DocFlavor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Controller
public class BoardController {

    @Autowired
    BoardService boardService;  // boardService 메소드 호출용 객체
    @Autowired
    HttpServletResponse response; //응답객체
    @Autowired  //빈 생성 : 자동 메모리 할당
    HttpServletRequest request;

    // 게시물 전체 목록 페이지 이동
    @GetMapping("/board/boardlist")
    public String boardlist(Model model, @PageableDefault Pageable pageable) {

        /*검색 서비스*/
        String keyword = request.getParameter("keyword");
        String search = request.getParameter("search");
        //세션값 부여
        HttpSession session = request.getSession();

        if (keyword != null || search != null) {
            session.setAttribute("keyword", keyword);
            session.setAttribute("search", search);

        } else {
            keyword = (String) session.getAttribute("keyword");
            search = (String) session.getAttribute("search");
        }
        /*페이징처리 서비스*/
//        ArrayList<BoardDto> boardDtos = boardService.boardlist(pageable);
        Page<BoardEntity> boardDtos = boardService.boardlist(pageable, keyword, search);
        model.addAttribute("BoardDtos", boardDtos);

        System.out.println(boardDtos.getNumber());
        return "board/boardlist";  // 타임리프 를 통한 html 반환
    }


    // 게시물 쓰기 페이지로 이동
    @GetMapping("/board/boardwrite")
    public String boardwrite() {
        return "board/boardwrite"; // 리턴은 타임 리프 와 url 를 반환해
    }

    //게시물 쓰기 처리
    @PostMapping("/board/boardwritecontroller")
    @ResponseBody // 값을 넘겨줄떄는 제발 이거써요~!~!!~!controller에 값을띄 우고싶으면
    public String boardwritecontroller(@RequestParam("b_img") MultipartFile file) throws IOException {

        String uuidfile = null;
        if (!file.getOriginalFilename().equals("")) { /// 이미ㅣ지 않넣고도 상세보기가능하게만드는거// 첨부파일이있을때
            //파일 이름 중복배제제
            UUID uuid = UUID.randomUUID();// 고유 식별자 객체 난수 생성 메소드 호출
            //만약에 파일명에 _ 가존재한다면-로변경
            String OriginalName = file.getOriginalFilename();
            uuidfile = uuid.toString() + "_" + OriginalName.replace("_", "-");
            //파일처리[JSP(COS라이브러리)--->SPRING (MultipartFile클래스)]
            // String dir = request.getSession().getServletContext().getRealPath("/");
            String dir = "C:\\Users\\505\\Desktop\\Spring\\Spring\\src\\main\\resources\\static\\upload";
            String filepath = dir + "\\" + uuidfile; // 저장경로 +form에서 첨부한 파일 이름 호출
            //file.getOriginalFilename() : form 첨부파일 호출
            file.transferTo(new File(filepath));//transferTO ㅣ 파일 저장 [예외처리해야해오!@]
            System.out.println(filepath);
        } else { // 첨부파일이 없을떄
            uuidfile = null;
        }
        // 무조건 예 외 처리 해야한다
        //transferTo :파일 저장 [스트림 없이]
        //제목 과 내용도 호출
        String b_title;
        String b_content = request.getParameter("b_contents");
        b_title = request.getParameter("b_title");
        b_content = request.getParameter("b_contents");

        BoardDto boardDto = new BoardDto();
        boardDto.setB_title(b_title);
        boardDto.setB_contents(b_content);
        boardDto.setB_img(uuidfile);// form에서 첨부한 파일이름호출

        System.out.println(boardDto.toString());
        //세션
        HttpSession session = request.getSession();
        //세션 호출
        MemberDto memberDto = (MemberDto) session.getAttribute("logindto");
        boardDto.setB_write(memberDto.getM_id());
        boardService.boardwrite(boardDto);

        return "1"; // 글쓰기 성공시 게시판 목록이동

    }


    //첨부파일 다운로드 처리
    @GetMapping("/board/filedownload")
    public void filedownload(@RequestParam("b_img") String b_img, HttpServletResponse response) {

        String path = "C:\\Users\\505\\Desktop\\Spring\\Spring\\src\\main\\resources\\static\\upload\\" + b_img;
        //객체화
        File file = new File(path);
        //다운로드
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode( b_img.split("_")[1],"UTF-8"));
                                                                              //영문x한글[URLEncoder.encode(파일명,"UTF-8")]
            //파일 객체 내보내기
            OutputStream outputStream = response.getOutputStream();
            FileInputStream filelnputStream = new FileInputStream(path);

            int read = 0;
            byte[] buffer = new byte[1024 * 1024];//읽어올 바이트을 저장할 배열
            while ((read = filelnputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
        } catch (Exception e) {
            System.out.println("download error" + e);
        }

    }

    // 게시물 보기 페이지 이동
    @GetMapping("/board/boardview/{b_num}") // GET 방식으로 URL 매핑[연결]
    public String boardview(@PathVariable("b_num") int b_num, Model model) {

        BoardDto boardDto = boardService.getboard(b_num);

        // 첨부파일 존재하면
        if (boardDto.getB_img() != null) boardDto.setB_realimg(boardDto.getB_img().split("_")[1]);
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
    // 수정페이지 이동
    @GetMapping("/board/boardupdate/{b_num}")
    public String boardupdate(@PathVariable("b_num") int b_num, Model model) {

        BoardDto boardDto = boardService.getboard(b_num);

        // 첨부파일 존재하면 uuid가 제거된 파일명 변환해서 b_realimg 담기
        if (boardDto.getB_img() != null) boardDto.setB_realimg(boardDto.getB_img().split("_")[1]);

        model.addAttribute("boardDto", boardDto);

        return "board/boardupdate"; // html 열기
    }

    //수정 처리
    @PostMapping("/board/boardcontroller")
    public String boardcontroller(@RequestParam("b_newimg") MultipartFile file
            , @RequestParam("b_num") int b_num
            , @RequestParam("b_title") String b_title
            , @RequestParam("b_contents") String b_contents
            , @RequestParam("b_img") String b_img) {


        if (!file.getOriginalFilename().equals("")) {
            try {
                UUID uuid = UUID.randomUUID();
                String OriginalName = file.getOriginalFilename();
                String uuidfile = uuid.toString() + "_" + OriginalName.replace("_", "-");
                String dir = "C:\\Users\\505\\Desktop\\Spring\\Spring\\src\\main\\resources\\static\\upload";
                String filepath = dir + "\\" + uuidfile;
                file.transferTo(new File(filepath));
                boardService.update(
                        BoardDto.builder().b_num(b_num).b_title(b_title).b_contents(b_contents).b_img(uuidfile).build());

            } catch (Exception e) {
            }
        } else {
            boardService.update(
                    BoardDto.builder().b_num(b_num).b_title(b_title).b_contents(b_contents).b_img(b_img).build());

        }
        return "redirect:/board/boardview/" + b_num;
    }



}
