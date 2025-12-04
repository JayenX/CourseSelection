package com.example;

import com.example.common.Result;
import com.example.entity.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test: Verify that the administrator login was successful
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminLoginIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testAdminLoginSuccess() {
        Account account = new Account();
        account.setUsername("admin");
        account.setPassword("admin");
        account.setRole("ADMIN");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Account> request = new HttpEntity<>(account, headers);
        ResponseEntity<Result> response = restTemplate.postForEntity("/login", request, Result.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("200", response.getBody().getCode());
        assertNotNull(response.getBody().getData());
        System.out.println("✅ The administrator login test passed");
    }
}
