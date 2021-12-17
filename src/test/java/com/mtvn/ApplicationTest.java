package com.mtvn;

import com.mtvn.rest.controller.shared.DropdownController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTest {
    @Autowired
    DropdownController dropdownController;
    @Test
    public void contextLoads() {
        assertThat(dropdownController).isNotNull();
    }
}
