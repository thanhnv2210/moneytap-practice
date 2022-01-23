package com.mtvn.rest.repository;

import com.mtvn.auth.server.repository.UserRepository;
import com.mtvn.persistence.entities.auth.User;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

//@ExtendWith(SpringExtension.class)
//@DataJpaTest
public class UserRepositoryTest {
    @Autowired private DataSource dataSource;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

//    @Test
    public void whenFindByName_thenReturnEmployee() {
        assertThat(dataSource).isNotNull();

        User thanh = new User();
        thanh.setUsername("david");
        thanh.setPassword("MyPassword");
        thanh.setName("Thanh Nguyen");
        thanh.setEmail("thanh@moneytap.vn");
        thanh.setPhone("389963096");
        entityManager.persist(thanh);
        entityManager.flush();

        // when
        Optional<User> findUser = userRepository.findByPhone(thanh.getPhone());

        // then
        assertThat(findUser).isPresent();
        assertEquals("david", findUser.get().getUsername());
    }
}
