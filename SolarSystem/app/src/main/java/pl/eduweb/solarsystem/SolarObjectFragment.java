package pl.eduweb.solarsystem;


import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SolarObjectFragment extends Fragment {


    public static final String SHOW_TOOLBAR = "show_toolbar";
    public static final String OBJECT_KEY = "object";

    @Bind(R.id.objectImageView)
    ImageView objectImageView;
    @Bind(R.id.solarObjectToolbar)
    Toolbar solarObjectToolbar;
    @Bind(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @Bind(R.id.app_bar)
    AppBarLayout appBar;
    @Bind(R.id.objectTextView)
    TextView objectTextView;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.moonsLabel)
    TextView moonsLabel;
    @Bind(R.id.moonsRecyclerView)
    RecyclerView moonsRecyclerView;
    private SolarObject solarObject;

    private SolarObjectsAdapter.SolarObjectClickedListener solarObjectClickedListener;


    public SolarObjectFragment() {
        // Required empty public constructor
    }

    public static SolarObjectFragment newInstance(SolarObject solarObject, boolean showToolbar) {

        Bundle args = new Bundle();
        args.putSerializable(OBJECT_KEY, solarObject);
        args.putBoolean(SHOW_TOOLBAR, showToolbar);
        SolarObjectFragment fragment = new SolarObjectFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_solar_object, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        Bundle args = getArguments();
        if(args != null) {
            SolarObject serializable = (SolarObject) args.getSerializable(OBJECT_KEY);
            boolean showToolbar = args.getBoolean(SHOW_TOOLBAR);
            showSolarObject(serializable);

            setToolbar(showToolbar);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        solarObjectClickedListener = (SolarObjectsAdapter.SolarObjectClickedListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        solarObjectClickedListener = null;
    }

    public void setToolbar(boolean showToolbar) {
        if(showToolbar) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            activity.setSupportActionBar(solarObjectToolbar);
            toolbarLayout.setTitle(solarObject.getName());
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            solarObjectToolbar.setVisibility(View.GONE);
        }
    }

    private void showSolarObject(final SolarObject solarObject) {
        this.solarObject = solarObject;
        try {
            String text = SolarObject.loadStringFromAssets(getContext(), solarObject.getText());
            objectTextView.setText(Html.fromHtml(text));
        } catch (IOException e) {
            e.printStackTrace();
        }


        Glide.with(this)
                .load(solarObject.getImagePath())
                .into(objectImageView);

        moonsRecyclerView.setVisibility(solarObject.hasMoons() ? View.VISIBLE : View.GONE);
        moonsLabel.setVisibility(solarObject.hasMoons() ? View.VISIBLE : View.GONE);

        if(solarObject.hasMoons()) {
            SolarObjectsAdapter adapter = new SolarObjectsAdapter(solarObject.getMoons());
            adapter.setSolarObjectClickedListener(solarObjectClickedListener);
            moonsRecyclerView.setAdapter(adapter);
            moonsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            moonsRecyclerView.setNestedScrollingEnabled(false);
        }
        boolean hasMovie = !TextUtils.isEmpty(solarObject.getVideo());
        if (hasMovie) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showYouTubeVideo(solarObject.getVideo());
                }
            });
        } else {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
            lp.setAnchorId(View.NO_ID);

            fab.setVisibility(View.GONE);
        }
    }

    private void showYouTubeVideo(String video) {

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + video));
            startActivity(intent);
        }catch (ActivityNotFoundException e) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + video));
            startActivity(intent);
        }

    }





    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_solar_object, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_set_as_wallpaper) {
            setAsWallpaper(solarObject.getImage());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setAsWallpaper(String image) {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getContext());
        try {
            wallpaperManager.setStream(getContext().getAssets().open(image));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
