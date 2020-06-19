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


import java.security.Principal;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.unicon.lti13demo.exceptions.ConnectionException;
import net.unicon.lti13demo.model.LtiContextEntity;
import net.unicon.lti13demo.model.PlatformDeployment;
import net.unicon.lti13demo.model.ags.LineItems;
import net.unicon.lti13demo.model.oauth2.Token;
import net.unicon.lti13demo.repository.LtiContextRepository;
import net.unicon.lti13demo.repository.PlatformDeploymentRepository;
import net.unicon.lti13demo.service.AdvantageAgsService;



/**
 * This LTI 3 redirect controller will retrieve the LTI3 requests and redirect them to the right page.
 * Everything that arrives here is filtered first by the LTI3OAuthProviderProcessingFilter
 */
@Controller
@Scope("session")
@RequestMapping(AgsController.AGS)
public class AgsController {

	public static final String AGS = "/ags";
	public static final String AGS_LINEITEM = "/lineitems";
	public static final String AGS_LINEITEM_SCORE = AGS_LINEITEM + "/score";
    static final Logger log = LoggerFactory.getLogger(MembershipController.class);

    @Autowired
    LtiContextRepository ltiContextRepository;

    @Autowired
    PlatformDeploymentRepository platformDeploymentRepository;

    @Autowired
    AdvantageAgsService advantageAgsService;

    @RequestMapping(value = AGS_LINEITEM, method = RequestMethod.GET)
    public String lineItemsGet(HttpServletRequest req, Principal principal, Model model) throws ConnectionException {

        //To keep this endpoint secured, we will only allow access to the course/platform stored in the session.
        //LTI Advantage services doesn't need a session to access to the membership, but we implemented this control here
        // to avoid access to all the courses and platforms.
        HttpSession session = req.getSession();
        if ((session.getAttribute("deployment_key") !=null) && (session.getAttribute("deployment_key") !=null)){
            model.addAttribute("noSessionValues", false);
            Long deployment = (Long) session.getAttribute("deployment_key");
            String contextId = (String) session.getAttribute("context_id");
            //We find the right deployment:
            Optional<PlatformDeployment> platformDeployment = platformDeploymentRepository.findById(deployment);
            if (platformDeployment.isPresent()) {
                //Get the context in the query
                LtiContextEntity context = ltiContextRepository.findByContextKeyAndPlatformDeployment(contextId, platformDeployment.get());

                //Call the membership service to get the users on the context
                // 1. Get the token
                Token token = advantageAgsService.getReadLineItemToken(platformDeployment.get());

                // 2. Call the service
                LineItems lineItems = advantageAgsService.callLineItemService(token, context);

                // 3. update the model
                model.addAttribute("results", lineItems.getLineItemList());
            }
        } else {
            model.addAttribute("noSessionValues", true);
        }
        return "ltiAdvLineItemMain";
    }
    
    @RequestMapping(value = AGS_LINEITEM_SCORE, method = RequestMethod.POST)
    public String scorePost(HttpServletRequest req, Principal principal, Model model) throws ConnectionException {

        //To keep this endpoint secured, we will only allow access to the course/platform stored in the session.
        //LTI Advantage services doesn't need a session to access to the membership, but we implemented this control here
        // to avoid access to all the courses and platforms.
        HttpSession session = req.getSession();
        if ((session.getAttribute("deployment_key") !=null) && (session.getAttribute("deployment_key") !=null)){
            model.addAttribute("noSessionValues", false);
            Long deployment = (Long) session.getAttribute("deployment_key");
            String contextId = (String) session.getAttribute("context_id");
            //We find the right deployment:
            Optional<PlatformDeployment> platformDeployment = platformDeploymentRepository.findById(deployment);
            if (platformDeployment.isPresent()) {
                //Get the context in the query
                LtiContextEntity context = ltiContextRepository.findByContextKeyAndPlatformDeployment(contextId, platformDeployment.get());

                //Call the membership service to get the users on the context
                // 1. Get the token
                Token token = advantageAgsService.getWriteScoreToken(platformDeployment.get());

                // 2. Call the service
                LineItems lineItems = advantageAgsService.callLineItemService(token, context);

                // 3. update the model
                model.addAttribute("results", lineItems.getLineItemList());
            }
        } else {
            model.addAttribute("noSessionValues", true);
        }
        return "ltiAdvAgsMain";
    }
    
    @RequestMapping(value = AGS_LINEITEM, method = RequestMethod.POST)
    public String lineItemPost(HttpServletRequest req, Principal principal, Model model) throws ConnectionException {

        //To keep this endpoint secured, we will only allow access to the course/platform stored in the session.
        //LTI Advantage services doesn't need a session to access to the membership, but we implemented this control here
        // to avoid access to all the courses and platforms.
        HttpSession session = req.getSession();
        if ((session.getAttribute("deployment_key") !=null) && (session.getAttribute("deployment_key") !=null)){
            model.addAttribute("noSessionValues", false);
            Long deployment = (Long) session.getAttribute("deployment_key");
            String contextId = (String) session.getAttribute("context_id");
            //We find the right deployment:
            Optional<PlatformDeployment> platformDeployment = platformDeploymentRepository.findById(deployment);
            if (platformDeployment.isPresent()) {
                //Get the context in the query
                LtiContextEntity context = ltiContextRepository.findByContextKeyAndPlatformDeployment(contextId, platformDeployment.get());

                //Call the membership service to get the users on the context
                // 1. Get the token
                Token token = advantageAgsService.getWriteLineItemToken(platformDeployment.get());

                // 2. Call the service
                LineItems lineItems = advantageAgsService.callLineItemService(token, context);

                // 3. update the model
                model.addAttribute("results", lineItems.getLineItemList());
            }
        } else {
            model.addAttribute("noSessionValues", true);
        }
        return "ltiAdvAgsMain";
    }


}
