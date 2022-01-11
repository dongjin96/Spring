package ansan.Service;


import ansan.Domain.Dto.BoardDto;
import ansan.Domain.Entity.board.BoardEntity;
import ansan.Domain.Entity.board.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // 모든 글출력 메소드
    public ArrayList<BoardDto> boardlist() {
        // 게시물 번호를 정렬해서 엔티티 호출하기
        // SQL : Select * from board order by 필드명 DESC
        // JPA : boardRepository.findAll( Sort.by( Sort.Direction.DESC , "entity 필드명" ) );
        List<BoardEntity> boardEntities = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "createdDate")); // 모든 엔티티 호출

        ArrayList<BoardDto> boardDtos = new ArrayList<>(); // 모든 dto 담을 리스트 선언
        for (BoardEntity boardEntity : boardEntities) { // 모든 엔티티를 반복하면서 하나씩 꺼내오기
            // 엔티티 -> dto 변환
            // 날짜 쪼개는거 형변환 <--dto 에서 String으로 바꾼다
            ///날짜 형변환[!localdate-->Sting]
            ///LocalDateTime.format() :Local Date Time
            String date = boardEntity.getCreatedDate().format(DateTimeFormatter.ofPattern("yy-MM-dd"));
            ///오늘날짜
            String nowdate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy-MM-dd"));
            /// 만약에 게시물 작성일이 오늘이면 시간출력 오늘이아니면 날짜를 출력
            if (date.equals(nowdate)) {
                date = boardEntity.getCreatedDate().format(DateTimeFormatter.ofPattern("hh:mm:ss"));
            }

            BoardDto boardDto = new BoardDto(
                    boardEntity.getB_num(),
                    boardEntity.getB_title(),
                    boardEntity.getB_contents(),
                    boardEntity.getB_write(),
                    date,
                    boardEntity.getB_view(),
                    boardEntity.getB_img());
            boardDtos.add(boardDto); //  리스트에 저장
        }
        return boardDtos;
    }

    ;

    //게시물 view 출력
    public BoardDto getboard(int b_num) {
        //findByID("pk값"): 해당 pk의 엔티티를 호출한다=>
        Optional<BoardEntity> entityoptional = boardRepository.findById(b_num);
        String date = entityoptional.get().getCreatedDate().format(DateTimeFormatter.ofPattern("yy-MM-dd"));

        return BoardDto.builder()
                .b_num(entityoptional.get().getB_num())
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
           return true;
       }catch (Exception e) {
           System.out.println( e );
           return false;
       }
    }

}


