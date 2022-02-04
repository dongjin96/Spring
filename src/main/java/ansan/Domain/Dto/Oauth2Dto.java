package ansan.Domain.Dto;

import ansan.Domain.Entity.Member.MemberEntity;
import ansan.Domain.Entity.Member.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
@Getter @Setter
public class Oauth2Dto {

    // 이름
    String name;
    // 이메일
    String email;
    // 회원정보
    private Map<String , Object> attribute;
    // 요청 토큰[키]
    private String nameattributekey;

    // 풀생성자
    @Builder
    public Oauth2Dto(String name, String email, Map<String, Object> attribute, String nameattributekey) {
        this.name = name;
        this.email = email;
        this.attribute = attribute;
        this.nameattributekey = nameattributekey;
    }

    // 클라이언트 구분용 메소드 [ 카카오 or 네이버 or 구글 ]
    public static Oauth2Dto of( String registrationid , String nameattributekey , Map<String, Object> attribute){
        if( registrationid.equals("kakao")){   return ofkakao(    nameattributekey , attribute  ); }   // 카카오
        else if( registrationid.equals("naver") ){ return ofnaver(    nameattributekey , attribute  );} // 네이버
        else{ return null;} // 구글

    }
    // 네이버 정보 dto 변환 메소드
    private static Oauth2Dto ofnaver( String nameattributekey ,Map<String, Object> attribute  ){

        Map<String, Object> response = (Map<String, Object>) attribute.get("response"); // response 키 호출

        return Oauth2Dto.builder()
                .name( (String) response.get("name") ) // name 이거 우리가 정하는거아님
                .email((String) response.get("email"))
                .attribute( attribute )
                .nameattributekey( nameattributekey )
                .build();
    }

    // 카카오 정보 dto 변환 메소드
    private static Oauth2Dto ofkakao( String nameattributekey ,Map<String, Object> attribute  ){

        Map<String, Object> kakao_account = (Map<String, Object>) attribute.get("kakao_account"); // kakao account 키 호출
        Map<String,Object> profile = (Map<String, Object>) kakao_account.get("profile");

        return Oauth2Dto.builder()
                .name( (String) profile.get("nickname") )
                .email((String) kakao_account.get("email"))
                .attribute( attribute )
                .nameattributekey( nameattributekey )
                .build();
    }
    // 첫 로그인했을때 회원가입 dto -> entitiy
    public MemberEntity toEntity(){
        return MemberEntity.builder().m_name(this.name).memail(this.email).m_grade(Role.Member).build();
    }


}
