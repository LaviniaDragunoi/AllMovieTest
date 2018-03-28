package com.example.user.allmovietest.trailers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.allmovietest.R;

import java.util.List;

/**
 * Created by Lavinia Dragunoi on 3/14/2018.
 * The TrailerAdapter will set up the recyclerView and
 * will help to populate the detail_activity.xml with trailers for each movie
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    private List<TrailerObject> trailersList;
    private Context mContext;

    /**
     * TrailerAdapter constructor that will take the trailerList to display within context
     *
     * @param context      the context within will be displayed the trailersList
     * @param trailersList the list of trailers that will be displayed
     */
    public TrailerAdapter(Context context, List<TrailerObject> trailersList) {
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

    /**
     * This method will create Views each time the RecyclerView will need it
     *
     * @param parent   the ViewGroup that will within the ViewHolder
     * @param viewType
     * @return a new TrailerViewHolder that will have the View for each item
     */
    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_item, parent, false);
        return new TrailerViewHolder(itemView);

    }

    /**
     * This method will be called to display information on a specific position and to register
     * a clickListener that will open Youtube to play the trailer
     *
     * @param holder   this TrailerViewHolder should be updated
     * @param position the position of the item within the adapter
     */
    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {

        //get the position
        final TrailerObject trailerObject = trailersList.get(position);

        //Button with OnClickListener that will sent the intent to open Youtube for each trailer
        FloatingActionButton video = holder.videoItem;
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerObject.getSiteTrailer()));
                mContext.startActivities(new Intent[]{youtubeIntent});
            }
        });

        //get the holder that should be updated for each trailer details
        TextView trailerName = holder.trailerName;
        trailerName.setText(trailerObject.getNameTrailer());

    }

    /**
     * this method is counting the number of items in the list displayed
     *
     * @return the number of items available
     */
    @Override
    public int getItemCount() {
        return trailersList.size();
    }

    /**
     * This innerclass will help hold the views
     */
    class TrailerViewHolder extends RecyclerView.ViewHolder {
        TextView trailerName;
        FloatingActionButton videoItem;

        public TrailerViewHolder(View itemView) {
            super(itemView);

            trailerName = itemView.findViewById(R.id.trailer_name);
            videoItem = itemView.findViewById(R.id.video_view_item);
        }
    }
}
