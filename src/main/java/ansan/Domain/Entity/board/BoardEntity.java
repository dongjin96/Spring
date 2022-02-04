package ansan.Domain.Entity.board;


import ansan.Domain.Entity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity // db내 테이블과 매핑 설정
@Table( name = "board") // 테이블속성 / 테이블이름 설정
@Getter@Setter @ToString
@AllArgsConstructor @NoArgsConstructor @Builder
public class BoardEntity extends BaseTimeEntity {
    @Id // pk
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO KEY
    @Column(name = "bnum")
    private int bnum;

    @Column(name = "b_title")
    private String b_title;

    @Column(name = "b_contents", columnDefinition = "LONGTEXT")// 썸머 노트 내용에 사진이 들어갈 경우에 바이트가 커야함
    private String b_contents;

    @Column(name = "b_write")
    private String b_write;

    @Column(name = "b_view")
    private int b_view;

    @Column(name = "b_img")
    private String b_img;

    // 여러개 댓글저장할 리스트 cascade= CascadeType.ALL 삭제되면 다같이삭제된다
    @OneToMany(mappedBy="boardEntity",cascade= CascadeType.ALL)
    private List<ReplyEntitiy> replyEntitiy = new ArrayList<>();

}