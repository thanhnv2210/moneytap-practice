package com.mtvn.rest.controller.hook;

import com.mtvn.rest.dto.MoEngageHookDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MoEngageHookControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void hookMoengageUnauthorized() throws Exception {
        final Map<String, Object> payload = new HashMap<String, Object>() {{
            put("key1", "value1");
            put("key2", "value2");
        }};
        MoEngageHookDto input = MoEngageHookDto.builder()
                .email("thanh@moneytap.vn")
                .payload(payload).build();
        ResponseEntity<Object> response = restTemplate.postForEntity(new URL("http://localhost:" + port + "/v1/hook/moengage").toString(),input,Object.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        //String result = Objects.requireNonNull(response.getBody()).toString();
        //System.out.println(result);
        //assertEquals("SUCCESS", result);
    }
}
