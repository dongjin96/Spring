package ansan.Domain.Entity.Room;

import ansan.Domain.Entity.BaseTimeEntity;
import ansan.Domain.Entity.Member.MemberEntity;
import lombok.*;

import javax.persistence.*;

@Entity// 디비내 테이블과 매핑한다
@Table(name = "Note") //테이블 속성[name="테이블이름"]
@AllArgsConstructor//풀생성자[룸복]
@NoArgsConstructor//빈생성자[룸북]
@Getter
@Setter // 필드 get ,set 메소드 필드 넣고뺴는 메소드[룸북]
@ToString(exclude ="roomimgEntities") //tostring-> Object 객체의 주소값 @TOSting-->[필드내용물] 원래는 주소값이 하나 딱나오는데  이거쓰면 내용물이리스트로 쭈욱나온다  근데 주소값이 필요하면 쓰면XXXXX
@Builder // 객체 생성시 안정성 보장[new 생성자()<-----> Builder] : 1. 필드 주입순서 없어요~!
// 거래방식 회원번호 이미지 @ont to many Roomimng 리스트  //@many to one membber
public class NoteEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "nnum") //컬럼명
    private int nnum;   //문의번호
    @Column( name = "ncontents")
    private String ncontents;   //문의내용
    @Column( name = "nreply")
    private String nreply;      //문의답변
    @Column( name = "nread")
    private int nread;//0.안읽음 1이면 읽음

    @ManyToOne
    @JoinColumn(name="mnum")
    private MemberEntity memberEntity;  //보낸사람
    @ManyToOne
    @JoinColumn(name="rnum")
    private RoomEntity roomEntity;      //문의방또는 받는사람람


}
