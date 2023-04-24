package shop.mtcoding.servicebank.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.mtcoding.servicebank.model.user.User;
import shop.mtcoding.servicebank.model.user.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

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
    void findByUsername() {
        //given
        String username = "test";

        //when
        User user = userRepository.findByUsername(username).orElse(null);

        //then
        assertEquals(username,user.getUsername());
    }
}