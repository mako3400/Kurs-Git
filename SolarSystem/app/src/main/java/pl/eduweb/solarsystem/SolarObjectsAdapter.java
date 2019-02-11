package pl.eduweb.solarsystem;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SolarObjectsAdapter extends RecyclerView.Adapter<SolarObjectsAdapter.SolarObjectViewHolder> {

    private final SolarObject[] solarObjects;

    private SolarObjectClickedListener solarObjectClickedListener;

    public SolarObjectsAdapter(SolarObject[] solarObjects) {
        this.solarObjects = solarObjects;
    }

    public void setSolarObjectClickedListener(SolarObjectClickedListener solarObjectClickedListener) {
        this.solarObjectClickedListener = solarObjectClickedListener;
    }

    @Override
    public SolarObjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_solar_object, parent, false);
        return new SolarObjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SolarObjectViewHolder holder, int position) {
        SolarObject solarObject = solarObjects[position];
        holder.setSolarObject(solarObject);

    }

    @Override
    public int getItemCount() {
        return solarObjects.length;
    }

    private void itemClicked(SolarObject solarObject) {

        if(solarObjectClickedListener != null) {
            solarObjectClickedListener.solarObjectClicked(solarObject);
        }
    }

    class SolarObjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.itemImageView)
        ImageView itemImageView;
        @Bind(R.id.itemTextView)
        TextView itemTextView;

        private SolarObject solarObject;

        public SolarObjectViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void setSolarObject(SolarObject solarObject) {
            this.solarObject = solarObject;

            itemTextView.setText(solarObject.getName());

            Glide.with(itemImageView.getContext())
                    .load(solarObject.getImagePath())
                    .placeholder(R.drawable.planet_placeholder)
                    .fitCenter()
                    .into(itemImageView);

        }

        public SolarObject getSolarObject() {
            return solarObject;
        }

        @Override
        public void onClick(View v) {
            itemClicked(solarObject);
        }
    }

    public interface SolarObjectClickedListener {
        void solarObjectClicked(SolarObject solarObject);
    }


}
