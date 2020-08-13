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
package net.unicon.lti13demo.utils.lti;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ClientArrayUtils {

	static final Logger log = LoggerFactory.getLogger(ClientArrayUtils.class);
	public static List<Map<String, Object>> getAll(RestTemplate restTemplate, String url, HttpEntity entity, String maxPageSize) {
		List<Map<String, Object>> objects = new ArrayList<Map<String, Object>>();
		//even though this is not optimal, to be safe, all endpoints are guaranteed to bring back a default page size of 10
		String nextUrl = url + "&per_page=" + maxPageSize;
		do {
			nextUrl = get(restTemplate, objects, nextUrl, entity);
		} while (StringUtils.isNotBlank(nextUrl));
		return objects;
	}

	public static String get(RestTemplate restTemplate, List<Map<String, Object>> objects, String url, HttpEntity entity) {
		
		log.debug("Attempting standard REST call. Using " + HttpMethod.GET + " call to service URL: " + url);
		ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);
		log.debug("Response received [{}] from REST call to service URL: {}", response.getStatusCode(), url);
		List<Map<String, Object>> records = (List<Map<String, Object>>) response.getBody();

		if (records != null && !records.isEmpty()) {
			
			objects.addAll(records);
			log.debug("Records for url {}, total length: {}", url, objects.size());
		}

		return nextPage(response, entity);

	}
	
	private static String nextPage(ResponseEntity<?> response, HttpEntity entity) {
		if (response.getHeaders().containsKey("Link")) {
			return extractURIByRel(response.getHeaders().get("Link").get(0), "next", entity);
		}
		log.debug("No Link: found");
		return null;
	}
	
	public static String extractURIByRel(final String linkHeader, final String rel, final HttpEntity entity) {
		if (linkHeader == null) {
			return null;
		}

		String uriWithSpecifiedRel = null;
		final String[] links = linkHeader.split(",");
		String linkRelation = null;
		for (final String link : links) {
			final int positionOfSeparator = link.indexOf(';');
			linkRelation = link.substring(positionOfSeparator + 1, link.length()).trim();
			if (extractTypeOfRelation(linkRelation).equals(rel)) {
				log.debug("Link: next page: {}, position of separator {}", link, positionOfSeparator);
				uriWithSpecifiedRel = link.substring(1, positionOfSeparator - 1);
				break;
			}
		}
		
		log.debug("Link: for next page: {}", uriWithSpecifiedRel);
		return uriWithSpecifiedRel;
	}

	private static Object extractTypeOfRelation(final String linkRelation) {
		final int positionOfEquals = linkRelation.indexOf('=');
		return linkRelation.substring(positionOfEquals + 2, linkRelation.length() - 1).trim();
	}
}
