package net.javango.bakingtime.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import net.javango.bakingtime.R;
import net.javango.bakingtime.RecipeRepo;
import net.javango.bakingtime.model.Recipe;

import java.util.List;

/**
 * The configuration screen for the {@link RecipeWidget RecipeWidget} AppWidget.
 */
public class WidgetConfigureActivity extends Activity {

    private static final String PREFS_NAME = "net.javango.bakingtime.widget.RecipeWidget";
    private static final String PREF_PREFIX_KEY = "recipewidget_";
    private static final String PREF_PREFIX_NAME = "recipewidget_name";
    int widgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private Spinner recipeChoice;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.recipe_widget_configure);

        List<Recipe> recipes = RecipeRepo.getInstance().getRecipes().getValue();
        recipeChoice = findViewById(R.id.recipe_choice);
        ArrayAdapter<Recipe> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, recipes);
        recipeChoice.setAdapter(adapter);

        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            widgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

       // mAppWidgetText.setText(loadTitlePref(WidgetConfigureActivity.this, widgetId));
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = WidgetConfigureActivity.this;

            // When the button is clicked, store the string locally
            Recipe recipe = (Recipe) recipeChoice.getSelectedItem();
            saveWidgetPref(context, widgetId, recipe);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            RecipeWidget.updateAppWidget(context, appWidgetManager, widgetId);
            appWidgetManager.notifyAppWidgetViewDataChanged(widgetId, R.id.list_view);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public WidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveWidgetPref(Context context, int appWidgetId, Recipe recipe) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putInt(PREF_PREFIX_KEY + appWidgetId, recipe.getId());
        prefs.putString(PREF_PREFIX_NAME + appWidgetId, recipe.getName());
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static int getRecipeId(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        int rid = prefs.getInt(PREF_PREFIX_KEY + appWidgetId, 0);
        return rid;
    }

    static String getRecipeName(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String name = prefs.getString(PREF_PREFIX_NAME + appWidgetId, null);
        return name;
    }

    static void deleteWidgetPref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.remove(PREF_PREFIX_NAME + appWidgetId);
        prefs.apply();
    }

}

