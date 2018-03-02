package com.nextbit.yassin.bakingapp.presenter.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.nextbit.yassin.bakingapp.R;
import com.nextbit.yassin.bakingapp.domain.model.Step;
import com.nextbit.yassin.bakingapp.presenter.frag.RecipeButtons;
import com.nextbit.yassin.bakingapp.presenter.frag.RecipeVideo;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetails extends AppCompatActivity implements RecipeVideo.ListItemClickListener {
    public static final String EXTRA_RECIPE_ID_CLICKED_FROM_MAINACT_OR_WIDGET = "positionClickedFromMainActivity";
    public static final String EXTRA_RECIPE_Name_CLICKED_FROM_MAINACT_OR_WIDGET = "RECIPEName";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Toolbar myToolbar =  findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
//        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra(EXTRA_RECIPE_Name_CLICKED_FROM_MAINACT_OR_WIDGET));
        int selectedRecipeBundle = getIntent().getIntExtra(EXTRA_RECIPE_ID_CLICKED_FROM_MAINACT_OR_WIDGET,1);
        Bundle args = new Bundle();

        args.putInt(EXTRA_RECIPE_ID_CLICKED_FROM_MAINACT_OR_WIDGET,selectedRecipeBundle);

        RecipeButtons recipeButtons=new RecipeButtons();
        recipeButtons.setArguments(args);
        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, recipeButtons, "").commit();
        }
    }

    @Override
    public void onListItemClick(List<Step> allSteps, int Index) {
        RecipeVideo detailsFra=new RecipeVideo();
        Bundle args = new Bundle();
        args.putInt("clickpos",Index);
        args.putParcelableArrayList("steps", (ArrayList<? extends Parcelable>) allSteps);

        detailsFra.setArguments(args);



        if (isTablet(this)){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container2, detailsFra).commit();
        }
        else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, detailsFra)
                    .commit();
        }



    }

    public  boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        this.finish();
        return true;
    }


}
