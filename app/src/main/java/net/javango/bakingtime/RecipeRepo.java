package net.javango.bakingtime;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import net.javango.bakingtime.model.Recipe;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class RecipeRepo {

    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/";
    private static RecipeRepo repo = new RecipeRepo();

    private RecipeService service;
    // simple cache
    private MutableLiveData<List<Recipe>> recipes;
    private Map<Integer, Recipe> recipeMap = new LinkedHashMap<>();

    private RecipeRepo() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(RecipeService.class);
    }

    private interface RecipeService {
        @GET("2017/May/59121517_baking/baking.json")
        Call<List<Recipe>> getRecipes();
    }

    public static RecipeRepo getInstance() {
        return repo;
    }

    public LiveData<List<Recipe>> getRecipes() {
        if (recipes != null)
            return recipes;

        recipes = new MutableLiveData<>();
        service.getRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                List<Recipe> list = response.body();
                for (Recipe r : list)
                    recipeMap.put(r.getId(), r);

                recipes.setValue(list);
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                // nothing for now
            }
        });
        return recipes;
    }

    public Recipe getRecipe(int id) {
        return recipeMap.get(id);
    }

    public Recipe getRecipeSync(int id) {
        // use caching if possible
        Recipe recipe = recipeMap.get(id);
        if (recipe != null)
            return recipe;

        List<Recipe> list = null;
        try {
            list = service.getRecipes().execute().body();
        } catch (IOException e) {
            return null;
        }
        for (Recipe r : list) {
            if (r.getId() == id)
                return r;
        }

        return null;
    }

    public void getRecipesSync() {
        try {
            Response<List<Recipe>> res = service.getRecipes().execute();
            List<Recipe> list = res.body();
            for (Recipe r : list)
                recipeMap.put(r.getId(), r);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
