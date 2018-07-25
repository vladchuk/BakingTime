/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package net.javango.bakingtime;


import android.app.ListActivity;
import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import net.javango.bakingtime.model.Recipe;
import net.javango.bakingtime.model.Step;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

@RunWith(AndroidJUnit4.class)
public class StepListActivityTest {

    @Rule
    // don't start the activity
    public ActivityTestRule<StepListActivity> testRule = new ActivityTestRule<>(StepListActivity.class, false, false);

    @Before
    public void setUp() {
      RecipeRepo.getInstance().getRecipes();
    }

    @Test
    public void clickItem_OpensStepDetail() {
        Intent intent = StepListActivity.newFillInIntent(1);
        testRule.launchActivity(intent);

        onView(withId(R.id.step_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // check correct step by description
        onView(allOf(withId(R.id.step_detail), isDisplayingAtLeast(20))).check(matches(withText(containsString("Recipe Introduction"))));

    }

}