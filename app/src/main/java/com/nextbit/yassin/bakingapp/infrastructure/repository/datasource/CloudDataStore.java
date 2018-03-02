package com.nextbit.yassin.bakingapp.infrastructure.repository.datasource;





import com.nextbit.yassin.bakingapp.domain.model.RecipeList;
import com.nextbit.yassin.bakingapp.domain.service.SOService;
import com.nextbit.yassin.bakingapp.infrastructure.cache.EntityCache;
import com.nextbit.yassin.bakingapp.infrastructure.service.ApiUtils;

import java.util.List;

import rx.Observable;



public class CloudDataStore implements RecipeDataStore {
    private final EntityCache entityCache;
    private SOService mService;

    public CloudDataStore(EntityCache entityCache) {
        this.entityCache = entityCache;
        mService = ApiUtils.getSOService();
    }


    @Override
    public Observable<List<RecipeList>> recipeList() {
        return this.mService.getRecipeList();
    }

}
