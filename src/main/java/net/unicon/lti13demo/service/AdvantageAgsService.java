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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import net.unicon.lti13demo.exceptions.ConnectionException;
import net.unicon.lti13demo.exceptions.helper.ExceptionMessageGenerator;
import net.unicon.lti13demo.model.LtiContextEntity;
import net.unicon.lti13demo.model.PlatformDeployment;
import net.unicon.lti13demo.model.ags.LineItem;
import net.unicon.lti13demo.model.ags.LineItems;
import net.unicon.lti13demo.model.oauth2.Token;
import net.unicon.lti13demo.tokens.AgsTokens;


/**
 * This manages all the Membership call for the LTIRequest (and for LTI in general)
 * Necessary to get appropriate TX handling and service management
 */
@Component
public class AdvantageAgsService {

    @Autowired
    AdvantageConnectorHelper advantageConnectorHelper;

    @Autowired
    private ExceptionMessageGenerator exceptionMessageGenerator;

    static final Logger log = LoggerFactory.getLogger(AdvantageAgsService.class);

    //Asking for a token with the right scope.
    public Token getReadLineItemToken(PlatformDeployment platformDeployment) throws ConnectionException {
        String scope = AgsTokens.AGS_SCOPE_LINE_ITEM;
        Token token=  advantageConnectorHelper.getToken(platformDeployment, scope);
        if(token != null) {
        	return token;
        }
        scope = AgsTokens.AGS_SCOPE_RESULT_READ_ONLY;
        return advantageConnectorHelper.getToken(platformDeployment, scope);
    }
    
    //Asking for a token with the right scope.
    public Token getWriteScoreToken(PlatformDeployment platformDeployment) throws ConnectionException {
        String scope = AgsTokens.AGS_SCOPE_SCORE;
        return advantageConnectorHelper.getToken(platformDeployment, scope);
    }
    
    //Asking for a token with the right scope.
    public Token getWriteLineItemToken(PlatformDeployment platformDeployment) throws ConnectionException {
    	 String scope = AgsTokens.AGS_SCOPE_LINE_ITEM;
         return advantageConnectorHelper.getToken(platformDeployment, scope);
    }

    //Calling the grade service and getting a paginated result of results.
    public LineItems callLineItemService(Token token, LtiContextEntity context) throws ConnectionException {
        LineItems lineItemUsers = new LineItems();
        log.debug("Token -  "+ token.getAccess_token());
        try {
            RestTemplate restTemplate = advantageConnectorHelper.createRestTemplate();
            //We add the token in the request with this.
            HttpEntity request = advantageConnectorHelper.createTokenizedRequestEntity(token);
            //The URL to get the lineItem contents is stored in the context (in our database) because it came
            // from the platform when we created the link to the context, and we saved it then.
            final String GET_LINEITEM = context.getLineitems();
            log.debug("GET_LINEITEM -  "+ GET_LINEITEM);
            ResponseEntity<LineItems> membershipGetResponse = restTemplate.
                    exchange(GET_LINEITEM, HttpMethod.GET, request, LineItems.class);
            List<LineItem> lineItemList = new ArrayList<>();
            if (membershipGetResponse != null) {
                HttpStatus status = membershipGetResponse.getStatusCode();
                if (status.is2xxSuccessful()) {
                    lineItemUsers = membershipGetResponse.getBody();
                    lineItemList.addAll(lineItemUsers.getLineItemList());
                    //We deal here with pagination
                    log.debug("We have {} users",lineItemUsers.getLineItemList().size());
                    String nextPage = advantageConnectorHelper.nextPage(membershipGetResponse.getHeaders());
                    log.debug("We have next page: " + nextPage);
                    while (nextPage != null) {
                        ResponseEntity<LineItems> responseForNextPage = restTemplate.exchange(nextPage, HttpMethod.GET,
                                request, LineItems.class);
                        LineItems newLineItemList = responseForNextPage.getBody();
                        List<LineItem> nextLineItemsList = newLineItemList
                                .getLineItemList();
                        log.debug("We have {} users in the next page",newLineItemList.getLineItemList().size());
                        lineItemList.addAll(nextLineItemsList);
                        nextPage = advantageConnectorHelper.nextPage(responseForNextPage.getHeaders());
                    }
                    lineItemUsers = new LineItems();
                    lineItemUsers.getLineItemList().addAll(lineItemList);
                } else {
                    String exceptionMsg = "Can get the membership";
                    log.error(exceptionMsg);
                    throw new ConnectionException(exceptionMsg);
                }
            } else {
                log.warn("Problem getting the membership");
            }
        } catch (Exception e) {
            StringBuilder exceptionMsg = new StringBuilder();
            exceptionMsg.append("Can get the token");
            log.error(exceptionMsg.toString(),e);
            e.printStackTrace();
            throw new ConnectionException(exceptionMessageGenerator.exceptionMessage(exceptionMsg.toString(), e));
        }
        return lineItemUsers;
    }
    
