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

public enum GradingProgress {
	FullyGraded,
	Pending,
	PendingManual,
	Failed,
	NotReady
}

/*
 * 'FullyGraded' - The grading process is completed; the score value, if any, represents the current Final Grade;
'Pending' – Final Grade is pending, but does not require manual intervention; if a Score value is present, it indicates the current value is partial and may be updated.
'PendingManual' – Final Grade is pending, and it does require human intervention; if a Score value is present, it indicates the current value is partial and may be updated during the manual grading.
'Failed' - The grading could not complete.
'NotReady' - There is no grading process occurring; for example, the student has not yet made any submission.
*/
