package net.javango.bakingtime;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.javango.bakingtime.model.Ingredient;
import net.javango.bakingtime.model.Recipe;
import net.javango.bakingtime.model.Step;

import java.util.List;

/**
 * An activity representing a list of Steps. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StepPagerActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class StepListActivity extends AppCompatActivity {

    private static final String EXTRA_RECIPE_ID = "net.javango.bakingtime.recipe_id";

    private boolean mTwoPane;
    private Recipe recipe;
    private ViewGroup ingredientLayout;

    public static Intent newIntent(Context context, int recipeId) {
        Intent intent = new Intent(context, StepListActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, recipeId);
        return intent;
    }

    public static Intent newFillInIntent(int recipeId) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_RECIPE_ID, recipeId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.step_detail_container) != null) {
            // use two-pane mode for tablets
            mTwoPane = true;
        }

        RecyclerView recyclerView = findViewById(R.id.step_list);
        int recipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, -1);
        recipe = RecipeRepo.getInstance().getRecipe(recipeId);
        recyclerView.setAdapter(new StepAdapter(this, recipe.getSteps(), mTwoPane, recipeId));
        setTitle(recipe.getName());
        ingredientLayout = findViewById(R.id.ingredient_list);
        populateIngredients();
    }

    private void populateIngredients() {
        for (Ingredient ingr : recipe.getIngredients()) {
            TextView view =  new TextView(this);
            String text = ingr.getName() + ", " + ingr.getQuantity() + " " + ingr.getMeasure();
            view.setText(text);
            ingredientLayout.addView(view);
        }
    }

    public static class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepHolder> {

        private final StepListActivity mParentActivity;
        private final List<Step> steps;
        private final boolean mTwoPane;
        private int recipeId;

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Step step = (Step) view.getTag();
                if (mTwoPane) {
                    StepFragment fragment = StepFragment.newInstance(step);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.step_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = StepPagerActivity.newIntent(context, step, recipeId);
                    context.startActivity(intent);
                }
            }
        };

        private StepAdapter(StepListActivity parent, List<Step> steps, boolean twoPane, int recipeId) {
            this.steps = steps;
            mParentActivity = parent;
            mTwoPane = twoPane;
            this.recipeId = recipeId;
        }

        @Override
        public StepHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_step, parent, false);
            return new StepHolder(view);
        }

        @Override
        public void onBindViewHolder(StepHolder holder, int position) {
            Step step = steps.get(position);
            if (step.getId() > 0)
                holder.mIdView.setText(String.valueOf(step.getId()));
            else
                holder.mIdView.setVisibility(View.GONE);
            holder.mDscrView.setText(step.getShortDescription());
            holder.itemView.setTag(step);
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return steps.size();
        }

        class StepHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mDscrView;

            private StepHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mDscrView = (TextView) view.findViewById(R.id.step_short_descr);
            }
        }
    }
}
