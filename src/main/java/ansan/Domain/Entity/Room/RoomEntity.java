package ansan.Domain.Entity.Room;


import ansan.Domain.Entity.BaseTimeEntity;
import ansan.Domain.Entity.Member.MemberEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity// 디비내 테이블과 매핑한다
@Table(name = "Room") //테이블 속성[name="테이블이름"]
@AllArgsConstructor//풀생성자[룸복]
@NoArgsConstructor//빈생성자[룸북]
@Getter@Setter // 필드 get ,set 메소드 필드 넣고뺴는 메소드[룸북]
@ToString(exclude ="roomimgEntities") //tostring-> Object 객체의 주소값 @TOSting-->[필드내용물] 원래는 주소값이 하나 딱나오는데  이거쓰면 내용물이리스트로 쭈욱나온다  근데 주소값이 필요하면 쓰면XXXXX
@Builder // 객체 생성시 안정성 보장[new 생성자()<-----> Builder] : 1. 필드 주입순서 없어요~!
// 거래방식 회원번호 이미지 @ont to many Roomimng 리스트  //@many to one membber
public class RoomEntity extends BaseTimeEntity {
    //번호
    @Id // pk [ 기본키 : 테이블 1개당 기본키 1개 권장 ]
    @GeneratedValue( strategy = GenerationType.IDENTITY ) // 오토키
    @Column( name = "rnum") // 필드 속성 ( name ="필드명" )
    private int rnum;
    // 이름
    @Column( name = "rname")
    private String rname;
    //가격
    @Column( name = "rprice")
    private String rprice;
    //면적
    @Column( name = "rarea")
    private int rarea;
    //관리비
    @Column( name = "rmanagementfee")
    private int rmanagementfee;
    //준공날짜
    @Column( name = "rcompletiondate")
    private String rcompletiondate;
    //입주가능일
    @Column( name = "rindate")
    private String rindate;
    //구조
    @Column( name = "rstructure")
    private String rstructure;
    //층/건물층수
    @Column( name = "rfloor")
    private String rfloor;
    //건물종류
    @Column( name = "rkind")
    private String rkind;
    //주소
    @Column( name = "raddress")
    private String raddress;
    // 내용
    @Column( name = "rcontents")
    private String rcontents;
    //상태
    @Column( name = "ractive")
    private String ractive;
    //거래방식 [ 전세 , 월세 , 매매 ]
    @Column( name = "rtrans")
    private String rtrans;
    //회원번호 관계
    @ManyToOne
    @JoinColumn(name ="mnum") // 해당 필드의 이름[컬럼 열 필드]
    private MemberEntity memberEntity;
    //이미지 관계//룸삭제시 이미지 같이 삭제 [제약조건 : cascade= REMOVE]
    @OneToMany( mappedBy = "roomEntity",cascade=CascadeType.REMOVE )
    private List<RoomimgEntity> roomimgEntities = new ArrayList<>();

    //문의글
    //이미지 관계//룸삭제시 이미지 같이 삭제 [제약조건 : cascade= REMOVE]
    @OneToMany( mappedBy = "roomEntity",cascade=CascadeType.REMOVE )
    private List<NoteEntity> NoteEntities = new ArrayList<>();




}
