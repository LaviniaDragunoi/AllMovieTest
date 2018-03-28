package com.example.user.allmovietest.reviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.allmovietest.R;

import java.util.List;

/**
 * Created by Lavinia Dragunoi on 3/15/2018.
 * The ReviewAdapter will set up the recyclerView and
 * will help to populate the  detail_activity.xml with all reviews received by a movie
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private List<ReviewObject> reviewsList;
    private Context mContext;

    /**
     * ReviewAdapter constructor that will take the reviewList to display within context
     *
     * @param context     the context within will be displayed the reviewsList
     * @param reviewsList the list of reviews that will be displayed
     */
    public ReviewAdapter(Context context, List<ReviewObject> reviewsList) {
        this.mContext = context;
        this.reviewsList = reviewsList;
    }

    /**
     * This method will create Views each time the RecyclerView will need it
     *
     * @param parent   the ViewGroup that will within the ViewHolder
     * @param viewType
     * @return a new ReviewViewHolder that will have the View for each item
     */
    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(itemView);
    }

    /**
     * This method will be called to display information on a specific position and to register
     * a clickListener that will opened full review text for each review longer then 5 lines
     *
     * @param holder   this ReviewViewHolder should be updated
     * @param position the position of the item within the adapter
     */
    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        //get the position
        ReviewObject reviewItem = reviewsList.get(position);

        //get the holder that should be updated for each review details
        TextView author = holder.authorOfReview;
        author.setText(reviewItem.getAuthor());
        final TextView content = holder.contentReview;
        content.setText(reviewItem.getContent());
        //OnClickListener to display only 5 lines of text, and when it is clicked to show
        // full text of the review
        final int maxLines = 5;
        content.setMaxLines(maxLines);
        content.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                if (content.getMaxLines() == maxLines) {
                    content.setMaxLines(content.length());
                } else {
                    content.setMaxLines(maxLines);
                }
            }
        });
    }

    /**
     * this method is counting the number of items in the list displayed
     *
     * @return the number of items available
     */
    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    /**
     * This method adds new objects to the listVew
     */
    public void addAll(List<ReviewObject> reviewsList) {
        this.reviewsList.clear();
        this.reviewsList.addAll(reviewsList);
        notifyDataSetChanged();
    }

    /**
     * This method clear the news list
     */
    public void clearAll() {
        this.reviewsList.clear();
        notifyDataSetChanged();
    }

    /**
     * This innerclass will help hold the views
     */
    class ReviewViewHolder extends RecyclerView.ViewHolder {
        //Variables that will help to set the information needed in the RecyclerView that will display
        // the review list in the detail_activity.xml layout.
        TextView authorOfReview;
        TextView contentReview;

        //ViewHolder's constructor
        public ReviewViewHolder(View itemView) {
            super(itemView);

            //Find Views that will display each item
            authorOfReview = itemView.findViewById(R.id.review_author_tv);
            contentReview = itemView.findViewById(R.id.review_content_tv);
        }
    }
}
