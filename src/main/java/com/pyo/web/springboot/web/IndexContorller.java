package com.pyo.web.springboot.web;

import com.pyo.web.springboot.config.auth.dto.SessionUser;
import com.pyo.web.springboot.service.posts.PostsService;
import com.pyo.web.springboot.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;


@RequiredArgsConstructor
@Controller
public class IndexContorller {

    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model) { //model 로 서버 템플릿 엔진에서 사용할수있는 객체를 저장
        // 여기서는 postService.findAllDesc() 로 가져온 결과를 posts로 index.mustache에 전달함
        model.addAttribute("posts", postsService.findAllDesc());

        SessionUser user = (SessionUser)httpSession.getAttribute("user");
        if(user != null){
            model.addAttribute("userName" , user.getName());
        }

        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";

    }

    @GetMapping("/posts/update/{id}")
    public String postUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";

    }

}
