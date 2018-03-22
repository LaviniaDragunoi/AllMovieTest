package com.example.user.allmovietest.trailers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.allmovietest.DetailActivity;
import com.example.user.allmovietest.R;


import java.util.List;

/**
 * Created by Lavinia Dragunoi on 3/14/2018.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
private List<TrailerObject> trailersList;
private Context mContext;

public TrailerAdapter(Context context, List<TrailerObject> trailersList){
    this.mContext = context;
    this.trailersList = trailersList;
}

    /**
     * This method adds new objects to the listVew
     */
    public void addAll(List<TrailerObject> trailersList) {
        this.trailersList.clear();
        this.trailersList.addAll(trailersList);
        notifyDataSetChanged();
    }

    /**
     * This method clear the news list
     */
    public void clearAll() {
        this.trailersList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_item, parent, false);
        return new TrailerViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {

        final TrailerObject trailerObject = trailersList.get(position);


        FloatingActionButton video = holder.videoItem;
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerObject.getSiteTrailer()));
                mContext.startActivities(new Intent[]{youtubeIntent});
            }
        });

        TextView trailerName = holder.trailerName;
        trailerName.setText(trailerObject.getNameTrailer());

    }

    @Override
    public int getItemCount() {
        return trailersList.size();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder{
        TextView trailerName;
        FloatingActionButton videoItem;
        public TrailerViewHolder(View itemView) {
            super(itemView);

            trailerName = itemView.findViewById(R.id.trailer_name);
            videoItem = itemView.findViewById(R.id.video_view_item);
        }
    }

}
