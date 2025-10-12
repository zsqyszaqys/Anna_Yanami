package com.anna_yanami.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class BackendApplicationTests {

    @Test
    void contextLoads() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("200716"));
        System.out.println(passwordEncoder.matches("200716", "$2a$10$k3gQJMji.06pKErXTCnobesJgMPEwNfAjg46CoEOSpWhie2d3yhAW"));
    }

}
