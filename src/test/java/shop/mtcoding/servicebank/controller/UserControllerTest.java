package shop.mtcoding.servicebank.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import shop.mtcoding.servicebank.core.session.SessionUser;
import shop.mtcoding.servicebank.dto.user.UserRequest;
import shop.mtcoding.servicebank.dto.user.UserResponse;
import shop.mtcoding.servicebank.service.UserService;

import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserController.class) class UserControllerTest {


    @MockBean
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("/join - POST")
    void test1() throws Exception {
        // given
        UserRequest.JoinInDTO joinInDTO = UserRequest.JoinInDTO.builder()
                .email("owner@naver.com")
                .password("12345")
                .fullName("owner")
                .username("TESTER")
                .build();
        // when

        given(userService.회원가입(any(UserRequest.JoinInDTO.class)))
                .willReturn(UserResponse.JoinOutDTO.builder()
                        .id(1L)
                        .fullName("owner")
                        .username("TESTER")
                        .build());
        // then

        mockMvc.perform(post("/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(joinInDTO))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.msg").value("성공"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.username").value("TESTER"))
                .andExpect(jsonPath("$.data.fullName").value("owner"));
    }

    @Test
    @DisplayName("/login -POST")
    void test2() throws Exception {
        // given
        MockHttpSession  httpSession = new MockHttpSession();

        UserRequest.LoginInDTO loginInDTO =  UserRequest.LoginInDTO.builder()
                .username("owner")
                .password("12345")
                .build();

        SessionUser sessionUser = SessionUser.builder()
                .username("owner")
                .id(1L)
                .build();
        // when
        given(userService.로그인(any(UserRequest.LoginInDTO.class)))
                .willReturn(sessionUser);

        httpSession.setAttribute("sessionUser",sessionUser);
        // then

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(loginInDTO))
                .session(httpSession))

                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.msg").value("성공"))
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.id").value(1))
                    .andExpect(jsonPath("$.data.username").value("owner"));


        Assertions.assertThat(httpSession.getAttribute("sessionUser")).isEqualTo(sessionUser);
    }
}