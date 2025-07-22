package com.loievroman.bookstoreapp;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookStoreAppApplicationTests {

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void contextLoads() {
    }

}
