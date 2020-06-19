/**
 * Copyright 2019 Unicon (R)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.unicon.lti13demo.utils.oauth;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nimbusds.jose.Algorithm;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;

import net.unicon.lti13demo.model.PlatformDeployment;
import net.unicon.lti13demo.model.RSAKeyEntity;

public class OAuthUtils2 {
	 static final Logger log = LoggerFactory.getLogger(OAuthUtils.class);
	 
	    static final String PEM_PRIVATE_START = "-----BEGIN PRIVATE KEY-----";
	    static final String PEM_PRIVATE_END = "-----END PRIVATE KEY-----";
	    
	    static final String PUBLIC_START = "-----BEGIN PUBLIC KEY-----";
	    static final String PUBLIC_END = "-----END PUBLIC KEY-----";

	    // PKCS#1 format
	    static final String PEM_RSA_PRIVATE_START = "-----BEGIN RSA PRIVATE KEY-----";
	    static final String PEM_RSA_PRIVATE_END = "-----END RSA PRIVATE KEY-----";

	    //This is added to deal with the PCKS#1 key that IMS is providing in their test platform.

	    private OAuthUtils2() {
	        throw new IllegalStateException("Utility class");
	    }

	    public static RSAPublicKey loadPublicKey(String key) throws GeneralSecurityException {
	        String publicKeyContent = key.replaceAll("\\n", "").replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");
	        KeyFactory kf = KeyFactory.getInstance("RSA");
	        X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent));
	        RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(keySpecX509);
	        return pubKey;
	    }

	    public static PrivateKey loadPrivateKey(String privatePEMKey) throws GeneralSecurityException, IOException {
	    	return PrivateKeyReader.loadPrivateKey(privatePEMKey);
	    }
	    
	    public static RSAKeyEntity buildKeyEntity(String toolId) throws NoSuchAlgorithmException, NoSuchProviderException {
	    	KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(2048);
			KeyPair kp = kpg.generateKeyPair();
			
			Key pub = kp.getPublic();
			Key pvt = kp.getPrivate();

	    	String privateKey = PEM_PRIVATE_START + Base64.getEncoder().encodeToString(pvt.getEncoded()) + PEM_PRIVATE_END;
	    	String publicKey = PUBLIC_START + Base64.getEncoder().encodeToString(pub.getEncoded())  + PUBLIC_END;
	    	return  new  RSAKeyEntity( toolId, true, publicKey, privateKey);
	    }
	    
	    public static RSAKey make(String kid, String publicPEMKey, String privatePEMKey) throws GeneralSecurityException, IOException {

	        try {
	            RSAPublicKey pub = (RSAPublicKey) loadPublicKey(publicPEMKey);
	            RSAPrivateCrtKey priv = (RSAPrivateCrtKey) loadPrivateKey(privatePEMKey);
	            Algorithm keyAlg = JWSAlgorithm.parse("RSA256");
	            RSAKey rsaKey = new RSAKey.Builder(pub)
	                    .privateKey(priv)
	                    .keyUse(KeyUse.SIGNATURE)
	                    .algorithm(keyAlg)
	                    .keyID(kid)
	                    .build();

	            return rsaKey;
	        } catch (NoSuchAlgorithmException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	            return null;
	        }
	    }
	    
	    public static String getPublicKeyJson(RSAKeyEntity entity) throws GeneralSecurityException, IOException {
	    	return getPublicKeyJson(entity.getKid().getKid(), entity.getPublicKey(), entity.getPrivateKey());
	    }
	    
	    public static String getPublicKeyJson(String kid, String publicPEMKey, String privatePEMKey) throws GeneralSecurityException, IOException {
	    	JWK jwk = make( kid, publicPEMKey, privatePEMKey);
	    	JWK pub = jwk.toPublicJWK();
	    	return pub.toJSONString();
	    }
	    
	    public static String getPublicKeyPem(String json) throws ParseException, JOSEException {
	    	RSAKey rsaKey = RSAKey.parse(json);
	    	RSAPublicKey pub = rsaKey.toRSAPublicKey();
	    	return PUBLIC_START + Base64.getEncoder().encodeToString(pub.getEncoded())  + PUBLIC_END;
	    }
	    
	    public static String cleanPublicKey(String publicKey) {
	    	publicKey = publicKey.replace(PUBLIC_START, "");
	    	publicKey = publicKey.replace(PUBLIC_END, "");
	    	return publicKey.replaceAll("\\s", "");
	    }
	    
	    public static String cleanPrivateKey(String publicKey) {
	    	publicKey = publicKey.replace(PEM_RSA_PRIVATE_START, "");
	    	publicKey = publicKey.replace(PEM_RSA_PRIVATE_END, "");
	    	publicKey = publicKey.replace(PEM_PRIVATE_START, "");
	    	publicKey = publicKey.replace(PEM_PRIVATE_END, "");
	    	return publicKey.replaceAll("\\s", "");
	    }
	    
}
