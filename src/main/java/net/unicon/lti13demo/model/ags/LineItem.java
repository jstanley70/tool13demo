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

package net.unicon.lti13demo.model.ags;

import lombok.Data;

@Data
public class LineItem {
	private String timestamp;
	private String scoreGiven;
	private String scoreMaximum;
	private String comment;
	private String activityProgress;
	private String gradingProgress;
	private String userId;
}

/*
 * {
  "timestamp": "2017-04-16T18:54:36.736+00:00",
  "scoreGiven" : 83,
  "scoreMaximum" : 100,
  "comment" : "This is exceptional work.",
  "activityProgress" : "Completed",
  "gradingProgress": "FullyGraded",
  "userId" : "5323497"
}

valid timestamps to be supported and tested:
"timestamp": "2017-04-16T18:54:36.736+00:00"
  "timestamp": "2017-04-16T18:54:36.736Z"
  "timestamp": "2017-04-16T18:54:36.736+00"
*/
