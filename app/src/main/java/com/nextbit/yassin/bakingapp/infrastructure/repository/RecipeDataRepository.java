package com.nextbit.yassin.bakingapp.infrastructure.repository;

import com.nextbit.yassin.bakingapp.domain.model.RecipeList;
import com.nextbit.yassin.bakingapp.domain.repository.RecipeRepository;
import com.nextbit.yassin.bakingapp.infrastructure.repository.datasource.RecipeDataStore;
import com.nextbit.yassin.bakingapp.infrastructure.repository.datasource.RecipeDataStoreFactory;

import java.util.List;

import rx.Observable;



public class RecipeDataRepository implements RecipeRepository {
    private final RecipeDataStoreFactory recipeDataStoreFactory;

    public RecipeDataRepository(RecipeDataStoreFactory recipeDataStoreFactory) {
        this.recipeDataStoreFactory = recipeDataStoreFactory;
    }
    @Override
    public Observable<List<RecipeList>> recipeSteps() {
        final RecipeDataStore recipeDataStore = this.recipeDataStoreFactory.create();
        return recipeDataStore.recipeList();

    }
}
