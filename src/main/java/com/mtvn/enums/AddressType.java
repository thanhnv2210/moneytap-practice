package com.mtvn.enums;

import lombok.Getter;

public enum AddressType {

	PERMANENT("Permenent Address"),
    HOME("Residence Address"),
    WORK("Work Address"),
    OTHER("Other");
    
    AddressType(String description) {
    	this.description = description;
    }
    @Getter private String description;
}
