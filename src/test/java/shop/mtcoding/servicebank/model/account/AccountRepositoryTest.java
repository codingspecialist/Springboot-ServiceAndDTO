package shop.mtcoding.servicebank.model.account;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.mtcoding.servicebank.model.user.User;
import shop.mtcoding.servicebank.model.user.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;


    @PersistenceContext
    EntityManager entityManager;

    @BeforeEach
    void setUp(){
        User user = userRepository.save(User.builder()
                .username("username")
                .email("owner@naver.com")
                .password("1234")
                .fullName("fullName")
                .build());


         Account account =  Account.builder()
                 .user(user)
                .number(404)
                .password(1234)
                .status(true)
                .balance(404L)
                .build();


         accountRepository.save(account);
    }

    @Test
    @DisplayName("findByNumber() 테스트")
    void test1() {
        // given
        Account accountQuery = (Account) entityManager.createQuery("select ac from Account ac where ac.number = :number")
                .setParameter("number",404)
                .getSingleResult();

        User user = User.builder()
                .username("username")
                .password("1234")
                .fullName("fullName")
                .build();

        Account account = accountRepository.findByNumber(404).get();
        // when

        // then

        Assertions.assertThat(account).isEqualTo(accountQuery);
        Assertions.assertThat(account.getNumber()).isEqualTo(404);
        Assertions.assertThat(account.getUser().getUsername()).isEqualTo("username");
    }

    @Test
    @DisplayName("findByUserId")
    void test2() {
        // given
        User user = userRepository.findByUsername("username").get();

        List<Account> temp = accountRepository.findByUserId(user.getId());

        List<Account> accountQuery = entityManager.createQuery("select ac from Account ac " +
                "where ac.user.id = :userId")
                .setParameter("userId",user.getId())
                .getResultList();
        // when
        accountQuery.forEach(account -> {
            Assertions.assertThat(temp.contains(account)).isTrue();
        });
        // then
        Assertions.assertThat(accountQuery).hasSize(1);
        Assertions.assertThat(temp).hasSize(1);

    }
}