package pl.eduweb.solarsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SolarObjectActivity extends AppCompatActivity implements SolarObjectsAdapter.SolarObjectClickedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solar_object);

        if (savedInstanceState == null) {
            SolarObject solarObject = (SolarObject) getIntent().getSerializableExtra(SolarObjectFragment.OBJECT_KEY);
            SolarObjectFragment fragment = SolarObjectFragment.newInstance(solarObject, true);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.solarObjectContainerLayout, fragment)
                    .commit();
        }
    }

    public static void start(Activity activity, SolarObject solarObject) {
        Intent intent = new Intent(activity, SolarObjectActivity.class);
        intent.putExtra(SolarObjectFragment.OBJECT_KEY, solarObject);
        activity.startActivity(intent);
    }

    @Override
    public void solarObjectClicked(SolarObject solarObject) {
        SolarObjectActivity.start(this, solarObject);
    }
}
