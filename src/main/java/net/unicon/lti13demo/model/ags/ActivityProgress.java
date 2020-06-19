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

public enum ActivityProgress {
	Initialized,
	Started,
	InProgress,
	Submitted,
	Completed
}

/*
 * 'Initialized' – the user has not started the activity, or the activity has been reset for that student.
'Started' – the activity associated with the line item has been started by the user to which the result relates.
'InProgress' - the activity is being drafted and is available for comment.
'Submitted' - the activity has been submitted at least once by the user but the user is still able make further submissions.
'Completed' – the user has completed the activity associated with the line item.
*/
