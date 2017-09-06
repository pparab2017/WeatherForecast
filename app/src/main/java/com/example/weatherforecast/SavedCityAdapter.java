package com.example.weatherforecast;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pushparajparab on 10/19/16.
 */
public class SavedCityAdapter extends RecyclerView.Adapter<SavedCityAdapter.SavedCityViewHolder> {

    private List<SavedCity> mSavedCities;
    private LayoutInflater inflater;
    private int recourseID;
    private Context mContext;
    private boolean mTempType;

    public SavedCityAdapter(List<SavedCity> savedCities, Context context, int recourseId, boolean tType)
    {
        this.inflater = LayoutInflater.from(context);
        this.mSavedCities = savedCities;
        this.recourseID = recourseId;
        this.mContext = context;
        this.mTempType = tType;
        this.itemClickCityCallBack = (ItemClickCityCallBack)context;
    }

    private ItemClickCityCallBack itemClickCityCallBack;

    public interface ItemClickCityCallBack
    {
        void OnItemClick(int p,boolean fav);
        void OnItemDelete(int p);
        //if next method.
    }


    @Override
    public SavedCityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(this.recourseID,parent,false);
        return new SavedCityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SavedCityViewHolder holder, int position) {

        SavedCity savedCity = this.mSavedCities.get(position);
        holder.city.setText(savedCity.getCityName() + ", " +savedCity.getCountryName());
        holder.temperature.setText(mTempType ?  savedCity.getTemperatureInF() : savedCity.getTemperatureInC());
        holder.date.setText("Updated on: " + savedCity.getDateInRequiredFormat());

        if(savedCity.isFavorite())
        {
            holder.iconStar.setImageResource(R.drawable.star_gold);
            holder.iconStar.setTag(R.drawable.star_gold);

        }
        else
        {
            holder.iconStar.setImageResource(R.drawable.star_gray);
            holder.iconStar.setTag(R.drawable.star_gray);
        }
        //iconStar.setTag(R.drawable.star_gray);

    }

    @Override
    public int getItemCount() {
        return mSavedCities.size();
    }

    class SavedCityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener
    {

        private TextView date,temperature,city;
        private ImageView iconStar;
        private View container;

        public SavedCityViewHolder(View itemView) {
            super(itemView);
            city= (TextView) itemView.findViewById(R.id.textViewCityCountryName);
            date= (TextView) itemView.findViewById(R.id.textViewLastUpdated);
            temperature= (TextView) itemView.findViewById(R.id.textViewCityTemperature);
            iconStar = (ImageView) itemView.findViewById(R.id.imageViewStarIcon);
            container = itemView.findViewById(R.id.eachCityRoot);
            iconStar.setOnClickListener(this);

            //container.setOnClickListener(this);
            container.setOnLongClickListener(this);


        }

        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.imageViewStarIcon:

                if( !iconStar.getTag().toString().trim().equals(String.valueOf( R.drawable.star_gold).trim()))
                {
                        iconStar.setImageResource(R.drawable.star_gold);
                        iconStar.setTag(R.drawable.star_gold);
                        itemClickCityCallBack.OnItemClick(getAdapterPosition(),true);

                }else
                {
                    iconStar.setImageResource(R.drawable.star_gray);
                    iconStar.setTag(R.drawable.star_gray);
                    itemClickCityCallBack.OnItemClick(getAdapterPosition(),false);
                }
                    break;

            }
        }


        @Override
        public boolean onLongClick(View v) {

            switch (v.getId()) {
                case R.id.eachCityRoot:
                    itemClickCityCallBack.OnItemDelete(getAdapterPosition());
                    break;
            }
            return true;
        }
    }
}
