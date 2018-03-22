package com.example.user.allmovietest.reviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.allmovietest.R;

import java.util.List;

/**
 * Created by Lavinia Dragunoi on 3/15/2018.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private List<ReviewObject> reviewsList;
    private Context mContext;

    public ReviewAdapter(Context context, List<ReviewObject> reviewsList){
        this.mContext = context;
        this.reviewsList = reviewsList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {

        ReviewObject reviewItem = reviewsList.get(position);

        TextView author = holder.authorOfReview;
        author.setText(reviewItem.getAuthor());
        final TextView content = holder.contentReview;
        content.setText(reviewItem.getContent());
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

    class ReviewViewHolder extends RecyclerView.ViewHolder{

        TextView authorOfReview;
        TextView contentReview;

        public ReviewViewHolder(View itemView) {
            super(itemView);

            authorOfReview = itemView.findViewById(R.id.review_author_tv);
            contentReview = itemView.findViewById(R.id.review_content_tv);
        }
    }
}
