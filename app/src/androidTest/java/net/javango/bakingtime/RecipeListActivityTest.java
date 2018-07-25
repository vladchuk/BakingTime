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


import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;
import android.widget.Toolbar;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class RecipeListActivityTest {

    public static final String RECIPE_NAME = "Brownies";

    @Rule
    public ActivityTestRule<RecipeListActivity> testRule = new ActivityTestRule<>(RecipeListActivity.class);

    /**
     * Clicks on list item and checks it opens up the StepListActivity with the correct title.
     */
    @Test
    public void clickItem_OpensStepsActivity() {
        onView(withId(R.id.recipe_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        // check correct activity by the title
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withText(RECIPE_NAME)).check(matches(withParent(withId(R.id.toolbar))));
    }

}