package pl.eduweb.solarsystem;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MoonsPagerAdapter extends FragmentStatePagerAdapter {

    private final SolarObject[] objectsWithMoons;

    public MoonsPagerAdapter(FragmentManager fm, SolarObject[] objectsWithMoons) {
        super(fm);
        this.objectsWithMoons = objectsWithMoons;
    }

    @Override
    public Fragment getItem(int position) {
        return SolarObjectsFragment.newInstance(objectsWithMoons[position].getMoons());
    }

    @Override
    public int getCount() {
        return objectsWithMoons.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return objectsWithMoons[position].getName();
    }
}
