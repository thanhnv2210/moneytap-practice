package com.mtvn.model;

import com.mtvn.enums.AddressType;
import lombok.Data;

@Data
public class AddressModel {
    private String city;
    private String state;
    private Integer pincode;
    private GeoLocationModel geoLocation;
    private String landmark;
    private AddressType addressType;
    private String addressLine1;
    private String addressLine2;
    private String citySubdivision1;
    private String citySubdivision2;
}
