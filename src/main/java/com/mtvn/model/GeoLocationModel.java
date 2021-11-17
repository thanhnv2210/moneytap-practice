package com.mtvn.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GeoLocationModel {
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal accuracy;
}