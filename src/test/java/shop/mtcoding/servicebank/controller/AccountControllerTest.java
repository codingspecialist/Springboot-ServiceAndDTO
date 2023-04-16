package shop.mtcoding.servicebank.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.jshell.Snippet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockReset;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import shop.mtcoding.servicebank.core.session.SessionUser;
import shop.mtcoding.servicebank.dto.account.AccountRequest;
import shop.mtcoding.servicebank.dto.account.AccountResponse;
import shop.mtcoding.servicebank.service.AccountService;

import javax.servlet.http.HttpSession;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.ResponseEntity.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(AccountController.class)
class AccountControllerTest {



    @Autowired
    MockMvc mockMvc;

    @MockBean
    AccountService accountService;


    @Test
    @DisplayName("/account/{number} - GET")
    void AccountControllerTest() throws Exception {
        // given
        HttpSession httpSession = mock(HttpSession.class);

        // when
        given(accountService.계좌상세보기(anyInt(),anyLong()))
                .willReturn(AccountResponse.DetailOutDTO.builder()
                        .number(404)    //PathVariable에서 받은 값
                        .id(1L) //session에서 꺼낸 값
                        .fullName("fullName")
                        .balance(404L)
                        .build());
        // then

       mockMvc.perform(MockMvcRequestBuilders
                .get("/account/404")
                .sessionAttr("sessionUser", SessionUser.builder()
                        .id(1L)
                        .username("user")
                        .createdAt(LocalDateTime.now().toString())
                        .build()))

               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(jsonPath("$.data.id").value(1))
               .andExpect(jsonPath("$.data.fullName").value("fullName"))
               .andExpect(jsonPath("$.data.number").value(404));

        /** @apiNote
         * {"status":200,
         * "msg":"성공",
         * "data":
         *      {
             *      "id":1,
             *      "fullName":"fullName",
             *      "number":404,
             *      "balance":404,
             *      "createdAt":null
         *      }
     *      }
         * */
    }


    /**
     * @param userId PK를 통해 세션에 유저와 비교하여 계좌를 조회한다.
     * */

    @Test
    @DisplayName("/account - GET")
    void test2() throws Exception {
        // given
        List<AccountResponse.ListOutDTO.AccountDTO> list = new ArrayList<>();
        list.add(AccountResponse.ListOutDTO.AccountDTO.builder().id(1L).balance(404L).balance(404L).build());
        list.add(AccountResponse.ListOutDTO.AccountDTO.builder().id(2L).balance(404L).balance(404L).build());

        given(accountService.유저계좌목록보기(anyLong()))
                .willReturn(AccountResponse.ListOutDTO.builder()
                        .fullName("fullName")
                        .accounts(list)
                        .build());
        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/account")
                .param("userId","1")
                .sessionAttr("sessionUser", SessionUser.builder()
                        .id(1L)
                        .username("user")
                        .createdAt(LocalDateTime.now().toString())
                        .build()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.msg").value("성공"))
                .andExpect(jsonPath("$.data.fullName").value("fullName"))
                .andExpect(jsonPath("$.data.accounts.length()").value(2));
        // then
        /*
         * {
         *   "status":200,
         *   "msg":"성공",
         *   "data":{
         *       "fullName":"fullName",
         *       "accounts":[
         *               {"id":1,
         *               "number":null,
         *               "balance":404},
         *               {"id":2,"number":null,"balance":404}]}}
         *
         *
         * */
    }


    @Test
    @DisplayName("/account - POST")
    void test3() throws Exception {
        // given
        AccountRequest.SaveInDTO saveInDTO = AccountRequest.SaveInDTO.builder()
                .number(1234)
                .password(1111)
                .build();
        // when
        given(accountService.계좌등록(any(AccountRequest.SaveInDTO.class),anyLong()))
                .willReturn(AccountResponse.SaveOutDTO.builder()
                        .number(saveInDTO.getNumber())
                        .balance(404L)
                        .id(1L)
                        .build());

         mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(saveInDTO))
                .sessionAttr("sessionUser", SessionUser.builder()
                    .id(1L)
                    .username("user")
                    .createdAt(LocalDateTime.now().toString())
                    .build()))
             .andExpect(MockMvcResultMatchers.status().isOk())
             .andExpect(jsonPath("$.data.id").value(1))
             .andExpect(jsonPath("$.data.number").value(1234))
             .andExpect(jsonPath("$.data.balance").value(404));


        // then
    }

    @Test
    @DisplayName("/account/transfer - POST")
    void test4() throws Exception {
        // given
        AccountResponse.TransferOutDTO transferOutDTO = AccountResponse.TransferOutDTO.builder()
                .balance(404L)
                .id(1L)
                .number(404)
                .build();

        AccountRequest.TransferInDTO transferInDTO = AccountRequest.TransferInDTO.builder()
                .withdrawNumber(401)
                .withdrawNumber(402)
                .withdrawPassword(403)
                .build();

        // when
        given(accountService.계좌이체(any(AccountRequest.TransferInDTO.class),anyLong()))
                .willReturn(transferOutDTO);
        // then

         mockMvc.perform(post("/account/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(transferInDTO))
                .sessionAttr("sessionUser", SessionUser.builder()
                        .id(1L)
                        .username("user")
                        .createdAt(LocalDateTime.now().toString())
                        .build()))
             .andExpect(MockMvcResultMatchers.status().isOk())
             .andExpect(jsonPath("$.status").value(200))
             .andExpect(jsonPath("$.msg").value("성공"))
             .andExpect(jsonPath("$.data.id").value(1))
             .andExpect(jsonPath("$.data.number").value(404))
             .andExpect(jsonPath("$.data.balance").value(404));


    }
}


