package com.mtvn.persistence.entities.template;

import com.mtvn.model.GeoLocationModel;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class GeoLocation {
    @Column(name = "latitude")
    private BigDecimal latitude;

    @Column(name = "longitude")
    private BigDecimal longitude;

    @Column(name = "accuracy")
    private BigDecimal accuracy;

    public GeoLocation() {}

    public GeoLocation(BigDecimal latitude, BigDecimal longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "GeoLocation{"
                + "accuracy=" + accuracy
                + ", latitude=" + latitude
                + ", longitude=" + longitude
                + '}';
    }

    public BigDecimal getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(BigDecimal accuracy) {
        this.accuracy = accuracy;
    }

    public static GeoLocation getFromModel(GeoLocationModel geoLocation) {
        GeoLocation gl = new GeoLocation();
        BeanUtils.copyProperties(geoLocation, gl);
        return gl;
    }

    public static GeoLocationModel getFromEntity(GeoLocation geoLocation) {
        GeoLocationModel gl = new GeoLocationModel();
        BeanUtils.copyProperties(geoLocation, gl);
        return gl;
    }
}