    //Calling the grade service and getting a paginated result of results.
    public LineItems callScoreService(Token token, LtiContextEntity context) throws ConnectionException {
        LineItems lineItemUsers = new LineItems();
        log.debug("Token -  "+ token.getAccess_token());
        try {
            RestTemplate restTemplate = advantageConnectorHelper.createRestTemplate();
            //We add the token in the request with this.
            HttpEntity request = advantageConnectorHelper.createTokenizedRequestEntity(token);
            //The URL to get the lineItem contents is stored in the context (in our database) because it came
            // from the platform when we created the link to the context, and we saved it then.
            final String GET_LINEITEM = context.getLineitems();
            log.debug("GET_LINEITEM -  "+ GET_LINEITEM);
            ResponseEntity<LineItems> membershipGetResponse = restTemplate.
                    exchange(GET_LINEITEM, HttpMethod.GET, request, LineItems.class);
            List<LineItem> lineItemList = new ArrayList<>();
            if (membershipGetResponse != null) {
                HttpStatus status = membershipGetResponse.getStatusCode();
                if (status.is2xxSuccessful()) {
                    lineItemUsers = membershipGetResponse.getBody();
                    lineItemList.addAll(lineItemUsers.getLineItemList());
                    //We deal here with pagination
                    log.debug("We have {} users",lineItemUsers.getLineItemList().size());
                    String nextPage = advantageConnectorHelper.nextPage(membershipGetResponse.getHeaders());
                    log.debug("We have next page: " + nextPage);
                    while (nextPage != null) {
                        ResponseEntity<LineItems> responseForNextPage = restTemplate.exchange(nextPage, HttpMethod.GET,
                                request, LineItems.class);
                        LineItems newLineItemList = responseForNextPage.getBody();
                        List<LineItem> nextLineItemsList = newLineItemList
                                .getLineItemList();
                        log.debug("We have {} users in the next page",newLineItemList.getLineItemList().size());
                        lineItemList.addAll(nextLineItemsList);
                        nextPage = advantageConnectorHelper.nextPage(responseForNextPage.getHeaders());
                    }
                    lineItemUsers = new LineItems();
                    lineItemUsers.getLineItemList().addAll(lineItemList);
                } else {
                    String exceptionMsg = "Can get the membership";
                    log.error(exceptionMsg);
                    throw new ConnectionException(exceptionMsg);
                }
            } else {
                log.warn("Problem getting the membership");
            }
        } catch (Exception e) {
            StringBuilder exceptionMsg = new StringBuilder();
            exceptionMsg.append("Can get the token");
            log.error(exceptionMsg.toString(),e);
            e.printStackTrace();
            throw new ConnectionException(exceptionMessageGenerator.exceptionMessage(exceptionMsg.toString(), e));
        }
        return lineItemUsers;
    }



}
