package ansan.Service;

import ansan.Domain.Dto.MemberDto;
import ansan.Domain.Dto.Oauth2Dto;
import ansan.Domain.Entity.Member.MemberEntity;
import ansan.Domain.Entity.Member.MemberRepository;
import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;



    @Service
    public class OauthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
        @Override // 소셜 로그인후 회원정보 가져오기 메소드
        public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
            OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
            OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);    //perperties 에 요청 url 로부터 인증,토큰,회원정보등등
            // 회원정보 속성 가져오기
            String nameattributekey = userRequest.getClientRegistration()
                    .getProviderDetails()
                    .getUserInfoEndpoint()
                    .getUserNameAttributeName();
            // 클라이언트 아이디 가져오기
            String registrationid = userRequest.getClientRegistration().getRegistrationId();

            // DTO
            Oauth2Dto oauth2Dto = Oauth2Dto.of(registrationid, nameattributekey, oAuth2User.getAttributes());
            // DB 저장
            MemberEntity memberEntity = saveorupdate(oauth2Dto);

            //세션할당
            //소셜로그인시 id가 없기때문에 이메일에서 @뒤를 제거한 아이디를 세션에 담기
            String snid = memberEntity.getMemail().split("@")[0];
            MemberDto loginDto = MemberDto.builder().m_id(snid).m_num(memberEntity.getM_num()).build();

            HttpSession session = request.getSession(); //서버내 세션 가져오기
            session.setAttribute("logindto", loginDto); //세션 설정하기


            //리턴(회원 정보와 인증 권한[키])
            return new DefaultOAuth2User(
                    Collections.singleton(new SimpleGrantedAuthority(memberEntity.getRoleKey())),
                    oauth2Dto.getAttribute(),
                    oauth2Dto.getNameattributekey()
            );
        }
        @Autowired
        private HttpServletRequest request;
        @Autowired
        private MemberRepository memberRepository;

        // 동일한 이메일이 있을경우 업데이트 동일한 이메일 없으면 저장
        public MemberEntity saveorupdate(Oauth2Dto oauth2Dto) {
            //1. memberRepository 이용한 동일한 이메일찾기[findBy 필드명 --> 반환타입 이 optional]
            MemberEntity memberEntity = memberRepository.findBymemail( oauth2Dto.getEmail() )
                    .map(entity->entity.update(oauth2Dto.getName()))//map(
                    .orElse(oauth2Dto.toEntity()); //orElse(): 동일한 이메일이 없을경우 dto->entity





            //OPtional 클래스
            //1.orElse(해당  OPtional객체 null이면)
            //2.map(해당값---> 이벤트): 여러개 있을경우 모두 처리
            return memberRepository.save(memberEntity);
        }
    }

