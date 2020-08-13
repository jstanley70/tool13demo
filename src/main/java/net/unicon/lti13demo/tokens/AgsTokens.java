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

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;

public class AgsTokens {

	public static final String AGS_SCOPE_LINE_ITEM = "https://purl.imsglobal.org/spec/lti-ags/scope/lineitem";
	public static final String AGS_SCOPE_LINE_ITEM_READ_ONLY = "https://purl.imsglobal.org/spec/lti-ags/scope/lineitem.readonly";
	public static final String AGS_SCOPE_RESULT_READ_ONLY = "https://purl.imsglobal.org/spec/lti-ags/scope/result.readonly";
	public static final String AGS_SCOPE_SCORE = "https://purl.imsglobal.org/spec/lti-ags/scope/score";

	public static final String AGS_LINE_ITEM = "lineitem";
	public static final String AGS_LINE_ITEMS = "lineitems";
	public static final String AGS_LINK_ID_FILTER = "resource_link_id";
	public static final String AGS_RESOURCE_ID_FILTER = "resource_id";
	public static final String AGS_TAG_FILTER = "tag";
	public static final String AGS_LIMIT_FILTER = "limit";

	public static final String AGS_LINE_ITEM_APPLICATION_MEDIA_TYPE = "application/vnd.ims.lis.v2.lineitemcontainer+json";
	public static final String AGS_LINE_ITEM_MEDIA_TYPE = "vnd.ims.lis.v2.lineitemcontainer+json";
	public static final String AGS_LINE_ITEM_MEDIA_TYPE_JSON_LD = "http://purl.imsglobal.org/ctx/lis/v2/outcomes/LineItemContainer";
	public static final String AGS_LINE_ITEM_MEDIA_TYPE_RDF_TYPE = "http://purl.imsglobal.org/vocab/lis/v2/outcomes#LineItemContainer";

	public static final List<MediaType> getAgsMediaTypes() {
		List<MediaType> supportedMediaTypes = new ArrayList<>();
		supportedMediaTypes.add(new MediaType("application", AgsTokens.AGS_LINE_ITEM_MEDIA_TYPE, StandardCharsets.UTF_8));
		return supportedMediaTypes;
	}
}
