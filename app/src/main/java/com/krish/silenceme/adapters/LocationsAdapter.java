package com.krish.silenceme.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.krish.silenceme.R;
import com.krish.silenceme.activity.LocationActivity;
import com.krish.silenceme.activity.MainActivity;
import com.krish.silenceme.beans.MyLocation;
import com.krish.silenceme.common.Constants;
import com.krish.silenceme.service.LocationService;

import java.util.List;

public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.LocationViewHolder> {

    private Context context;

    private List<MyLocation> locations;

    public LocationsAdapter(Context context, List<MyLocation> locations){
        this.context = context;
        this.locations = locations;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.locations_cardview_main, viewGroup, false);
        return new LocationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final LocationViewHolder locationViewHolder, int i) {

        final MyLocation location = locations.get(i);
        locationViewHolder.tvLocName.setText(location.getLocationName());
        locationViewHolder.switchLoc.setChecked(location.isActive());

        locationViewHolder.switchLoc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                location.setActive(isChecked);
                if(context instanceof MainActivity){
                    ((MainActivity)context).getLocationService().saveLocation(location);
                }
            }
        });

        locationViewHolder.view.setOnClickListener(new View.OnClickListener() {
            Context context = locationViewHolder.view.getContext();
            @Override public void onClick(View v) {
                Intent locActivity =  new Intent(context,LocationActivity.class);
                locActivity.putExtra("locObject", new Gson().toJson(location));
                ((MainActivity)context).startActivityForResult(locActivity, Constants.DELETE_LOC_ACT_REQUEST_OK);
            }
        });


    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public static class LocationViewHolder extends RecyclerView.ViewHolder {
        public View view;

        protected static TextView tvLocName;
        protected static Switch switchLoc;

        public LocationViewHolder(View v){
            super(v);
            view = v;

            /*view.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Intent locActivity =  new Intent(view.getContext(),LocationActivity.class);
                    view.getContext().startActivity(locActivity);

                    locActivity.putExtra("myObject", new Gson().toJson(myobject));

                }
            });*/

            tvLocName = v.findViewById(R.id.name_on_card);
            switchLoc = v.findViewById(R.id.switchLoc);
        }
    }

    public void setLocations(List<MyLocation> locations){
        this.locations = locations;
    }
}
