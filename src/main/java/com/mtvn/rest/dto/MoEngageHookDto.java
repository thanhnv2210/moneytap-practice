package com.mtvn.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoEngageHookDto {
    private String email;
    private Map<String, Object> payload;
}
