package pl.eduweb.solarsystem;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoonsFragment extends Fragment {


    public static final String OBJECTS_KEY = "objects";
    @Bind(R.id.moonsViewPager)
    ViewPager moonsViewPager;

    private TabCallback tabCallback;


    public MoonsFragment() {
        // Required empty public constructor
    }

    public static MoonsFragment newInstance(SolarObject[] solarObjects) {

        Bundle args = new Bundle();
        args.putSerializable(OBJECTS_KEY, solarObjects);

        MoonsFragment fragment = new MoonsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        tabCallback = (TabCallback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        tabCallback = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_moons, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SolarObject[] solarObjects = (SolarObject[]) getArguments().getSerializable(OBJECTS_KEY);
        MoonsPagerAdapter moonsPagerAdapter = new MoonsPagerAdapter(getChildFragmentManager(), solarObjects);
        moonsViewPager.setAdapter(moonsPagerAdapter);

        tabCallback.showTabs(moonsViewPager);
    }



    @Override
    public void onDestroyView() {
        tabCallback.hideTabs();
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public interface TabCallback {
        void showTabs(ViewPager viewPager);
        void hideTabs();
    }
}
