package net.javango.bakingtime;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import net.javango.bakingtime.model.Recipe;
import net.javango.bakingtime.model.Step;

import java.util.List;

/**
 * An activity representing a single Step detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link StepListActivity}.
 */
public class StepPagerActivity extends AppCompatActivity {

    static final String EXTRA_STEP_OBJ = "net.javango.bakingtime.step_obj";
    static final String EXTRA_RECIPE_ID = "net.javango.bakingtime.recipe_id";

    private ViewPager viewPager;
    private List<Step> steps;

    public static Intent newIntent(Context context, Step step, int recipeId) {
        Intent intent = new Intent(context, StepPagerActivity.class);
        intent.putExtra(EXTRA_STEP_OBJ, step);
        intent.putExtra(EXTRA_RECIPE_ID, recipeId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_pager);
        final Step step = (Step) getIntent().getSerializableExtra(EXTRA_STEP_OBJ);
        int rid = getIntent().getIntExtra(EXTRA_RECIPE_ID, -1);

        viewPager = (ViewPager) findViewById(R.id.step_view_pager);
        Recipe recipe = RecipeRepo.getInstance().getRecipe(rid);
        steps = recipe.getSteps();
        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return StepFragment.newInstance(steps.get(position));
            }

            @Override
            public int getCount() {
                return steps.size();
            }
        });

        viewPager.setCurrentItem(step.getId());
        getSupportActionBar().setTitle(step.getShortDescription());
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                String title = steps.get(position).getShortDescription();
                getSupportActionBar().setTitle(title);
            }
        });
    }

}
