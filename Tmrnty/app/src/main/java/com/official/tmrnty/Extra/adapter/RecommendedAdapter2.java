package com.official.tmrnty.Extra.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.official.tmrnty.Extra.FoodDetails;
import com.official.tmrnty.Extra.model.Recommended;
import com.official.tmrnty.R;

import java.util.List;

public class RecommendedAdapter2 extends RecyclerView.Adapter<RecommendedAdapter2.RecommendedViewHolder> {

    private Context context;
    private List<Recommended> recommendedList;

    public RecommendedAdapter2(Context context, List<Recommended> recommendedList) {
        this.context = context;
        this.recommendedList = recommendedList;
    }

    @NonNull
    @Override
    public RecommendedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.videos_recycle, parent, false);
        return new RecommendedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendedViewHolder holder, @SuppressLint("RecyclerView") final int position) {

            holder.recommendedName.setText(recommendedList.get(position).getsubject());
            holder.recommendedRating.setText(recommendedList.get(position).getrate());
            holder.recommendedLikes.setText(recommendedList.get(position).getlikes() + " Likes");
            holder.recommendedtype.setText(recommendedList.get(position).gettype());
            holder.recommendedviews.setText(recommendedList.get(position).getviews());
            Glide.with(context).load(recommendedList.get(position).getimage()).into(holder.recommendedImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, FoodDetails.class);
                i.putExtra("id", recommendedList.get(position).getid());
                i.putExtra("subject", recommendedList.get(position).getsubject());
                i.putExtra("description", recommendedList.get(position).getdescription());
                i.putExtra("url", recommendedList.get(position).geturl());
                i.putExtra("image", recommendedList.get(position).getimage());
                i.putExtra("rate", recommendedList.get(position).getrate());
                i.putExtra("views", recommendedList.get(position).getviews());
                i.putExtra("like", recommendedList.get(position).getlikes());
                i.putExtra("type", recommendedList.get(position).gettype());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return recommendedList.size();
    }

    public static class RecommendedViewHolder extends RecyclerView.ViewHolder{

        ImageView recommendedImage;
        TextView recommendedName, recommendedRating, recommendedLikes, recommendedtype, recommendedviews;

        public RecommendedViewHolder(@NonNull View itemView) {
            super(itemView);

            recommendedImage = itemView.findViewById(R.id.recommended_image);
            recommendedName = itemView.findViewById(R.id.recommended_name);
            recommendedRating = itemView.findViewById(R.id.recommended_rating);
            recommendedLikes = itemView.findViewById(R.id.recommended_likes);
            recommendedtype = itemView.findViewById(R.id.article_type);
            recommendedviews = itemView.findViewById(R.id.recommended_views);
            recommendedImage.setClipToOutline(true);

        }
    }

    public static String getStringBetweenTwoCharacters(String input, String to, String from)
    {
        return input.substring(input.indexOf(to)+2, input.lastIndexOf(from));
    }
}
