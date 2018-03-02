package com.nextbit.yassin.bakingapp.infrastructure.cache;

import android.content.Context;

import com.nextbit.yassin.bakingapp.domain.model.RecipeList;

import java.util.List;

import rx.Observable;



public class CacheImpl implements EntityCache {
    private final Context context;
//    private DatabaseHandler databaseHandler;

    public CacheImpl(Context context) {
        this.context = context.getApplicationContext();
//        databaseHandler=new DatabaseHandler(this.context);

    }
    @Override
    public Observable<List<RecipeList>> getRecipesCache() {
        return null;
    }

    @Override
    public void putRecipes(RecipeList recipeList) {

    }

    @Override
    public boolean isCached() {
        return false;
    }

    @Override
    public void evictAll() {

    }
}
