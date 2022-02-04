package ansan.Domain.Entity.Room;

import lombok.*;

import javax.persistence.*;

@Entity// 디비내 테이블과 매핑한다
@Table(name = "Roomimg") //테이블 속성[name="테이블이름"]
@AllArgsConstructor//풀생성자[룸복]
@NoArgsConstructor//빈생성자[룸북]
@Getter
@Setter // 필드 get ,set 메소드 필드 넣고뺴는 메소드[룸북]
@ToString//tostring-> Object 객체의 주소값 @TOSting-->[필드내용물] 원래는 주소값이 하나 딱나오는데  이거쓰면 내용물이리스트로 쭈욱나온다  근데 주소값이 필요하면 쓰면XXXXX
@Builder


public class RoomimgEntity { // 디비내 테이블과 매핑한다~!
    //번호
    @Id // pk [기본키:테이블 1개당 기본키 1개 권장 ]
    @GeneratedValue( strategy = GenerationType.IDENTITY ) // 오토키
    @Column( name = "rimgnum") // 필드속성 ( name ="필드명" )
    private int rimgnum;
    //이미지경로
    @Column( name = "rimg")
    private String rimg;
    // 룸 관계
    @ManyToOne
    @JoinColumn(name="rnum")
    private RoomEntity roomEntity;
}


