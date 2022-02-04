package ansan.Domain.Entity.Member;

import ansan.Domain.Entity.BaseTimeEntity;
import ansan.Domain.Entity.Room.NoteEntity;
import ansan.Domain.Entity.Room.RoomEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    private String mid;
    @Column
    private String m_password;
    @Column
    private String m_name;
    @Column
    private String m_sex;
    @Column
    private String m_phone;
    @Column
    private String memail;
    @Column
    private  String m_address;
    @Column
    private  int m_point;
    @Enumerated(EnumType.STRING)//
    @Column
    private  Role m_grade;//회원등급
     //DB는 ROLE 자료형X->  @Enumerated(EnumType.String)문자열 자료형 변환
    //oauth2 에서 동일한 이메일이면 업데이트 처리 메소드
     public MemberEntity update(String name){
      this.m_name =name;
      return this;
     }
  //해당 Role에 key반환 메소드드
    public String getRoleKey(){return this.m_grade.getKey();}



 // 룸 리스트
 @OneToMany( mappedBy ="memberEntity" )
 private List<RoomEntity> roomEntities = new ArrayList<>();
 //문의 리스트
 @OneToMany( mappedBy ="memberEntity" )
 private List<NoteEntity> NoteEntities = new ArrayList<>();

}
