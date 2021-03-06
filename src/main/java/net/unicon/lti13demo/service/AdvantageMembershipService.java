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

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.AsymmetricJWK;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.SigningKeyResolverAdapter;
import net.minidev.json.JSONObject;
import net.unicon.lti13demo.exceptions.ConnectionException;
import net.unicon.lti13demo.exceptions.helper.ExceptionMessageGenerator;
import net.unicon.lti13demo.model.LtiContextEntity;
import net.unicon.lti13demo.model.PlatformDeployment;
import net.unicon.lti13demo.model.RSAKeyEntity;
import net.unicon.lti13demo.model.RSAKeyId;
import net.unicon.lti13demo.model.membership.CourseUser;
import net.unicon.lti13demo.model.membership.CourseUsers;
import net.unicon.lti13demo.model.oauth2.Token;
import net.unicon.lti13demo.repository.LtiContextRepository;
import net.unicon.lti13demo.utils.oauth.OAuthUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.PublicKey;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * This manages all the Membership call for the LTIRequest (and for LTI in general)
 * Necessary to get appropriate TX handling and service management
 */
@Component
public class AdvantageMembershipService {

    @Autowired
    AdvantageConnectorHelper advantageConnectorHelper;

    @Autowired
    private ExceptionMessageGenerator exceptionMessageGenerator;

    static final Logger log = LoggerFactory.getLogger(AdvantageMembershipService.class);

    //Asking for a token with the right scope.
    public Token getToken(PlatformDeployment platformDeployment) throws ConnectionException {
        String scope = "https://purl.imsglobal.org/spec/lti-nrps/scope/contextmembership.readonly";
        return advantageConnectorHelper.getToken(platformDeployment, scope);
    }

    //Calling the membership service and getting a paginated result of users.
    public CourseUsers callMembershipService(Token token, LtiContextEntity context) throws ConnectionException {
        CourseUsers courseUsers = new CourseUsers();
        log.debug("Token -  "+ token.getAccess_token());
        try {
            RestTemplate restTemplate = advantageConnectorHelper.createRestTemplate();
            //We add the token in the request with this.
            HttpEntity request = advantageConnectorHelper.createTokenizedRequestEntity(token);
            //The URL to get the course contents is stored in the context (in our database) because it came
            // from the platform when we created the link to the context, and we saved it then.
            final String GET_MEMBERSHIP = context.getContext_memberships_url();
            log.debug("GET_MEMBERSHIP -  "+ GET_MEMBERSHIP);
            ResponseEntity<CourseUsers> membershipGetResponse = restTemplate.
                    exchange(GET_MEMBERSHIP, HttpMethod.GET, request, CourseUsers.class);
            List<CourseUser> courseUserList = new ArrayList<>();
            if (membershipGetResponse != null) {
                HttpStatus status = membershipGetResponse.getStatusCode();
                if (status.is2xxSuccessful()) {
                    courseUsers = membershipGetResponse.getBody();
                    courseUserList.addAll(courseUsers.getCourseUserList());
                    //We deal here with pagination
                    log.debug("We have {} users",courseUsers.getCourseUserList().size());
                    String nextPage = advantageConnectorHelper.nextPage(membershipGetResponse.getHeaders());
                    log.debug("We have next page: " + nextPage);
                    while (nextPage != null) {
                        ResponseEntity<CourseUsers> responseForNextPage = restTemplate.exchange(nextPage, HttpMethod.GET,
                                request, CourseUsers.class);
                        CourseUsers nextCourseList = responseForNextPage.getBody();
                        List<CourseUser> nextCourseUsersList = nextCourseList
                                .getCourseUserList();
                        log.debug("We have {} users in the next page",nextCourseList.getCourseUserList().size());
                        courseUserList.addAll(nextCourseUsersList);
                        nextPage = advantageConnectorHelper.nextPage(responseForNextPage.getHeaders());
                    }
                    courseUsers = new CourseUsers();
                    courseUsers.getCourseUserList().addAll(courseUserList);
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
        return courseUsers;
    }



}
