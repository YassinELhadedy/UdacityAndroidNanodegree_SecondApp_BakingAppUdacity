package com.nextbit.yassin.bakingapp.presenter.service;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.widget.Toast;

import com.nextbit.yassin.bakingapp.R;
import com.nextbit.yassin.bakingapp.domain.model.RecipeList;
import com.nextbit.yassin.bakingapp.domain.repository.RecipeRepository;
import com.nextbit.yassin.bakingapp.infrastructure.cache.CacheImpl;
import com.nextbit.yassin.bakingapp.infrastructure.repository.RecipeDataRepository;
import com.nextbit.yassin.bakingapp.infrastructure.repository.datasource.RecipeDataStoreFactory;
import com.nextbit.yassin.bakingapp.presenter.widget.RecipeWidgetProvider;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class RecipeIntentService extends IntentService {
    public List<RecipeList>RecipeList=new ArrayList<>();

    private static final String ACTION_DISPLAY_RECIPE = "com.nextbit.yassin.bakingapp.presenter.service.action.DISPLAY.RECIPE";
    private static final String ACTION_BAZ = "com.nextbit.yassin.bakingapp.presenter.service.action.BAZ";

    private static final String EXTRA_PARAM1 = "com.nextbit.yassin.bakingapp.presenter.service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.nextbit.yassin.bakingapp.presenter.service.extra.PARAM2";

    public RecipeIntentService() {
        super("RecipeIntentService");
    }


    public static void startDisplayRecipes(Context context) {
        Intent intent = new Intent(context, RecipeIntentService.class);
        intent.setAction(ACTION_DISPLAY_RECIPE);
        context.startService(intent);
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_DISPLAY_RECIPE.equals(action)) {
                handleActionDisplayRecipe();

        }
    }
    }


    private void handleActionDisplayRecipe() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
        //Trigger data update to handle the GridView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);
        //Now update all widgets
        RecipeWidgetProvider.updateRecipeWidgets(this, appWidgetManager ,appWidgetIds);
    }

}



