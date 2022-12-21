package com.pyo.web.springboot.config.auth;

import com.pyo.web.springboot.config.auth.dto.OAuthAttributes;
import com.pyo.web.springboot.config.auth.dto.SessionUser;
import com.pyo.web.springboot.domain.user.User;
import com.pyo.web.springboot.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;


@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User>
                delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest. //registrationid = 현재 로그인 진행중인 서비스를 구분하는 코드
                // 차후 네이버 연동시 네이버 로그인인지 구글 로그인인지 구분할때 사용함
                getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().
                //OAuth2 로그인 진행시 키가 되는 필드값을 의미. primary key와 같은 의미.
                //구글의 경우 기본적으로 코드를 지원하지만 네이버 카카오 등은 기본지원 X 구글의 기본코드는 sub
                        
                getProviderDetails().
                getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
                //OAuthAttribute는 OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담을 클래스
                //이후 네이버 등 다른 소셜 로그인도 이 클래스를 사용
        User user = saveOrUpdate(attributes);
        httpSession.setAttribute("user", new SessionUser(user));
        //SessionUser = 세션에 사용자 정보를 저장하기 위한 DTO 클래스
        //중요한것은 User에서 직접적으로 받아오는것이 아닌 새로 만들어서 사용한다는 것
        return new DefaultOAuth2User(
                Collections.singleton(new
                        SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());

    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}
