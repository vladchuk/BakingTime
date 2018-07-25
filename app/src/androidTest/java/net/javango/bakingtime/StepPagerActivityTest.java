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


import android.content.Context;
import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import net.javango.bakingtime.model.Recipe;
import net.javango.bakingtime.model.Step;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.hasFocus;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static net.javango.bakingtime.StepPagerActivity.EXTRA_RECIPE_ID;
import static net.javango.bakingtime.StepPagerActivity.EXTRA_STEP_OBJ;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

@RunWith(AndroidJUnit4.class)
public class StepPagerActivityTest {

    @Rule
    // don't start activity
    public ActivityTestRule<StepPagerActivity> testRule = new ActivityTestRule<>(StepPagerActivity.class, false,  false);

    @Before
    public void setUp() {
        RecipeRepo.getInstance().getRecipes();
    }

    public static Intent newIntent(Step step, int recipeId) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_STEP_OBJ, step);
        intent.putExtra(EXTRA_RECIPE_ID, recipeId);
        return intent;
    }

    @Test
    public void testSwipe() {
        Recipe recipe = RecipeRepo.getInstance().getRecipeSync(1);
        Step step = recipe.getSteps().get(0);
        Intent intent = newIntent(step, recipe.getId());
        testRule.launchActivity(intent);

        // Locate the ViewPager and perform a swipe right action
        onView(withId(R.id.step_view_pager)).perform(swipeLeft());

        // Check next step is displayed
        onView(allOf(withId(R.id.step_detail), isDisplayingAtLeast(20))).check(matches(withText(containsString("Preheat the oven"))));
    }

}