package shop.mtcoding.servicebank.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import shop.mtcoding.servicebank.core.session.SessionUser;
import shop.mtcoding.servicebank.model.account.Account;
import shop.mtcoding.servicebank.model.account.AccountRepository;
import shop.mtcoding.servicebank.model.user.User;
import shop.mtcoding.servicebank.model.user.UserRepository;

import java.util.Arrays;
import java.util.List;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private SessionUser sessionUser;

    @BeforeEach
    void setUp(@Autowired UserRepository userRepository, @Autowired AccountRepository accountRepository){
        userRepository.deleteAll();
        accountRepository.deleteAll();

        User user1 = User.builder()
                .username("test1")
                .password("1234")
                .email("email@email.com")
                .fullName("tester1")
                .status(true)
                .build();
        User user2 = User.builder()
                .username("test2")
                .password("1234")
                .email("email@email.com")
                .fullName("tester2")
                .status(true)
                .build();
        List<User> users = Arrays.asList(user1,user2);
        userRepository.saveAll(users);
        sessionUser = new SessionUser(user1);

        Account account1 = Account.builder()
                .user(user1)
                .number(1111)
                .password(0000)
                .balance(1000L)
                .status(true)
                .build();
        Account account2 = Account.builder()
                .user(user2)
                .number(2222)
                .password(0000)
                .balance(1000L)
                .status(true)
                .build();
        List<Account> accounts = Arrays.asList(account1,account2);
        accountRepository.saveAll(accounts);
    }

    @Test
    @DirtiesContext
    @DisplayName("계좌조회")
    void findTransaction() throws Exception {
        //given
        int number = 1111;

        //when
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .get("/account/"+number+"/transaction")
                .sessionAttr("sessionUser", sessionUser)
                .contentType(MediaType.APPLICATION_JSON);
        ResultActions actions = mockMvc.perform(builder);

        //then
        ResultMatcher isOk = MockMvcResultMatchers.status().isOk();
        actions.andExpect(isOk);
    }
}