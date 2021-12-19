package com.mtvn.rest.controller.shared;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.net.URL;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DropdownControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getCompanies() throws Exception {
        ResponseEntity<Map> response = restTemplate.getForEntity(new URL("http://localhost:" + port + "/shared/companies").toString(), Map.class);
        System.out.println(response.getBody());
        assert response.getBody() != null;
        assertEquals(2, response.getBody().size());
    }
}
