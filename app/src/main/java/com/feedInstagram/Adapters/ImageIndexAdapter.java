package com.feedInstagram.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.feedInstagram.Controller.ManagerImages;
import com.feedInstagram.R;

/**
 * Created by santiagomedina on 20/03/16.
 */
public class ImageIndexAdapter extends RecyclerView.Adapter<ImageIndexAdapter.ViewHolder> {
    private Context mContext;
    private View.OnClickListener listener;
    // Start with first item selected
    private int selectedItem = 0;


    public ImageIndexAdapter(Context c) {
        mContext = c;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_image_index, parent, false);

        ViewHolder vh = new ViewHolder(itemView);

        return vh;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // Set selected state; use a state list drawable to style the view
        holder.imageview_index.setSelected(selectedItem == position);


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

        ImageView imageview_index;

        public ViewHolder(View itemView) {
            super(itemView);
            imageview_index = (ImageView) itemView.findViewById(R.id.imageview_index);

            imageview_index.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Redraw the old selection and the new

                    setSelected(getLayoutPosition());
                    if (listener != null)
                        listener.onClick(v);

                }
            });
        }


    }


}
