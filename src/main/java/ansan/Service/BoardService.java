package ansan.Service;


import ansan.Domain.Dto.BoardDto;
import ansan.Domain.Entity.board.BoardEntity;
import ansan.Domain.Entity.board.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service    // 필수!!!!!!!
public class BoardService {

    @Autowired
    BoardRepository boardRepository;

    // 글쓰기 메소드
    public void boardwrite(BoardDto boardDto) {
        boardRepository.save(boardDto.toentity());
    }

//[모든 글 출력 메소드 [페이징 처리한거]]
    public Page<BoardEntity> boardlist (Pageable pageable, String keyword,String search){
        //페이지 번호
        int page =0;
        if(pageable.getPageNumber()==0) page=0; //0이면 0페이지
        else page = pageable.getPageNumber()-1; //1이면-1 1페이지2이면-12페이지

        //페이지속성[PAGEREQUEST(페이지번호,페이당 게시물수,정렬기준)]
        pageable= PageRequest.of( page,5 ,Sort.by( Sort.Direction.DESC , "bnum"));

        //만약에 검색이 있을경우
        if(keyword != null && keyword.equals("b_title"))return boardRepository.findAlltitle(search,pageable);
//        if(keyword != null&&keyword.equals("b_contents"))return boardRepository.findAlltitle(search,pageable);
//        if(keyword != null&&keyword.equals("b_writer"))return boardRepository.findAlltitle(search,pageable);

    return boardRepository.findAll(pageable);

    }



//    // 모든 글출력 메소드[페이징 처리 x]
//    public ArrayList<BoardDto> boardlist() {
//        // 게시물 번호를 정렬해서 엔티티 호출하기
//        // SQL : Select * from board order by 필드명 DESC
//        // JPA : boardRepository.findAll( Sort.by( Sort.Direction.DESC , "entity 필드명" ) );
//        List<BoardEntity> boardEntities = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "createdDate")); // 모든 엔티티 호출
//
//        ArrayList<BoardDto> boardDtos = new ArrayList<>(); // 모든 dto 담을 리스트 선언
//        for (BoardEntity boardEntity : boardEntities) { // 모든 엔티티를 반복하면서 하나씩 꺼내오기
//            // 엔티티 -> dto 변환
//            // 날짜 쪼개는거 형변환 <--dto 에서 String으로 바꾼다
//            ///날짜 형변환[!localdate-->Sting]
//            ///LocalDateTime.format() :Local Date Time
//            String date = boardEntity.getCreatedDate().format(DateTimeFormatter.ofPattern("yy-MM-dd"));
//            ///오늘날짜
//            String nowdate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy-MM-dd"));
//            /// 만약에 게시물 작성일이 오늘이면 시간출력 오늘이아니면 날짜를 출력
//            if (date.equals(nowdate)) {
//                date = boardEntity.getCreatedDate().format(DateTimeFormatter.ofPattern("hh:mm:ss"));
//            }
//
//            BoardDto boardDto = new BoardDto(
//                    boardEntity.getB_num(),
//                    boardEntity.getB_title(),
//                    boardEntity.getB_contents(),
//                    boardEntity.getB_write(),
//                    date,
//                    boardEntity.getB_view(),
//                    boardEntity.getB_img(),null);
//            boardDtos.add(boardDto); //  리스트에 저장
//        }
//        return boardDtos;
//    }

    ;
    @Autowired
    HttpServletRequest request;
    //게시물 view 출력
    @Transactional
    public BoardDto getboard(int b_num) {
        //findByID("pk값"): 해당 pk의 엔티티를 호출한다=>
        Optional<BoardEntity> entityoptional = boardRepository.findById(b_num);
        String date = entityoptional.get().getCreatedDate().format(DateTimeFormatter.ofPattern("yy-MM-dd"));

        //[세션 이용한 ]조회수 중복 방지
        HttpSession session = request.getSession();
        if(session.getAttribute(b_num+"")==null) {//문자열임
            //조회수 변경
            entityoptional.get().setB_view(entityoptional.get().getB_view() + 1);
            //세션부여
            session.setAttribute(b_num+"",1);
            //해당 세션 시간  [1초]
            session.setMaxInactiveInterval(60*60*24); //60 1분  (60*60*24)= 24시간
        }
        return BoardDto.builder()
                .b_num(entityoptional.get().getBnum())
                .b_title(entityoptional.get().getB_title())
                .b_contents(entityoptional.get().getB_contents())
                .b_write(entityoptional.get().getB_write())
                .b_view(entityoptional.get().getB_view())
                .b_img(entityoptional.get().getB_img())
                .b_createdDate(date)



                .build();
    }

    public boolean delete(int b_num) {
        Optional<BoardEntity> entityOptional = boardRepository.findById(b_num);
        if (entityOptional.get() != null) {
            boardRepository.delete(entityOptional.get());
            return true;
        } else {
            return false;
        }

    }
    //게시물 수정 처리
    @Transactional//수정중 오류 발생시 rollback rollback: 취소
    public boolean update(BoardDto boardDto){
       try {
           //수정할 엔티티를 찾는다
           Optional<BoardEntity> entityOptional = boardRepository.findById(boardDto.getB_num());
           //엔티티를 수정한다[엔티티 변화->DB변경처리]
           entityOptional.get().setB_title( boardDto.getB_title());
           entityOptional.get().setB_contents( boardDto.getB_contents());
           entityOptional.get().setB_img(boardDto.getB_img());
           return true;
       }catch (Exception e) {
           System.out.println( e );
           return false;
       }
    }

}


