package net.javango.bakingtime.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import net.javango.bakingtime.R;
import net.javango.bakingtime.RecipeRepo;
import net.javango.bakingtime.model.Ingredient;
import net.javango.bakingtime.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(getApplicationContext(), intent);
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        private List<Ingredient> ingredients;
        private Context context;

        public ListRemoteViewsFactory(Context context, Intent intent) {
            this.context = context;
        }

        @Override
        public void onCreate() {
            // empty
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public int getViewTypeCount() {
            return 1; // all items in the view are the same
        }

        @Override
        public int getCount() {
            return ingredients.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public void onDestroy() {
            // empty
        }

        //called on start and when notifyAppWidgetViewDataChanged is called
        @Override
        public void onDataSetChanged() {
            Recipe r = RecipeRepo.getInstance().getRecipeSync(1);
            ingredients = r.getIngredients();
        }

        /*
         *Similar to getView of Adapter where instead of View
         *we return RemoteViews
         *
         */
        @Override
        public RemoteViews getViewAt(int position) {
            final RemoteViews remoteView = new RemoteViews(
                    context.getPackageName(), R.layout.list_item_ingredient);
            Ingredient ingredient = ingredients.get(position);
            remoteView.setTextViewText(R.id.ingredient_name, ingredient.getName());
            remoteView.setTextViewText(R.id.ingredient_measure, ingredient.getMeasure());
            remoteView.setTextViewText(R.id.ingredient_quantity, String.valueOf(ingredient.getQuantity()));

            return remoteView;
        }

    }