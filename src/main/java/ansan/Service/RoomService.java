package ansan.Service;

import ansan.Domain.Dto.MemberDto;
import ansan.Domain.Entity.Member.MemberEntity;
import ansan.Domain.Entity.Member.MemberRepository;
import ansan.Domain.Entity.Room.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RoomService {
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private MemberService memberService;
    @Autowired
    private RoomimgRepository roomimgRepository;

    //저장
    public boolean write(RoomEntity roomEntity, List<MultipartFile> files) {
        //등록한 회원번호 넣기[세션에 로그인 정보]
        HttpSession session = request.getSession();
        MemberDto memberDto = (MemberDto) session.getAttribute("logindto");
        //회원번호-> 회원 엔티티 가져오기
        MemberEntity memberEntity =
                memberService.getmentitiy(memberDto.getM_num());
        //룸엔티티에 회원 엔티티 넣기
        roomEntity.setMemberEntity(memberEntity);
        // 방상태 : 첫 등록시 검토중으로 설정
        roomEntity.setRactive("검토중");
        //룸엔티티 저장후에 룸엔티팉 번호가져온다
        int rnum = roomRepository.save(roomEntity).getRnum();
        //회원 엔티티 룸리스트에 룸엔티티 추가
        RoomEntity roomEntitysaved = roomRepository.findById(rnum).get();
        memberEntity.getRoomEntities().add(roomEntitysaved);
        // 파일처리
        String uuidfile = null;
        if (files.size() != 0) {
            for (MultipartFile file : files) {
                UUID uuid = UUID.randomUUID();
                //식별 난수값+_+파일명 (만약에 파일명에 _ 가 있으면 - 변환)
                uuidfile = uuid.toString() + "_" + file.getOriginalFilename().replaceAll("_", "-");
                String dir = "C:\\Users\\505\\Desktop\\Spring\\Spring\\src\\main\\resources\\static\\roomimg";
                String filepath = dir + "\\" + uuidfile; //경로 +파일명

                try {
                    file.transferTo(new File(filepath)); // 해당 파일-> 해당경로로 파일 이동 [복사]
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //roomimg 엔티티 생성
                RoomimgEntity roomimgEntity = RoomimgEntity.builder().rimg(uuidfile).roomEntity(roomEntitysaved).build();
                roomimgRepository.save(roomimgEntity).getRimgnum();
                //room 엔티티내 roomimg 리스트dp roomimg엔티티 주입
                roomRepository.findById(rnum).get().getRoomimgEntities().add(roomimgEntity);

            }
        }


        return true;

    }

    //모든룸 가져오기
    public List<RoomEntity> getroomlist() {
        return roomRepository.findAll();
    }

    // 특정 룸 가져오기
    public RoomEntity getroom(int rnum) {
        return roomRepository.findById(rnum).get();
    }

    //틋정룸 삭제
    public boolean delete(int rnum) {
        roomRepository.delete(roomRepository.findById(rnum).get());
        return true;
    }

    // 특정 룸 상태변경
    @Transactional//업데이트시 필수
    public boolean activeupdate(int rnum, String upactive) {
        RoomEntity roomEntity = roomRepository.findById(rnum).get();
        if (roomEntity.getRactive().equals(upactive)) {
            return false;
        } else {
            roomEntity.setRactive(upactive);
            return true;
        }
    }

    // 방정보 수정
    @Transactional
    public boolean update(int rnum, String field, String newcontents) {
        RoomEntity roomEntity = roomRepository.findById(rnum).get();
        if (field.equals("rname")) {
            roomEntity.setRprice(newcontents);
        } else if (field.equals("rprice")) {
            roomEntity.setRprice(newcontents);
        }
        return true;
    }

    //문의 등록
    public boolean notewrite(int rnum, String ncontents) {
        //로그인된 회원정보를 가져온다
        HttpSession session = request.getSession();
        MemberDto memberDto = (MemberDto) session.getAttribute("logindto");
        //만약에 로그인이 안되어 있으면
        if (memberDto == null) {
            return false;
        }//등록 실패


        //문의 엔티티 생성
        NoteEntity noteEntity = new NoteEntity();
        noteEntity.setNcontents(ncontents);//작성내용
        noteEntity.setMemberEntity(memberService.getmentitiy(memberDto.getM_num()));//작성자 엔티티
        noteEntity.setRoomEntity(roomRepository.findById(rnum).get());//방엔티티

        //문의 엔티티 저장
        int nnumm = noteRepository.save(noteEntity).getNnum();
        //해당 룸엔티티의 문의 리스트에 문의 엔티티 저장
        roomRepository.findById(rnum).get().getNoteEntities().add(noteRepository.findById(nnumm).get());
        //해당회원 엔티티의 문의 리스트에 문의엔티티 저장
        memberService.getmentitiy(memberDto.getM_num()).getNoteEntities().add(noteRepository.findById(nnumm).get());
        return true;
    }


    @Autowired
    private MemberRepository memberRepository;

    //로그인된 회원이 방 출력
    public List<RoomEntity> getmyroomlist() {
        HttpSession session = request.getSession();
        MemberDto memberDto = (MemberDto) session.getAttribute("logindto");
        MemberEntity memberEntity = memberService.getmentitiy(memberDto.getM_num());
        return memberEntity.getRoomEntities();
    }

    //로그인 된 회원이 등록한 문의 출력
    public List<NoteEntity> getmynotelist() {
        HttpSession session = request.getSession();
        MemberDto memberDto = (MemberDto) session.getAttribute("logindto");
        MemberEntity memberEntity = memberService.getmentitiy(memberDto.getM_num());
        return memberEntity.getNoteEntities();
    }

    //답변등록
    @Transactional
    public boolean notereplywrite(int nnum, String nreply) {
        noteRepository.findById(nnum).get().setNreply(nreply);
        return true;
    }

    //nread" 0안읽음 1: 읽음
    public void nreadcount() {
        HttpSession session = request.getSession();
        MemberDto memberDto = (MemberDto) session.getAttribute("logindto");
        if (memberDto == null) return;//로그인이 안되어있으면 제외
        int nreadcount = 0;//안읽은 쪽지개수
        // 로그인된 회원번호와 쪽지받은사람의 회원번호가 모두 동일하면
        for( NoteEntity noteEntity :  noteRepository.findAll() ) {
            if (noteEntity.getRoomEntity().getMemberEntity().getM_num()
                    == memberDto.getM_num()
                    && noteEntity.getNread() == 0) {
                nreadcount++;
            }
            // 문의엔티티.방엔티티.멤버엔티티.멤버번호
        }
        //세션에 자징하기
        session.setAttribute("nreadcount", nreadcount);
    }
        // 읽음 처리 서비스
        @Transactional // 업데이트처리
        public boolean nreadupdate( int nnum ){
            noteRepository.findById( nnum).get().setNread(1);
            return true;
        }
}

