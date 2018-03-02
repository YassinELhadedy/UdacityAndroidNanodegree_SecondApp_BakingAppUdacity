package com.nextbit.yassin.bakingapp.presenter.widget;


import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.widget.RemoteViews;
import android.widget.Toast;

import com.nextbit.yassin.bakingapp.R;
import com.nextbit.yassin.bakingapp.presenter.activity.RecipeDetails;
import com.nextbit.yassin.bakingapp.presenter.service.GridWidgetService;
import com.nextbit.yassin.bakingapp.presenter.service.RecipeIntentService;

import static com.nextbit.yassin.bakingapp.presenter.activity.RecipeDetails.EXTRA_RECIPE_ID_CLICKED_FROM_MAINACT_OR_WIDGET;
import static com.nextbit.yassin.bakingapp.presenter.activity.RecipeDetails.EXTRA_RECIPE_Name_CLICKED_FROM_MAINACT_OR_WIDGET;


/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId ) {
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        int width = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
        RemoteViews rv;
        if (width < 300) {
            rv = getSinglePlantRemoteView(context, R.drawable.nuttella);
            Toast.makeText(context, "listview", Toast.LENGTH_SHORT).show();
        } else {
            rv = getGardenGridRemoteView(context);
        }
        appWidgetManager.updateAppWidget(appWidgetId, rv);



    }

    private static RemoteViews getSinglePlantRemoteView(Context context, int imgRes) {
        // Set the click handler to open the DetailActivity for plant ID,
        // or the MainActivity if plant ID is invalid

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        // Update image and text
        views.setTextViewText(R.id.widget_recipe_name, "NutellaPie");
        views.setImageViewResource(R.id.widget_recipe_image,imgRes );

        Intent appIntent = new Intent(context, RecipeDetails.class);
        appIntent.putExtra(EXTRA_RECIPE_ID_CLICKED_FROM_MAINACT_OR_WIDGET, 0);
        appIntent.putExtra(EXTRA_RECIPE_Name_CLICKED_FROM_MAINACT_OR_WIDGET, "NutellaPie");

        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_recipe_image, appPendingIntent);

        // Handle empty gardens
        views.setEmptyView(R.id.widget_grid_view, R.id.empty_view);



        return views;
    }

    /**
     * Creates and returns the RemoteViews to be displayed in the GridView mode widget
     *
     * @param context The context
     * @return The RemoteViews for the GridView mode widget
     */
    private static RemoteViews getGardenGridRemoteView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_gridview);
        // Set the GridWidgetService intent to act as the adapter for the GridView
        Intent intent = new Intent(context, GridWidgetService.class);
        views.setRemoteAdapter(R.id.widget_grid_view, intent);

        // Set the PlantDetailActivity intent to launch when clicked
        Intent appIntent = new Intent(context, RecipeDetails.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_grid_view, appPendingIntent);
        // Handle empty gardens
        views.setEmptyView(R.id.widget_grid_view, R.id.empty_view);
        return views;
    }








    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        RecipeIntentService.startDisplayRecipes(context);

    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager,
                                           int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager , appWidgetId);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        RecipeIntentService.startDisplayRecipes(context);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

