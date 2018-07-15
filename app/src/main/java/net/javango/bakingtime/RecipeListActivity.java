package net.javango.bakingtime;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.javango.bakingtime.model.Recipe;

import java.util.List;

public class RecipeListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recipe_list);
        final RecipeAdapter recipeAdapter = new RecipeAdapter(this);
        recyclerView.setAdapter(recipeAdapter);
        RecipeRepo.getInstance().getRecipes().observe(this, new Observer<List<Recipe>>() {

            @Override
            public void onChanged(@Nullable List<Recipe> movies) {
                recipeAdapter.setData(movies);
            }
        });
        setTitle(R.string.app_name);
    }

    public static class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder> {

        private RecipeListActivity mParentActivity;
        private List<Recipe> mValues;

        private RecipeAdapter(RecipeListActivity parent) {
            mParentActivity = parent;
        }

        @Override
        public RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_recipe, parent, false);
            return new RecipeHolder(view);
        }

        @Override
        public void onBindViewHolder(final RecipeHolder holder, int position) {
            Recipe r = mValues.get(position);
            holder.name.setText(r.getName());
            holder.id = r.getId();
        }

        @Override
        public int getItemCount() {
            return mValues != null ? mValues.size() : 0;
        }

        class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private int id;
            private TextView name;

            RecipeHolder(View view) {
                super(view);
                name = (TextView) view.findViewById(R.id.recipe_name);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                Intent intent = StepListActivity.newIntent(mParentActivity, id);
                mParentActivity.startActivity(intent);
            }
        }

        public void setData(List<Recipe> data) {
            mValues = data;
            notifyDataSetChanged();
        }
    }
}
