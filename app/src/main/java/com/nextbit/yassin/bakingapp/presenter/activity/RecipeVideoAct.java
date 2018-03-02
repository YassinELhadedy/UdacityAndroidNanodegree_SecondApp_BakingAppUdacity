package com.nextbit.yassin.bakingapp.presenter.activity;

import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.nextbit.yassin.bakingapp.R;
import com.nextbit.yassin.bakingapp.domain.model.RecipeList;
import com.nextbit.yassin.bakingapp.domain.model.Step;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class RecipeVideoAct extends AppCompatActivity {
    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;
    private BandwidthMeter bandwidthMeter;
    private ArrayList<Step> steps = new ArrayList<>();
    private int selectedIndex;
    private Handler mainHandler;
    ArrayList<RecipeList> recipe;
    String recipeName;

    private View rootView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_video);
        mainHandler = new Handler();
        bandwidthMeter = new DefaultBandwidthMeter();


        textView = (TextView) rootView.findViewById(R.id.recipe_step_detail_text);
        textView.setText(getIntent().getStringExtra("description"));
        textView.setVisibility(View.VISIBLE);
        simpleExoPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.playerView);
        simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);

        String videoURL = getIntent().getStringExtra("videourl");

        Uri builtUri = Uri.parse(getIntent().getStringExtra("imageurl")).buildUpon().build();
        ImageView thumbImage = (ImageView) rootView.findViewById(R.id.thumbImage);
        Glide.with(getApplicationContext()).load(builtUri).into(thumbImage);

        if (!getIntent().getStringExtra("videourl").isEmpty()) {


            initializePlayer(Uri.parse(getIntent().getStringExtra("videourl")));


            Button mPrevStep = (Button) rootView.findViewById(R.id.previousStep);
            Button mNextstep = (Button) rootView.findViewById(R.id.nextStep);

            mPrevStep.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (steps.get(selectedIndex).getId() > 0) {
                        if (player != null) {
                            player.stop();
                        }
//                    itemClickListener.onListItemClick(steps,steps.get(selectedIndex).getId() - 1,recipeName);
                    } else {
                        Toast.makeText(getApplicationContext(), "You already are in the First step of the recipe", Toast.LENGTH_SHORT).show();

                    }
                }
            });

            mNextstep.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    int lastIndex = steps.size() - 1;
                    if (steps.get(selectedIndex).getId() < steps.get(lastIndex).getId()) {
                        if (player != null) {
                            player.stop();
                        }
//                    itemClickListener.onListItemClick(steps,steps.get(selectedIndex).getId() + 1,recipeName);
                    } else {
                        Toast.makeText(getApplicationContext(), "You already are in the Last step of the recipe", Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }
    }
    private void initializePlayer(Uri mediaUri) {
        if (player == null) {
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(mainHandler, videoTrackSelectionFactory);
            LoadControl loadControl = new DefaultLoadControl();

            player = ExoPlayerFactory.newSimpleInstance(getApplicationContext(), trackSelector, loadControl);
            simpleExoPlayerView.setPlayer(player);

            String userAgent = Util.getUserAgent(getApplicationContext(), "Baking App");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(getApplicationContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            player.prepare(mediaSource);
            player.setPlayWhenReady(true);
        }
    }
}
