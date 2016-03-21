package com.feedInstagram;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.feedInstagram.Controller.ManagerImages;
import com.feedInstagram.entitys.ImageDetail;
import com.squareup.picasso.Picasso;

/**
 * Created by santiagomedina on 20/03/16.
 */
public class PlaceholderLowImageFragment extends Fragment implements View.OnClickListener {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */


    private static final java.lang.String ARG_POSITION = "section_position";


    ImageLowFragmentListener listener;

    public PlaceholderLowImageFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderLowImageFragment newInstance(int position) {

        PlaceholderLowImageFragment fragment = new PlaceholderLowImageFragment();
        Bundle args = new Bundle();

        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ImageView imageview_low = (ImageView) rootView.findViewById(R.id.imageview_low_image);

        ImageDetail imageDetail = ManagerImages.getImagePosition(getArguments().getInt(ARG_POSITION));
        if (imageDetail != null) {
            Picasso.with(getActivity())
                    .load(imageDetail.getImageLowResolution().getUrl())
                    .resize(imageDetail.getImageLowResolution().getWidth(), imageDetail.getImageLowResolution().getHeight())
                    .centerCrop()
                    .into(imageview_low);
            imageview_low.setOnClickListener(this);
        }
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof ImageLowFragmentListener)) {
            throw new ExceptionInInitializerError("activity must implement ImageLowFragmentListener");
        }
        listener = (ImageLowFragmentListener) context;

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imageview_low_image) {
            if (listener != null) {
                listener.clicImageLowFragmentPosition(getArguments().getInt(ARG_POSITION));
            }

        }

    }

    public interface ImageLowFragmentListener {

        void clicImageLowFragmentPosition(int position);
    }
}