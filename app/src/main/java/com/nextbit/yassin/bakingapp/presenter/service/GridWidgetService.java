package com.nextbit.yassin.bakingapp.presenter.service;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import com.nextbit.yassin.bakingapp.R;
import com.nextbit.yassin.bakingapp.domain.model.RecipeList;
import com.nextbit.yassin.bakingapp.domain.repository.RecipeRepository;
import com.nextbit.yassin.bakingapp.infrastructure.cache.CacheImpl;
import com.nextbit.yassin.bakingapp.infrastructure.repository.RecipeDataRepository;
import com.nextbit.yassin.bakingapp.infrastructure.repository.datasource.RecipeDataStoreFactory;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.nextbit.yassin.bakingapp.presenter.activity.RecipeDetails.EXTRA_RECIPE_ID_CLICKED_FROM_MAINACT_OR_WIDGET;
import static com.nextbit.yassin.bakingapp.presenter.activity.RecipeDetails.EXTRA_RECIPE_Name_CLICKED_FROM_MAINACT_OR_WIDGET;

public class GridWidgetService extends RemoteViewsService {
    List<String>recipeNameList=new ArrayList<>();
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

//        getRecipeIngredients();
//        try {
//            Thread.sleep(200000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        return new GridRemoteViewsFactory(this.getApplicationContext(),recipeNameList);
    }
    private void getRecipeIngredients(){


        RecipeRepository recipeRepository=new RecipeDataRepository(new RecipeDataStoreFactory(this.getApplicationContext(),new CacheImpl(this.getApplicationContext())));
        recipeRepository.recipeSteps()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<RecipeList>>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(GridWidgetService.this, "on completed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<RecipeList> recipeLists) {

                        recipeLists.addAll(recipeLists);


                    }
                });


    }
}
class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;

    private List<String>ingredientList=new ArrayList<>();
    private int[] drawablesImages = {R.drawable.nuttella,R.drawable.browins,R.drawable.yellowcake,R.drawable.chessacake};
    private String[] recipeNameList = {"NuttelaPie","Browines","YellowCake","ChesseCake"};


    GridRemoteViewsFactory(Context applicationContext, List<String> ingredientList) {
        mContext = applicationContext;
        this.ingredientList=ingredientList;


    }

    @Override
    public void onCreate() {


    }

    //called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {
        // Get all plant info ordered by creation time
//     getRecipeIngredients();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
       return 4;

    }

    /**
     * This method acts like the onBindViewHolder method in an Adapter
     *
     * @param position The current position of the item in the GridView to be displayed
     * @return The RemoteViews object to display for the provided postion
     */
    @Override
    public RemoteViews getViewAt(int position) {




        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.recipe_widget);


            views.setTextViewText(R.id.widget_recipe_name,recipeNameList[position]);
//            views.setTextViewText(R.id.recipe_ingredient_quantity,  ingredientList.get(position).getQuantity()+"  "+ ingredientList.get(position).getMeasure());


           views.setImageViewResource(R.id.widget_recipe_image,drawablesImages[position] );

        Bundle extras = new Bundle();
        extras.putInt(EXTRA_RECIPE_ID_CLICKED_FROM_MAINACT_OR_WIDGET, position);
        extras.putString(EXTRA_RECIPE_Name_CLICKED_FROM_MAINACT_OR_WIDGET,recipeNameList[position]);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.widget_recipe_image, fillInIntent);


        return views;

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1; // Treat all items in the GridView the same
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


}
