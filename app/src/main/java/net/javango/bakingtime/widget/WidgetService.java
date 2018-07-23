package net.javango.bakingtime.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViewsService;

import net.javango.bakingtime.RecipeRepo;
import net.javango.bakingtime.model.Ingredient;
import net.javango.bakingtime.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WidgetService extends RemoteViewsService {

    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    public static List<Ingredient> ingredients;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(getApplicationContext(), intent);
    }

    /**
     * Retrieve appwidget id from intent it is needed to update widget later
     * initialize our AQuery class
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.hasExtra(AppWidgetManager.EXTRA_APPWIDGET_ID))
            appWidgetId = intent.getIntExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        fetchData();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * Gec data with callback
     */
    private void fetchData() {
        final int recipeId = 1;
        RecipeRepo.getInstance().getRecipe(recipeId, new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                List<Recipe> list = response.body();
                for (Recipe r : list)
                    if (r.getId() == recipeId)
                        ingredients = r.getIngredients();

                populateWidget();
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {

            }
        });
    }

    /**
     * Method which sends broadcast to WidgetProvider
     * so that widget is notified to do necessary action
     * and here action == WidgetProvider.DATA_FETCHED
     */
    private void populateWidget() {
        Intent widgetUpdateIntent = new Intent();
        widgetUpdateIntent.setAction(RecipeWidget.DATA_FETCHED);
        widgetUpdateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        sendBroadcast(widgetUpdateIntent);

        this.stopSelf();
    }
}