package com.mtvn.auth.server.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwkCertificateModel {
    @SerializedName("kid")       String keyId;
    @SerializedName("kty")       String keyType;
    @SerializedName("alg")       String algorithm;
    @SerializedName("n")         String modulus;
    @SerializedName("e")         String exponent;
    @SerializedName("use")       String usage = "sig";
}
