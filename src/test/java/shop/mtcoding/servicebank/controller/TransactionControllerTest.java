package shop.mtcoding.servicebank.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import shop.mtcoding.servicebank.core.session.SessionUser;
import shop.mtcoding.servicebank.dto.transaction.TransactionResponse;
import shop.mtcoding.servicebank.service.TransactionService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(TransactionController.class) class TransactionControllerTest {

        @MockBean
        TransactionService transactionService;


        @Autowired
        MockMvc mockMvc;

        @Test
        @DisplayName("/account/{number}/transaction - GET")
        void TransactionControllerTest() throws Exception {
            // given

            // when
            given(transactionService.입출금내역보기(anyInt(),anyLong()))
                    .willReturn(TransactionResponse.WithDrawAndDepositOutDTO.builder()
                            .transactions(List.of(new TransactionResponse.WithDrawAndDepositOutDTO.TransactionDTO(),
                                    new TransactionResponse.WithDrawAndDepositOutDTO.TransactionDTO()))
                            .balance(404L)
                            .fullName("fullName")
                            .id(1L)
                            .number(404)
                            .build());
            // then
          mockMvc.perform(MockMvcRequestBuilders.get("/account/1/transaction")
                    .param("gubun","all")
                    .sessionAttr("sessionUser", SessionUser.builder()
                            .id(1L)
                            .username("user")
                            .createdAt(LocalDateTime.now().toString())
                            .build())
            )
                  .andExpect(MockMvcResultMatchers.status().isOk())
                  .andExpect(jsonPath("$.status").value(200))
                  .andExpect(jsonPath("$.msg").value("성공"))
                  .andExpect(jsonPath("$.data").isNotEmpty())
                  .andExpect(jsonPath("$.data.id").value(1))
                  .andExpect(jsonPath("$.data.number").value(404))
                  .andExpect(jsonPath("$.data.balance").value(404))
                  .andExpect(jsonPath("$.data.fullName").value("fullName"))
                  .andExpect(jsonPath("$.data.transactions.length()").value(2));



        }
}