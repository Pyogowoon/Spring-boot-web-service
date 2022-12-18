package com.pyo.web.springboot;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import com.pyo.web.springboot.web.HelloController;



@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = HelloController.class)
public class HelloControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void hello가_리턴된다() throws Exception{
        String hello = "hello";

        mvc.perform(get("/hello")) // MockMvc 통해 /hello 주소로 HTTP GET 요청 (아래 검증기능 이어서 선언 가능)
                .andExpect(status().isOk()) // mvc.perform 결과 검증, HTTP Header Status 검증
                .andExpect(content().string(hello)); // Controller => "hello" 리턴 값 맞는지 검증
    }

    @Test
    public void DTO_리턴() throws Exception{
        String name = "hello";
        int amount = 1000;

        mvc.perform(get("/hello/dto")
                .param("name", name)
                .param("amount", String.valueOf(amount)))
                .andExpect(status().isOk())
            .andExpect(jsonPath("$.name" , is(name)))
            .andExpect(jsonPath("$.amount" , is(amount)));

    }


}
