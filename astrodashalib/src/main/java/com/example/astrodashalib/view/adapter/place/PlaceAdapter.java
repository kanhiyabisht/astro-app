package com.example.astrodashalib.view.adapter.place;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.astrodashalib.R;
import com.example.astrodashalib.model.Place;

import java.util.ArrayList;
/**
 * Created by himanshu on 25/09/17.
 */

public class PlaceAdapter extends BaseAdapter {

    Context context;
    ArrayList<Place> mPlaceArrayList;
    LayoutInflater inflater;
    OnPlaceSelected onPlaceSelected;


    public PlaceAdapter(Context context, ArrayList<Place> list, OnPlaceSelected onPlaceSelected) {
        this.context = context;
        this.mPlaceArrayList = list;
        this.inflater = LayoutInflater.from(context);
        this.onPlaceSelected = onPlaceSelected;
    }

    @Override
    public int getCount() {
        return mPlaceArrayList.size();
    }

    @Override
    public Place getItem(int position) {
        return mPlaceArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    static class PlacesViewHolder {
        RelativeLayout progressLayout;
        TextView placeHeadingTv;
        CardView placeCardView;

        public PlacesViewHolder(View view) {
            progressLayout = (RelativeLayout) view.findViewById(R.id.loading_rl);
            placeHeadingTv = (TextView) view.findViewById(R.id.place_heading_tv);
            placeCardView = (CardView) view.findViewById(R.id.place_card_view);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlacesViewHolder holder;
        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.place_item, null);
            holder = new PlacesViewHolder(view);
            view.setTag(holder);
        } else
            holder = (PlacesViewHolder) view.getTag();


        Place place = mPlaceArrayList.get(position);
        if (place.isProgressBar) {
            holder.progressLayout.setVisibility(View.VISIBLE);
            holder.placeCardView.setVisibility(View.GONE);
        } else {
            holder.progressLayout.setVisibility(View.GONE);
            holder.placeCardView.setVisibility(View.VISIBLE);
            holder.placeHeadingTv.setText(place.place);
        }

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!place.isProgressBar)
                    onPlaceSelected.placeSelected(place);
            }
        });
        return view;
    }

    public interface OnPlaceSelected {
        void placeSelected(Place place);
    }
}
