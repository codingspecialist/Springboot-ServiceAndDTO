package shop.mtcoding.servicebank.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.mtcoding.servicebank.core.exception.Exception400;
import shop.mtcoding.servicebank.core.exception.Exception401;
import shop.mtcoding.servicebank.core.session.SessionUser;
import shop.mtcoding.servicebank.dto.ResponseDTO;
import shop.mtcoding.servicebank.service.TransactionService;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@RestController
public class TransactionController {
    private final TransactionService transactionService;
    private final HttpSession session;

    @GetMapping("/account/{number}/transaction")
    public ResponseEntity<?> findTransaction(@PathVariable Integer number,
                                             @RequestParam(defaultValue = "all") String gubun) {
        SessionUser sessionUser = (SessionUser) session.getAttribute("sessionUser");
        if(sessionUser == null){
            throw new Exception401("인증되지 않은 사용자입니다");
        }


        if(gubun.equals("all")){
            ResponseDTO<?> responseDTO = new ResponseDTO<>()
                    .data(transactionService.입출금내역보기(number, sessionUser.getId()));
            return ResponseEntity.ok(responseDTO);
        }else if(gubun.equals("withdraw")){

            ResponseDTO<?> responseDTO = new ResponseDTO<>()
                    .data(transactionService.출금내역보기(number, sessionUser.getId()));
            return ResponseEntity.ok(responseDTO);
        }else if(gubun.equals("deposit")){
            ResponseDTO<?> responseDTO = new ResponseDTO<>()
                    .data(transactionService.입금내역보기(number, sessionUser.getId()));
            return ResponseEntity.ok(responseDTO);
        }else{
            throw new Exception400("gubun", "잘못된 요청을 하였습니다 : "+gubun);
        }
    }
}
