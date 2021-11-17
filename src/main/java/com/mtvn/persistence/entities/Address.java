package com.mtvn.persistence.entities;

import com.mtvn.api.shared.PincodeApi;
import com.mtvn.common.string.StringUtils;
import com.mtvn.enums.AddressType;
import com.mtvn.model.AddressModel;
import com.mtvn.persistence.entities.template.BaseEntityIntID;
import com.mtvn.persistence.entities.template.GeoLocation;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "address",
        indexes = {
                @Index(name = "idx_address_state", columnList = "state")
        }
)
@EqualsAndHashCode(callSuper = true)
@Data
public class Address extends BaseEntityIntID {

    @Column(name = "addressline1", nullable = false)
    private String addressLine1;

    @Column(name = "addressline2", nullable = false)
    private String addressLine2;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "pincode", nullable = false)
    private Integer pincode;

    @Column(name = "country")
    private String country;

    @Column(name = "banker_edited_addressline1")
    private String bankerEditedAddressline1;

    @Column(name = "banker_edited_addressline2")
    private String bankerEditedAddressline2;

    @Column(name = "banker_edited_city")
    private String bankerEditedCity;

    @Column(name = "banker_edited_state")
    private String bankerEditedState;

    @Column(name = "banker_edited_pincode")
    private Integer bankerEditedPincode;

    @Column(name = "landmark")
    private String landmark;

    @Column(name = "city_subdivision_1", length=32)
    private String citySubdivision1;

    @Column(name = "city_subdivision_2", length=32)
    private String citySubdivision2;

    @Column(name = "address_type")
    @Enumerated(EnumType.STRING)
    private AddressType addressType;

    private GeoLocation geoLocation;

    public String getBankerEditedAddressline1() {
        return StringUtils.sanitizeTextField(bankerEditedAddressline1);
    }

    public void setBankerEditedAddressline1(String bankerEditedAddressline1) {
        this.bankerEditedAddressline1 = StringUtils.sanitizeTextField(bankerEditedAddressline1);
    }

    public String getBankerEditedAddressline2() {
        return StringUtils.sanitizeTextField(bankerEditedAddressline2);
    }

    public void setBankerEditedAddressline2(String bankerEditedAddressline2) {
        this.bankerEditedAddressline2 = StringUtils.sanitizeTextField(bankerEditedAddressline2);
    }

    public String getBankerEditedCity() {
        if(bankerEditedPincode != null && bankerEditedPincode > 0 && !StringUtils.hasText(this.bankerEditedCity)) {
            this.bankerEditedCity = PincodeApi.getCity(bankerEditedPincode.toString());
        }
        return StringUtils.sanitizeTextField(bankerEditedCity);
    }

    public void setBankerEditedCity(String bankerEditedCity) {
        this.bankerEditedCity = StringUtils.sanitizeTextField(bankerEditedCity);
    }

    public String getBankerEditedState() {
        if(bankerEditedPincode != null && bankerEditedPincode > 0 && !StringUtils.hasText(this.bankerEditedState)) {
            this.bankerEditedState = PincodeApi.getState(bankerEditedPincode.toString());
        }
        return StringUtils.sanitizeTextField(bankerEditedState);
    }

    public void setBankerEditedState(String bankerEditedState) {
        this.bankerEditedState = StringUtils.sanitizeTextField(bankerEditedState);
    }

    public void setBankerEditedPincode(Integer bankerEditedPincode) {
        if(bankerEditedPincode != null && bankerEditedPincode > 0) {
            this.bankerEditedPincode = bankerEditedPincode;
            String sPincode = bankerEditedPincode.toString();
            if(!StringUtils.hasText(this.bankerEditedCity))
                this.bankerEditedCity = PincodeApi.getCity(sPincode);
            if(!StringUtils.hasText(this.bankerEditedState))
                this.bankerEditedState = PincodeApi.getState(sPincode);
        }else
            this.bankerEditedPincode = null;
    }

    public String getCountry() {
        return StringUtils.sanitizeTextField(country);
    }

    public void setCountry(String country) {
        this.country = StringUtils.sanitizeTextField(country);
    }

    public String getAddressLine1() {
        return StringUtils.sanitizeTextField(addressLine1);
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = StringUtils.sanitizeTextField(addressLine1);
    }

    public String getAddressLine2() {
        return StringUtils.sanitizeTextField(addressLine2);
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = StringUtils.sanitizeTextField(addressLine2);
    }

    public String getCity() {
        if(this.pincode != null && this.pincode > 0 && !StringUtils.hasText(this.city)) {
            this.city = PincodeApi.getCity(this.pincode.toString());
        }
        return StringUtils.sanitizeTextField(city);
    }

    public void setCity(String city) {
        this.city = StringUtils.sanitizeTextField(city);
    }

    public String getState() {
        if(this.pincode != null && this.pincode > 0 && !StringUtils.hasText(this.state)) {
            this.state = PincodeApi.getState(this.pincode.toString());
        }
        return StringUtils.sanitizeTextField(state);
    }

    public void setState(String state) {
        this.state = StringUtils.sanitizeTextField(state);
    }

    public void setPincode(Integer pincode) {
        if(pincode != null && pincode > 0) {
            this.pincode = pincode;
            String sPincode = pincode.toString();
            if(!StringUtils.hasText(this.city))
                this.city = PincodeApi.getCity(sPincode);
            if(!StringUtils.hasText(this.state))
                this.state = PincodeApi.getState(sPincode);
        }else
            this.pincode = null;
    }

    public Address clone() throws CloneNotSupportedException {
        super.clone();
        Address newAddress = new Address();
        newAddress.setAddressLine1(addressLine1);
        newAddress.setAddressLine2(addressLine2);
        newAddress.setArchived(false);
        newAddress.setDeleted(false);
        newAddress.setCity(city);
        newAddress.setCitySubdivision1(citySubdivision1);
        newAddress.setCitySubdivision2(citySubdivision2);
        newAddress.setCountry(country);
        newAddress.setGeoLocation(geoLocation);
        newAddress.setLandmark(landmark);
        newAddress.setState(state);
        newAddress.setPincode(pincode);
        return newAddress;
    }

    public String getCombinedAddressLineForTU() {
        return getCombinedAddressLineForTU(false);
    }

    public String getCombinedAddressLineForTU(boolean includeLandmark) {
        StringBuilder sb = new StringBuilder();
        if(StringUtils.hasText(this.bankerEditedAddressline1))
            sb.append(bankerEditedAddressline1).append(" ");
        else if(StringUtils.hasText(this.addressLine1))
            sb.append(addressLine1).append(" ");

        if(StringUtils.hasText(this.bankerEditedAddressline2))
            sb.append(bankerEditedAddressline2).append(" ");
        else if(StringUtils.hasText(this.addressLine2))
            sb.append(addressLine2).append(" ");
        if(includeLandmark && StringUtils.hasText(this.landmark)) {
            sb.append(this.landmark);
        }
        return StringUtils.sanitizeTextField(sb.toString());
    }

    public String getFullAddress() {
        String addressLines = getCombinedAddressLineForTU();
        addressLines = StringUtils.hasText(addressLines) ? addressLines : "";
        StringBuilder sb = new StringBuilder(addressLines);
        if(StringUtils.hasText(city))
            sb.append(" ").append(city);
        if(StringUtils.hasText(citySubdivision1))
            sb.append(" ").append(citySubdivision1);
        if(StringUtils.hasText(citySubdivision2))
            sb.append(" ").append(citySubdivision2);
        if(StringUtils.hasText(state))
            sb.append(" ").append(state);
        if(StringUtils.hasText(country))
            sb.append(" ").append(country);
        if(pincode != null && pincode > 0)
            sb.append(" ").append(pincode);
        return StringUtils.sanitizeTextField(sb.toString());
    }

    public String getPincodeForTU() {
        return this.getBankerEditedPincode() != null && this.getBankerEditedPincode() > 0
                ? this.getBankerEditedPincode().toString()
                : this.getPincode() != null && this.getPincode() > 0 ? this.getPincode().toString() : null;
    }

    public String getCityForTU() {
        return StringUtils.hasText(this.bankerEditedCity) ? this.bankerEditedCity : this.city;
    }

    public String getStateForTU() {
        return StringUtils.hasText(this.bankerEditedState) ? this.bankerEditedState : this.state;
    }

    public AddressModel getBean() {
        AddressModel address = new AddressModel();
        address.setState(this.getState());
        address.setCity(this.getCity());
        address.setAddressLine1(this.getAddressLine1());
        address.setAddressLine2(this.getAddressLine2());
        address.setPincode(this.getPincode());
        address.setCitySubdivision1(this.getCitySubdivision1());
        address.setCitySubdivision2(this.getCitySubdivision2());
        if(this.getGeoLocation() != null) {
            address.setGeoLocation(GeoLocation.getFromEntity(this.getGeoLocation()));
        }
        address.setLandmark(this.getLandmark());
        address.setAddressType(this.getAddressType());
        return address;
    }

    public static Address modelToEntity(AddressModel model) {
        if (model == null) {
            return null;
        }
        boolean hasAnyField = false;
        Address address = new Address();
        if(StringUtils.hasText(model.getState())) {
            hasAnyField = true;
            address.setState(model.getState());
        }
        if(StringUtils.hasText(model.getCity())) {
            hasAnyField = true;
            address.setCity(model.getCity());
        }
        if(StringUtils.hasText(model.getCitySubdivision1())) {
            hasAnyField = true;
            address.setCitySubdivision1(model.getCitySubdivision1());
        }
        if(StringUtils.hasText(model.getCitySubdivision2())) {
            hasAnyField = true;
            address.setCitySubdivision2(model.getCitySubdivision2());
        }
        if(StringUtils.hasText(model.getAddressLine1())
                || StringUtils.hasText(model.getAddressLine2())) {
            hasAnyField = true;
            address.setAddressLine1(model.getAddressLine1());
            address.setAddressLine2(model.getAddressLine2());
        }
        if(model.getPincode() != null && model.getPincode() > 0) {
            hasAnyField = true;
            address.setPincode(model.getPincode());
        }
        if(model.getGeoLocation() != null) {
            hasAnyField = true;
            if(model.getGeoLocation() != null) {
                address.setGeoLocation(GeoLocation.getFromModel(model.getGeoLocation()));
            }

        }
        if(StringUtils.hasText(model.getLandmark())) {
            hasAnyField = true;
            address.setLandmark(model.getLandmark());
        }
        if(model.getAddressType() != null)
            address.setAddressType(model.getAddressType());
        return hasAnyField ? address : null;
    }

}
