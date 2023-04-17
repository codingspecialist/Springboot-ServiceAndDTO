package shop.mtcoding.servicebank.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.mtcoding.servicebank.model.account.Account;
import shop.mtcoding.servicebank.model.account.AccountRepository;
import shop.mtcoding.servicebank.model.transaction.Transaction;
import shop.mtcoding.servicebank.model.transaction.TransactionRepository;
import shop.mtcoding.servicebank.model.user.User;
import shop.mtcoding.servicebank.model.user.UserRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TransactionRepositoryTest {
    @Autowired
    private TransactionRepository transactionRepository;

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
    void findByDeposit() {
        //given
        int number = 1111;

        //when
        List<Transaction> transaction = transactionRepository.findByDeposit(number);

        //then
        assertEquals(0,transaction.size());
    }

    @Test
    void findByWithdraw() {
        //given
        int number = 1111;

        //when
        List<Transaction> transaction = transactionRepository.findByWithdraw(number);

        //then
        assertEquals(0,transaction.size());
    }

    @Test
    void findByDepositAndWithdraw() {
        //given
        int number = 1111;

        //when
        List<Transaction> transaction = transactionRepository.findByDepositAndWithdraw(number);

        //then
        assertEquals(0,transaction.size());
    }
}