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
package net.unicon.lti13demo.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import net.unicon.lti13demo.client.MessageConverterHtml;
import net.unicon.lti13demo.client.RestTemplateResponseErrorHandler;
import net.unicon.lti13demo.exceptions.ConnectionException;
import net.unicon.lti13demo.exceptions.helper.ExceptionMessageGenerator;
import net.unicon.lti13demo.model.PlatformDeployment;
import net.unicon.lti13demo.model.oauth2.Token;

@Component
public class AdvantageConnectorHelper {

    @Autowired
    LTIJWTService ltijwtService;

    @Autowired
    private ExceptionMessageGenerator exceptionMessageGenerator;

    static final Logger log = LoggerFactory.getLogger(AdvantageConnectorHelper.class);


    public HttpEntity createRequestEntity(String apiKey) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey);
        HttpEntity request = new HttpEntity<>(headers);
        return request;
    }

    // We put the token in the Authorization as a simple Bearer one.
    public HttpEntity createTokenizedRequestEntity(Token token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccess_token());
        HttpEntity request = new HttpEntity<>(headers);
        return request;
    }

    //Asking for a token. The scope will come in the scope parameter
    //The platformDeployment has the URL to ask for the token.
    public Token getToken(PlatformDeployment platformDeployment, String scope) throws ConnectionException {
        Token token = null;
        try {
            RestTemplate restTemplate = createRestTemplate();
            // We need an specific request for the token.
            HttpEntity request = createTokenRequest(scope, platformDeployment);
            final String POST_TOKEN_URL = platformDeployment.getoAuth2TokenUrl();
            log.debug("POST_TOKEN_URL -  "+ POST_TOKEN_URL);
            ResponseEntity<Token> reportPostResponse = restTemplate.
                    postForEntity(POST_TOKEN_URL, request, Token.class);
            
            if (reportPostResponse != null) {
                HttpStatus status = reportPostResponse.getStatusCode();
                if (status.is2xxSuccessful()) {
                    token = reportPostResponse.getBody();
                } else {
                    String exceptionMsg = "Can not get the token, http status: %s";
                    log.error(String.format(exceptionMsg, status.toString()));
                    throw new ConnectionException(exceptionMsg);
                }
            } else {
                log.warn("Problem getting the token");
            }
        } catch (Exception e) {
            StringBuilder exceptionMsg = new StringBuilder();
            exceptionMsg.append("Can get the token");
            log.error(exceptionMsg.toString());
            throw new ConnectionException(exceptionMessageGenerator.exceptionMessage(exceptionMsg.toString(), e));
        }
        return token;
    }

    // This is specific to request a token.
    public HttpEntity createTokenRequest(String scope, PlatformDeployment platformDeployment) throws GeneralSecurityException, IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject parameterJson = new JSONObject();
        // The grant type is client credentials always
        parameterJson.put("grant_type", "client_credentials");
        // This is standard too
        parameterJson.put("client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer");
        //This is special (see the generateTokenRequestJWT method for more comments)
        String jwtToken = ltijwtService.generateTokenRequestJWT(platformDeployment);
        log.debug("jwt string createTokenRequest:", jwtToken);
        parameterJson.put("client_assertion", jwtToken);
        //We need to pass the scope of the token, meaning, the service we want to allow with this token.
        parameterJson.put("scope", scope);
        HttpEntity request = new HttpEntity<>(parameterJson.toString(), headers);
        return request;
    }

    public RestTemplate createRestTemplate() {
        RestTemplate restTemplate = new RestTemplate(
                new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
        messageConverters.add(converter);
        
        MessageConverterHtml errorConverter = new MessageConverterHtml();
        messageConverters.add(errorConverter);
        restTemplate.setMessageConverters(messageConverters);
        
        restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
        return restTemplate;
    }

    public String nextPage(HttpHeaders headers) {
        List<String> links = headers.get("link");
        if (CollectionUtils.isNotEmpty(links)) {
            String link = links.get(0);
            String[] tokens = StringUtils.split(link, ",");
            String url = indexOf(tokens, "rel=\"next\"");
            if (StringUtils.isNotEmpty(url)) {
                try {
                    return URLDecoder.decode(url, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public String indexOf(String[] tokens, String searchString) {
        for (String token : tokens) {
            if (StringUtils.contains(token, searchString)) {
                return StringUtils.substring(token, token.indexOf("<") + 1, token.indexOf(">"));
            }
        }
        return null;
    }
}