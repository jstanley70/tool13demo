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
package net.unicon.lti13demo.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import net.unicon.lti13demo.model.RSAKeyEntity;
import net.unicon.lti13demo.model.RSAKeyId;
import net.unicon.lti13demo.model.dto.JWKPublicKey;
import net.unicon.lti13demo.repository.RSAKeyRepository;
import net.unicon.lti13demo.utils.oauth.OAuthUtils2;

/**
 * Serving the public key of the tool.
 */
@Controller
@Scope("session")
@RequestMapping("/jwks")
public class JWKController {

    static final Logger log = LoggerFactory.getLogger(JWKController.class);
    @Autowired
    RSAKeyRepository rsaRepo;
    
    @RequestMapping(value = "/jwk",method = RequestMethod.GET, produces = "application/json;")
    @ResponseBody
    public String jkw(HttpServletRequest req,  Model model) {
        log.info("Someone is calling the jwk endpoint!");
        log.info(req.getQueryString());
        return "{\n" +
                "\"keys\": [{\n" +
                "    \"kty\": \"RSA\",\n" +
                "    \"n\": \"1emJEYJebrnPAvrAf6FDCQAOldKF3W-LY8i91L3NvUPgrkKsPjjRO-g0B-sRqKsoWVaN8wZ2j0y-e2YX5-ig1k2bMmNHMgRGISf1rvgMEJA1k9RiGxWuMeWrP9Aa_nYEs7Wau5dCB0SelGCPHEjrHmHmIzfZGsJG_i1AZ7EKOER90cxQG3pG8tnQqWNordtxJ7Cqr2_jSAFb5zW--AV9D6xjlSTuk1V3uJbtEH4q2Zid8fA8aAwaNPvL7QbW5IhrZw_chGxD_z3wHb1VQFiyycVjI6LTTmzI4IB9Dkt6QS3jzxft-AkTsJ4250xbCYr2lWsbd1n1-E3uzjipOS5EGQ\",\n" +
                "    \"e\": \"AQAB\",\n" +
                "\"kid\": \"000000000000000001\"," +
                "\"alg\": \"RS256\",\n" +
                "\"use\": \"sig\"}]}";


    }

    @RequestMapping(value = {"/{kid}", ""}, method = RequestMethod.GET, produces = "application/json;")
    @ResponseBody
    public PublicKeys jkws(@PathVariable(name="kid", required=false) String kid, HttpServletRequest req,  Model model) throws JsonMappingException, JsonProcessingException, GeneralSecurityException, IOException {
    	ObjectMapper mapper = new ObjectMapper();
        log.info("Someone is calling the jwk endpoint!");
        log.info(req.getQueryString());
        PublicKeys keys = new PublicKeys();
        if(StringUtils.isNotBlank(kid)) {
        RSAKeyId id = new RSAKeyId(kid, true);
        
        	Optional<RSAKeyEntity> rsaKey = rsaRepo.findById(id);
        	if(rsaKey.isPresent()) {
        		RSAKeyEntity rsaKeyEntity = rsaKey.get();
        		keys.keys.add(mapper.readValue(OAuthUtils2.getPublicKeyJson(rsaKeyEntity), JWKPublicKey.class));
        		return keys;
        	}
        }
        List<RSAKeyEntity> entities = rsaRepo.findAll();
        entities.stream().filter(et -> et.getKid().getTool() && StringUtils.isNotBlank(et.getPublicKey()))
        .forEach(et -> {
			try {
				keys.keys.add(mapper.readValue(OAuthUtils2.getPublicKeyJson(et), JWKPublicKey.class));
			} catch (GeneralSecurityException | IOException e) {
				log.error(String.format("Unable to generate keys for entity: %s", et), e);
			}
		});
        return keys;
    }
    
    @Data
    public class PublicKeys {
    	List<JWKPublicKey> keys = new ArrayList<>();
    }

}
