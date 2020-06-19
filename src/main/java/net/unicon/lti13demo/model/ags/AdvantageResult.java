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
public class AdvantageResult {
	private String id;
	private String scoreOf;
	private String userId;
	private String resultScore;
	private String resultMaximum;
	private String comment;
}


/*

id": "https://lms.example.com/context/2923/lineitems/1/results/5323497",
  "scoreOf": "https://lms.example.com/context/2923/lineitems/1",
  "userId": "5323497",
  "resultScore": 0.83,
  "resultMaximum": 1, default:1 must be greater than 0
  "comment": "This is exceptional work."

*/