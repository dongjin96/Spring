package ansan.Service;

import ansan.Domain.Dto.MemberDto;
import ansan.Domain.Entity.Member.MemberEntity;
import ansan.Domain.Entity.Member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class MemberService {
    @Autowired
    MemberRepository memberRepository ;

    // 회원등록 메소드
    public boolean membersignup( MemberDto memberDto ){
        memberRepository.save( memberDto.toentity()  );  // save(entity) : insert / update :  Entity를 DB에 저장
        return true;
    }

    //회원 출력 메소드
    public boolean getmember() {

        List<MemberEntity> memberEntityList = memberRepository.findAll();//모든엔티티 조회[p.81]
        for (MemberEntity memberEntity : memberEntityList) {
        }
        return true;
    }

    // 회원 로그인 메소드
    public MemberDto login(MemberDto memberDto) {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        for (MemberEntity memberEntity : memberEntityList) {
            if (memberEntity.getM_id().equals(memberDto.getM_id()) &&
                    memberEntity.getM_password().equals(memberDto.getM_password())) {
                return MemberDto.builder()
                        .m_id(memberEntity.getM_id())
                        .m_num(memberEntity.getM_num()).build();
            }
        }
        return null;
    }

    //회원 아이디 찾기
    public String findid(MemberDto memberDto) {
        //1. 모든 엔티티 호출
        List<MemberEntity> memberEntities = memberRepository.findAll();
        //2. 반복문 이용한 모든 엔티티를 하나씩 꺼내보기
        for (MemberEntity memberEntity : memberEntities){
            //3. 만약에 해당 엔티티 가 이름과 이메일이 동일하면
            if (memberEntity.getM_name().equals(memberDto.getM_name()) &&
                    memberEntity.getM_email().equals(memberDto.getM_email())) {
                // 아이디가 반환된다
                return memberEntity.getM_id();
            }

            }
        return null;
    }
    @Autowired
    private JavaMailSender javaMailSender;//자바 메일 객체
    //회원 비밀번호 찾기-> 메일전송[임시비밀번호]
    @Transactional
    public boolean findpassword(MemberDto memberDto) {
        //1. 모든 엔티티 호출
        List<MemberEntity> memberEntities = memberRepository.findAll();
        for (MemberEntity memberEntity : memberEntities) {
            if (memberEntity.getM_id().equals(memberDto.getM_id()) && memberEntity.getM_email().equals(memberEntity.getM_email())) {
                StringBuilder body = new StringBuilder();   // StringBuilder  : 문자열 연결 클래스  [ 문자열1+문자열2 ]
                body.append("<html> <body><h1> Ansan 계정 임시 비밀번호 </h1>");

                Random random = new Random();
                //임시 비밀번호 만들기
                StringBuilder temppassword = new StringBuilder();
                for (int i = 0; i < 12; i++) {//12자리 만들기
                    //랜덤숫자-->문자면환[문자마다]
                    temppassword.append((char) ((int) (random.nextInt(26)) + 97));
                }

                body.append("<div>" + temppassword + "</div></html>"); // 보내는 메시지에 임시비밀번호를 html 에 추가
                //엔티티내 패스워드 변경경
                memberEntity.setM_password(temppassword.toString());
                try {
                    MimeMessage message = javaMailSender.createMimeMessage();
                    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "utf-8");

                    mimeMessageHelper.setFrom("dhehdwls44@naver.com", "Ansan");
                    mimeMessageHelper.setTo(memberDto.getM_email());
                    mimeMessageHelper.setSubject("Ansan 계정 임시 비밀번호 발송 ");
                    mimeMessageHelper.setText(body.toString(), true);
                    javaMailSender.send(message);
                } catch (Exception e) {
                    System.out.println("메일전송 실패 " + e);
                }
                ;
                return true;

            }
        }
        return false;
    }
    //회원 아이디 중복체크
    public boolean idcheck(String m_id){
        //1. 모든 엔티티 가져오기
        List<MemberEntity> memberEntities = memberRepository.findAll();
        //2. 모든 엔티티 반복문 돌려서 엔티티 하나씩 가져오기
        for(MemberEntity memberEntity :memberEntities){
            //3. 해당 엔티티가 입력한 아이디와 동일하면
            if(memberEntity.getM_id().equals(m_id)){
                return true;//중복
            }
        }
        return false;//중복없음
    }


    // 이메일 중복체크
    public boolean emailcheck( String email ){
        // 1. 모든 엔티티 가져오기
        List<MemberEntity> memberEntities =  memberRepository.findAll();
        // 2. 모든 엔티티 반복문 돌려서 엔티티 하나씩 가쟈오기
        for( MemberEntity memberEntity : memberEntities ) {
            // 3. 해당 엔티티가 입력한 아이디와 동일하면
            if (memberEntity.getM_email().equals(email)) {
                return true; // 중복
            }
        }
        return false; // 중복 없음
    }
    //회원번호-> 회원정보 반환
    public MemberDto getmemberDto(int m_num) {
        //memberRepository.findALL() 모든 엔티티 호출
        //memberRepository.findById(pk값); 해당 pk값의 엔티티 호출
       Optional<MemberEntity>entityoptional= memberRepository.findById(m_num);
       //찾은 엔티티를 dto 변경후 반환[패스워드 , 수정 날짜 제외]
        return MemberDto.builder()
                .m_id(entityoptional.get().getM_id())
                .m_name(entityoptional.get().getM_name())
                .m_address(entityoptional.get().getM_address())
                .m_email(entityoptional.get().getM_email())
                .m_grade(entityoptional.get().getM_grade())
                .m_phone(entityoptional.get().getM_phone())
                .m_point(entityoptional.get().getM_point())
                .m_sex(entityoptional.get().getM_sex())
                .m_createdDate(entityoptional.get().getCreatedDate()).build();
    }
    @Transactional
    public boolean delete(int m_num, String passwordconfirm) {
        //1. 로그인된 회원번호의 엔티티 [레코드 호출]
        Optional<MemberEntity>memberEntity = memberRepository.findById(m_num);
        //optional 클래스 :null
        if(memberEntity.get().getM_password().equals(passwordconfirm)){
            //l 내 객체 호출
            memberRepository.delete(memberEntity.get());//jpa인터페이스를 이용한 엔티티db레코드 삭제제
           return true; //회원탈퇴
        }
        return false; //회원탈퇴X
    }

}

