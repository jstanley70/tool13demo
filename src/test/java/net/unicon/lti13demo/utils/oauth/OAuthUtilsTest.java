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

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.ParseException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;

import net.unicon.lti13demo.model.PlatformDeployment;
import net.unicon.lti13demo.model.RSAKeyEntity;
import net.unicon.lti13demo.model.dto.JWKPublicKey;

@ExtendWith(SpringExtension.class)
public class OAuthUtilsTest {

	@Test
	public void loadKeysTest() throws GeneralSecurityException, IOException {
		RSAKeyEntity entity = buildTestEntity();
		PrivateKey pk1 = OAuthUtils.loadPrivateKey(entity.getPrivateKey());
		PrivateKey pk2 = OAuthUtils2.loadPrivateKey(entity.getPrivateKey());
		
		assertTrue(pk1.equals(pk2));
	}
	
	@Test
	public void loadPublicKeys() throws GeneralSecurityException, IOException {
		RSAKeyEntity entity = buildTestEntity();

		
		PublicKey pub1 = OAuthUtils.loadPublicKey(entity.getPublicKey());
		PublicKey pub2 = OAuthUtils2.loadPublicKey(entity.getPublicKey());
		
		assertTrue(pub1.equals(pub2));
	}
	
	@Test
	public void testConversions() throws GeneralSecurityException, IOException, ParseException, JOSEException {
		ObjectMapper mapper = new ObjectMapper();
		RSAKeyEntity entity = buildTestEntity();
		String jwkJson = OAuthUtils2.getPublicKeyJson(entity);
		JWKPublicKey json = mapper.readValue(jwkJson, JWKPublicKey.class);
		String pem = OAuthUtils2.getPublicKeyPem(jwkJson);
		assert(pem.equals(entity.getPublicKey()));
	}
	
	@Test
	public void testCreateJWKPublicKey() throws GeneralSecurityException, IOException, ParseException, JOSEException {
		ObjectMapper mapper = new ObjectMapper();
		RSAKeyEntity entity = buildTestEntity();
		String jwkJson = OAuthUtils2.getPublicKeyJson(entity);
		JWKPublicKey json = mapper.readValue(jwkJson, JWKPublicKey.class);
		assert(json != null);
	}
	
	private RSAKeyEntity buildTestEntity() throws NoSuchAlgorithmException, NoSuchProviderException {
		
		return OAuthUtils2.buildKeyEntity("1");
	}
}
