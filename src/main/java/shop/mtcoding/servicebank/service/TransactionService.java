package shop.mtcoding.servicebank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.servicebank.core.exception.Exception404;
import shop.mtcoding.servicebank.dto.account.AccountRequest;
import shop.mtcoding.servicebank.dto.account.AccountResponse;
import shop.mtcoding.servicebank.dto.transaction.TransactionResponse;
import shop.mtcoding.servicebank.model.account.Account;
import shop.mtcoding.servicebank.model.account.AccountRepository;
import shop.mtcoding.servicebank.model.transaction.Transaction;
import shop.mtcoding.servicebank.model.transaction.TransactionRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional(readOnly = true)
    public TransactionResponse.WithdrawOutDTO 출금내역보기(Integer number, Long userId) {
        // 1. 계좌 확인
        Account accountPS = accountRepository.findByNumber(number)
                .orElseThrow(
                        () -> new Exception404("계좌를 찾을 수 없습니다"));

        // 2. 계좌 소유주 확인
        accountPS.checkOwner(userId);

        // 3. 출금 내역 조회
        List<Transaction> transactionListPS = transactionRepository.findByWithdraw(number);
        return new TransactionResponse.WithdrawOutDTO(accountPS, transactionListPS);
    }

    @Transactional(readOnly = true)
    public TransactionResponse.DepositOutDTO 입금내역보기(Integer number, Long userId) {
        // 1. 계좌 확인
        Account accountPS = accountRepository.findByNumber(number)
                .orElseThrow(
                        () -> new Exception404("계좌를 찾을 수 없습니다"));

        // 2. 계좌 소유주 확인
        accountPS.checkOwner(userId);

        // 3. 입금 내역 조회
        List<Transaction> transactionListPS = transactionRepository.findByDeposit(number);
        return new TransactionResponse.DepositOutDTO(accountPS, transactionListPS);
    }

    @Transactional(readOnly = true)
    public TransactionResponse.WithDrawAndDepositOutDTO 입출금내역보기(Integer number, Long userId) {
        // 1. 계좌 확인
        Account accountPS = accountRepository.findByNumber(number)
                .orElseThrow(
                        () -> new Exception404("계좌를 찾을 수 없습니다"));

        // 2. 계좌 소유주 확인
        accountPS.checkOwner(userId);

        // 3. 입출금 내역 조회
        List<Transaction> transactionListPS = transactionRepository.findByDepositAndWithdraw(number);
        return new TransactionResponse.WithDrawAndDepositOutDTO(accountPS, transactionListPS);
    }
}
