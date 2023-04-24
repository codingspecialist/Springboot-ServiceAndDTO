package shop.mtcoding.servicebank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import shop.mtcoding.servicebank.dto.user.UserRequest;
import shop.mtcoding.servicebank.model.user.User;
import shop.mtcoding.servicebank.model.user.UserRepository;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp(@Autowired UserRepository userRepository){
        userRepository.deleteAll();
        User user = User.builder()
                .username("test")
                .password("1234")
                .email("email@email.com")
                .fullName("tester")
                .status(true)
                .build();
        userRepository.save(user);
    }

    @Test
    @DisplayName("회원가입")
    void join() throws Exception {
        //given
        UserRequest.JoinInDTO join = UserRequest.JoinInDTO.builder()
                .username("test1")
                .password("1234")
                .email("email@email.com")
                .fullName("tester")
                .build();
        String requestBody = new ObjectMapper().writeValueAsString(join);

        //when
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/join")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON);
        ResultActions actions = mockMvc.perform(builder);

        //then
        ResultMatcher isOk = MockMvcResultMatchers.status().isOk();
        actions.andExpect(isOk);
    }

    @Test
    @DisplayName("로그인")
    void login() throws Exception {
        //given
        UserRequest.LoginInDTO login = UserRequest.LoginInDTO.builder()
                .username("test")
                .password("1234")
                .build();
        String requestBody = new ObjectMapper().writeValueAsString(login);

        //when
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/login")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON);
        ResultActions actions = mockMvc.perform(builder);

        //then
        ResultMatcher isOk = MockMvcResultMatchers.status().isOk();
        actions.andExpect(isOk);
    }
}