package com.nextbit.yassin.bakingapp.presenter.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.glidepalette.GlidePalette;
import com.nextbit.yassin.bakingapp.R;
import com.nextbit.yassin.bakingapp.domain.model.Ingredient;
import com.nextbit.yassin.bakingapp.domain.model.RecipeList;
import com.nextbit.yassin.bakingapp.domain.model.Step;
import com.nextbit.yassin.bakingapp.presenter.frag.RecipeVideo;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class RecipeStepsAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<RecipeList>recipeListList;
    private Context context;
    private int pos;
    private  int[] covers;

    public RecipeStepsAdapter(List<RecipeList> recipeListList, Context context,int pos) {
        this.recipeListList = recipeListList;
        this.context = context;
        this.pos=pos;
        covers = new int[]{
                R.drawable.nuttella,
                R.drawable.browins,
                R.drawable.yellowcake,
                R.drawable.chessacake
        };
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==1){
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ingredients_item, parent, false);
            return new IngredientHolder(v);


        }else {

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.grid_item, parent, false);

            return new PosterHolder(v);
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int viewType=getItemViewType(position);
        if (viewType==1){
            IngredientHolder ingredientHolder=(IngredientHolder)holder;
            Ingredient ingredient=recipeListList.get(pos).getIngredients().get(position);

            ingredientHolder.ingrStepDes.setText(ingredient.getIngredient());
            ingredientHolder.ingreCups.setText(ingredient.getQuantity()+" "+ingredient.getMeasure());
        }

        else {
            PosterHolder holder1=(PosterHolder)holder;
            Step r=recipeListList.get(pos).getSteps().get(position-recipeListList.get(pos).getIngredients().size());
            holder1.fav.setVisibility(VISIBLE);
            holder1.unFav.setVisibility(GONE);
            holder1.fav.invalidate();
            holder1.unFav.invalidate();


            String tit="  Enjoy make "+r.getShortDescription()+" Click here to see video";

            holder1.posterTitle.setTextColor(Color.BLACK);
            holder1.posterYear.setTextColor(Color.BLACK);
            holder1.posterTitle.setText(tit);
            holder1.posterTitle.setSelected(true);
            holder1.posterYear.setText(r.getShortDescription());
            holder1.posterTitleBackground.setBackgroundColor(GlidePalette.Profile.MUTED_DARK);
            holder1.poster.setBackgroundColor(GlidePalette.Swatch.BODY_TEXT_COLOR);
            holder1.poster.setImageResource(covers[pos]);

        }
    }

    @Override
    public int getItemCount() {
        return recipeListList.get(pos).getIngredients().size() +recipeListList.get(pos).getSteps().size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position>=recipeListList.get(pos).getIngredients().size())
        {
            return 2;
        }else {
            return 1;
        }
    }
    class PosterHolder extends RecyclerView.ViewHolder {
        CardView gridCard;
        ImageView poster, fav, unFav;
        View posterTitleBackground, dummyView;
        TextView posterTitle, posterYear;

        PosterHolder(View itemView) {
            super(itemView);
            poster =  itemView.findViewById(R.id.gridPoster);
            posterTitle =  itemView.findViewById(R.id.posterTitle);
            gridCard =  itemView.findViewById(R.id.gridCard);
            posterTitleBackground = itemView.findViewById(R.id.posterTitleBackground);
            fav =  itemView.findViewById(R.id.posterFav);
            unFav =  itemView.findViewById(R.id.posterUnFav);
            posterYear =  itemView.findViewById(R.id.posterYear);
            dummyView = itemView.findViewById(R.id.dummyView);
            itemView.setOnClickListener(view -> {
                int postionclick=getAdapterPosition()-recipeListList.get(pos).getIngredients().size();

                FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                RecipeVideo detailsFra=new RecipeVideo();
                Bundle args = new Bundle();
                args.putInt("clickpos",postionclick);
                args.putParcelableArrayList("steps", (ArrayList<? extends Parcelable>) recipeListList.get(pos).getSteps());
                detailsFra.setArguments(args);
                if (isTablet(context)){
                    manager.beginTransaction()
                            .replace(R.id.fragment_container2, detailsFra).commit();
                }else {
                    manager.beginTransaction()
                            .replace(R.id.fragment_container, detailsFra).commit();
                }



            });
        }
    }
    class IngredientHolder extends RecyclerView.ViewHolder{
        TextView ingrStepDes, ingreCups;


        IngredientHolder(View itemView) {
            super(itemView);
            ingrStepDes= itemView.findViewById(R.id.recipe_ingredient_name);
            ingreCups= itemView.findViewById(R.id.recipe_ingredient_quantity);
        }
    }
    public void updateMovies(List<RecipeList> items) {
        recipeListList = items;
        notifyDataSetChanged();
    }
    private boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;

    }

}
