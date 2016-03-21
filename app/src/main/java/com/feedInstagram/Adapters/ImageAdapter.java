package com.feedInstagram.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feedInstagram.Controller.ManagerImages;
import com.feedInstagram.R;
import com.feedInstagram.entitys.ImageDetail;
import com.squareup.picasso.Picasso;

/**
 * Created by santiagomedina on 20/03/16.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private Context mContext;
    private View.OnClickListener listener;
    // Start with first item selected
    private int selectedItem = 0;


    public ImageAdapter(Context c) {
        mContext = c;
    }


    public ImageDetail getItem(int position) {
        return ManagerImages.getImagePosition(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_image_thumbnail, parent, false);

        ViewHolder vh = new ViewHolder(itemView);

        return vh;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageDetail item = getItem(position);

        holder.bindImage(item, mContext);
        // Set selected state; use a state list drawable to style the view
        holder.imageview_thumbnail.setSelected(selectedItem == position);


    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return ManagerImages.getCountImages();
    }


    public void setSelected(int position) {
        notifyItemChanged(selectedItem);
        selectedItem = position;
        notifyItemChanged(selectedItem);


    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageview_thumbnail;
        TextView textview_thumbnail;

        public ViewHolder(View itemView) {
            super(itemView);
            imageview_thumbnail = (ImageView) itemView.findViewById(R.id.imageview_thumbnail);
            textview_thumbnail = (TextView) itemView.findViewById(R.id.textview_thumbnail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Redraw the old selection and the new

                    setSelected(getLayoutPosition());
                    if (listener != null)
                        listener.onClick(v);

                }
            });
        }

        public void bindImage(ImageDetail imageDetail, Context context) {
            Picasso.with(context)
                    .load(imageDetail.getImageLowResolution().getUrl())
                    .resize(imageDetail.getImageLowResolution().getWidth(), imageDetail.getImageLowResolution().getHeight())
                    .centerCrop()
                    .into(imageview_thumbnail);

            textview_thumbnail.setText(imageDetail.getText());
        }


    }


}
