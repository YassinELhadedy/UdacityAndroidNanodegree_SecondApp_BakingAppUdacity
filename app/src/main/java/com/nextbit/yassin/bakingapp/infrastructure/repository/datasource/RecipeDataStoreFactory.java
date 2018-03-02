package com.nextbit.yassin.bakingapp.infrastructure.repository.datasource;

import android.content.Context;
import android.util.Log;


import com.nextbit.yassin.bakingapp.infrastructure.cache.EntityCache;

import java.io.IOException;



//retrieve from disk if 1- offline 2- not empty db
    //otherwise retrieve from cloud

public class RecipeDataStoreFactory {
    private final Context context;
    private final EntityCache entityCache;

    public EntityCache getEntityCache() {
        return entityCache;
    }

    public RecipeDataStoreFactory(Context context, EntityCache entityCache) {
        this.context = context.getApplicationContext();
        this.entityCache = entityCache;
    }

    public RecipeDataStore create() {
        RecipeDataStore recipeDataStore;


        if (!isOnline()&& entityCache.isCached() ) {

            recipeDataStore = new DiskDataStore(this.entityCache);
        } else {
            entityCache.evictAll();
            recipeDataStore = createCloudDataStore();
        }

        return recipeDataStore;
    }



    public RecipeDataStore createCloudDataStore() {



        return new CloudDataStore(this.entityCache);
    }
    public boolean isOnline() {

        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }


}
