package com.mtvn.auth.server.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class JwkCertificateModel {
    @SerializedName("kid")       String keyId;
    @SerializedName("kty")       String keyType;
    @SerializedName("alg")       String algorithm;
    @SerializedName("n")         String modulus;
    @SerializedName("e")         String exponent;
    @SerializedName("use")       String usage = "sig";
}
