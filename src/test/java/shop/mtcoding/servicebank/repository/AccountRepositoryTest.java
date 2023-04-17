package shop.mtcoding.servicebank.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import shop.mtcoding.servicebank.core.session.SessionUser;
import shop.mtcoding.servicebank.model.account.Account;
import shop.mtcoding.servicebank.model.account.AccountRepository;
import shop.mtcoding.servicebank.model.user.User;
import shop.mtcoding.servicebank.model.user.UserRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AccountRepositoryTest {
    @Autowired
    private AccountRepository accountRepository;

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
    @DirtiesContext
    void findByNumber() {
        //given
        int number = 1111;

        //when
        Account account = accountRepository.findByNumber(number).orElse(null);

        //then
        assertEquals(number,account.getNumber());
    }

    @Test
    @DirtiesContext
    void findByUserId() {
        //given
        long userId = 1;

        //when
        List<Account> account = accountRepository.findByUserId(userId);

        //then
        assertEquals(1,account.size());
    }
}