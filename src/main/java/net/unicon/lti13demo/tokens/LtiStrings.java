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
package net.unicon.lti13demo.tokens;

public class LtiStrings {



    //Those are used by the session.
    public static final String LTI_SESSION_USER_ID = "user_id";
    public static final String LTI_SESSION_USER_ROLE = "user_role";
    public static final String LTI_SESSION_CONTEXT_ID = "context_id";
    public static final String LTI_SESSION_DEPLOYMENT_KEY = "deployment_key";

    //BASICS
    public static final String LTI_SCOPE = "scope";
    public static final String LTI_MESSAGE_TYPE = "https://purl.imsglobal.org/spec/lti/claim/message_type";
    public static final String LTI_VERSION = "https://purl.imsglobal.org/spec/lti/claim/version";
    public static final String LTI_DEPLOYMENT_ID = "https://purl.imsglobal.org/spec/lti/claim/deployment_id";

    //RESOURCE_LINK
    public static final String LTI_LINK = "https://purl.imsglobal.org/spec/lti/claim/resource_link";
    public static final String LTI_LINK_ID = "id";
    public static final String LTI_LINK_DESC = "description";
    public static final String LTI_LINK_TITLE = "title";

 
    //CONTEXT
    public static final String LTI_CONTEXT = "https://purl.imsglobal.org/spec/lti/claim/context";
    public static final String LTI_CONTEXT_ID = "id";
    public static final String LTI_CONTEXT_TYPE = "type";
    public static final String LTI_CONTEXT_LABEL = "label";
    public static final String LTI_CONTEXT_TITLE = "title";
    public static final String LTI_CONTEXT_TYPE_COURSE_TEMPLATE = "http://purl.imsglobal.org/vocab/lis/v2/course#CourseTemplate";
    public static final String LTI_CONTEXT_TYPE_COURSE_OFFERING = "http://purl.imsglobal.org/vocab/lis/v2/course#CourseOffering";
    public static final String LTI_CONTEXT_TYPE_COURSE_SECTION = "http://purl.imsglobal.org/vocab/lis/v2/course#CourseSection";
    public static final String LTI_CONTEXT_TYPE_GROUP = "http://purl.imsglobal.org/vocab/lis/v2/course#Group";

    //PLATFORM
    public static final String LTI_PLATFORM = "https://purl.imsglobal.org/spec/lti/claim/tool_platform";
    public static final String LTI_PLATFORM_GUID = "guid";
    public static final String LTI_PLATFORM_CONTACT_EMAIL = "contact_email";
    public static final String LTI_PLATFORM_DESC = "description";
    public static final String LTI_PLATFORM_NAME = "name";
    public static final String LTI_PLATFORM_URL = "url";
    public static final String LTI_PLATFORM_PRODUCT = "product_family_code";
    public static final String LTI_PLATFORM_PRODUCT_FAMILY_CODE = "product_family_code";
    public static final String LTI_PLATFORM_VERSION = "version";

    //LAUNCH_PRESENTATION
    public static final String LTI_LAUNCH_PRESENTATION = "https://purl.imsglobal.org/spec/lti/claim/launch_presentation";
    public static final String LTI_PRES_TARGET = "document_target";
    public static final String LTI_PRES_WIDTH = "width";
    public static final String LTI_PRES_HEIGHT = "height";
    public static final String LTI_PRES_RETURN_URL = "return_url";
    public static final String LTI_PRES_LOCALE = "locale";
    public static final String LTI_PRES_RETURN_URL_PARAMETER_ERROR_MSG = "_ltierrormsg";
    public static final String LTI_PRES_RETURN_URL_PARAMETER_MSG = "_ltimsg";
    public static final String LTI_PRES_RETURN_URL_PARAMETER_ERROR_LOG = "_ltierrorlog";
    public static final String LTI_PRES_RETURN_URL_PARAMETER_LOG = "_ltilog";

    //LIS
    public static final String LTI_LIS ="https://purl.imsglobal.org/spec/lti/claim/lis";
    public static final String LTI_PERSON_SOURCEDID = "person_sourcedid";
    public static final String LTI_COURSE_OFFERING_SOURCEDID = "course_offering_sourcedid";
    public static final String LTI_COURSE_SECTION_SOURCEDID = "course_section_sourcedid";

    //CUSTOM AND EXTENSION TEST
    public static final String LTI_EXTENSION = "https://www.example.com/extension";
    public static final String LTI_CUSTOM = "https://purl.imsglobal.org/spec/lti/claim/custom";

    //OTHERS
    public static final String LTI_NAME = "name";
    public static final String LTI_GIVEN_NAME = "given_name";
    public static final String LTI_FAMILY_NAME = "family_name";
    public static final String LTI_MIDDLE_NAME = "middle_name";
    public static final String LTI_PICTURE = "picture";
    public static final String LTI_EMAIL = "email";
    public static final String LTI_LOCALE = "locale";
    public static final String LTI_AZP = "azp";
    public static final String LTI_ENDPOINT = "https://purl.imsglobal.org/spec/lti-ags/claim/endpoint";
    public static final String LTI_ENDPOINT_SCOPE = "scope";
    public static final String LTI_ENDPOINT_LINEITEMS = "lineitems";
    public static final String LTI_NAMES_ROLE_SERVICE = "https://purl.imsglobal.org/spec/lti-nrps/claim/namesroleservice";
    public static final String LTI_NAMES_ROLE_SERVICE_CONTEXT = "context_memberships_url";
    public static final String LTI_NAMES_ROLE_SERVICE_VERSIONS = "service_versions";

    public static final String LTI_CALIPER_ENDPOINT_SERVICE = "https://purl.imsglobal.org/spec/lti-ces/claim/caliper-endpoint-service";
    public static final String LTI_CALIPER_ENDPOINT_SERVICE_SCOPES = "scopes";
    public static final String LTI_CALIPER_ENDPOINT_SERVICE_URL = "caliper_endpoint_url";
    public static final String LTI_CALIPER_ENDPOINT_SERVICE_SESSION_ID = "caliper_federated_session_id";

    public static final String LTI_11_LEGACY_USER_ID = "https://purl.imsglobal.org/spec/lti/claim/lti11_legacy_user_id";
    public static final String LTI_NONCE = "nonce";

    public static final String LTI_CONSUMER_KEY = "oauth_consumer_key";

    public static final String LTI_MESSAGE_TYPE_RESOURCE_LINK = "LtiResourceLinkRequest";
    public static final String LTI_MESSAGE_TYPE_DEEP_LINKING = "LtiDeepLinkingRequest";
    public static final String LTI_MESSAGE_TYPE_DEEP_LINKING_RESPONSE = "LtiDeepLinkingResponse";
    public static final String LTI_VERSION_3 = "1.3.0";
    public static final String LTI_TARGET_LINK_URI = "https://purl.imsglobal.org/spec/lti/claim/target_link_uri";
    
    
    


    

}
