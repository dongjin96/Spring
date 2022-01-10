package Day03.Domain.Entity.Member;

import Day03.Domain.Entity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
@Setter
@Entity//DB내 테이블과 연결
@Table(name="member")//테이블 속성
@Builder//
@NoArgsConstructor
@AllArgsConstructor
@Getter

public class MemberEntity extends BaseTimeEntity {
   @Id//기본키 PK
   @GeneratedValue(strategy = GenerationType.IDENTITY)//autokey
   private int m_num; //회원번호
    @Column
    private String m_id;
    @Column
    private String m_password;
    @Column
    private String m_name;
    @Column
    private String m_sex;
    @Column
    private String m_phone;
    @Column
    private String m_email;
    @Column
    private  String m_address;
    @Column
    private  int m_point;
    @Column
    private  String m_grade;

}
