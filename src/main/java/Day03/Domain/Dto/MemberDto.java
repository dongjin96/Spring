package Day03.Domain.Dto;

import Day03.Domain.Entity.Member.MemberEntity;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter@Setter
@ToString

@Builder
public class MemberDto {
    //필드
    private int m_num;   // 회원번호
    private String m_id;    // 회원아이디
    private String m_password; // 회원비밀번호
    private String m_name; // 회원이름
    private String m_sex; // 회원성별
    private String m_phone; // 회원연락처
    private String m_email; // 회원이메일
    private String m_address; // 회원주소
    private int m_point; // 회원포인트
    private String m_grade; // 회원등급
    private LocalDateTime m_createdDate;

    //DTO->entitiy
   public MemberEntity toentity(){
       return MemberEntity.builder()
               .m_id(this.m_id)
               .m_password(this.m_password)
               .m_name(this.m_name)
               .m_sex(this.m_sex)
               .m_phone(this.m_phone)
               .m_email(this.m_email)
               .m_address(this.m_address)
               .m_point(this.m_point)
               .m_grade(this.getM_grade()).build();
   }

}





    /*public MemberEntity toentity(){
        MemberEntity MemberEntity = new MemberEntity();
        MemberEntity.setM_id(this.m_id);
        MemberEntity.setM_password(this.m_password);
        MemberEntity.setM_name(this.m_name);
        MemberEntity.setM_sex(this.m_sex);
        MemberEntity.setM_phone(this.m_phone);
        MemberEntity.setM_email(this.m_email);
        MemberEntity.setM_address(this.m_address);
        return MemberEntity;
    }*/


