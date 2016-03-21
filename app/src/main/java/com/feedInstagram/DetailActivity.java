package com.feedInstagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.feedInstagram.Adapters.ImageIndexAdapter;
import com.feedInstagram.Controller.ManagerImages;
import com.feedInstagram.entitys.ImageDetail;
import com.feedInstagram.utilities.UtilImages;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private static int image_position_start = 0;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ImageIndexAdapter imageIndexAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        if (mViewPager != null) {
            mViewPager.setAdapter(mSectionsPagerAdapter);
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {

                    recyclerView.getLayoutManager().scrollToPosition(position);
                    imageIndexAdapter.setSelected(position);
                    loadFragmentTitle();

                }

                @Override
                public void onPageScrolled(int position, float positionOffset,
                                           int positionOffsetPixels) {

                }

                @Override
                public void onPageScrollStateChanged(int arg0) {

                }
            });
        }


    }

    private void loadFragmentTitle() {
        setTitle(mSectionsPagerAdapter.getPageTitle(mViewPager.getCurrentItem()));
    }

    @Override
    protected void onResume() {
        super.onResume();


        loadPosition();

        if (mViewPager.getAdapter().getCount() > 0) {
            createControlPages();
            if (mViewPager.getAdapter().getCount() > image_position_start) {

                mViewPager.setCurrentItem(image_position_start);
            } else {
                Toast.makeText(getApplicationContext(), R.string.error_detail_image_position, Toast.LENGTH_SHORT).show();
                mViewPager.setCurrentItem(0);
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.error_detail_image_empty, Toast.LENGTH_SHORT).show();
        }


    }

    private void loadPosition() {
        if (getIntent() != null) {
            if (getIntent().getExtras() != null) {
                image_position_start = getIntent().getExtras().getInt(UtilImages.INTENT_POSITION);
                return;
            }
        }

    }


    private void createControlPages() {
        recyclerView = (RecyclerView) findViewById(R.id.linear_control_page);
        imageIndexAdapter = new ImageIndexAdapter(getApplicationContext());
        recyclerView.setAdapter(imageIndexAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        imageIndexAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = recyclerView.getChildAdapterPosition(v);

                if (mViewPager != null)
                    mViewPager.setCurrentItem(position);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            showShareOptions();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showShareOptions() {
        String url = mSectionsPagerAdapter.getItem(mViewPager.getCurrentItem()).getArguments().getString(PlaceholderFragment.ARG_STANDARD_IMAGE);
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL");
        i.putExtra(Intent.EXTRA_TEXT, url);
        startActivity(Intent.createChooser(i, "Share URL"));

    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements View.OnClickListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_PUBLISH_DATE = "section_publish_date";
        private static final String ARG_AUTHOR = "section_author";
        private static final String ARG_TAG = "section_tags";
        public static final String ARG_STANDARD_IMAGE = "section_standard_image";
        private static final java.lang.String ARG_URL_PROFILE = "section_url_profile";
        public static final String ARG_TEXT = "section_text";
        public static final String ARG_STANDARD_IMAGE_WIDTH = "section_width";
        public static final String ARG_STANDARD_IMAGE_HEIGHT = "section_height";

        private TextView textview_publish;
        private TextView textview_author;
        private TextView textview_tags;
        private ImageView imageview_detail;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(ImageDetail imageDetail) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putLong(ARG_PUBLISH_DATE, imageDetail.getPublishDate());
            args.putString(ARG_AUTHOR, imageDetail.getAuthor());
            args.putStringArray(ARG_TAG, imageDetail.getTAgs());
            args.putString(ARG_STANDARD_IMAGE, imageDetail.getImageStandard().getUrl());
            args.putInt(ARG_STANDARD_IMAGE_WIDTH, imageDetail.getImageStandard().getWidth());
            args.putInt(ARG_STANDARD_IMAGE_HEIGHT, imageDetail.getImageStandard().getHeight());
            args.putString(ARG_URL_PROFILE, imageDetail.getUrlProfile());
            args.putString(ARG_TEXT, imageDetail.getText());
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));


            textview_publish = (TextView) rootView.findViewById(R.id.textview_publish);
            textview_author = (TextView) rootView.findViewById(R.id.textview_author);
            textview_tags = (TextView) rootView.findViewById(R.id.textview_tags);
            imageview_detail = (ImageView) rootView.findViewById(R.id.imageview_detail);


            textview_publish.setText(UtilImages.printPublishDate(getArguments().getLong(ARG_PUBLISH_DATE)));
            textview_author.setText(getArguments().getString(ARG_AUTHOR));
            textview_tags.setText(UtilImages.printTags(getArguments().getStringArray(ARG_TAG)));

            Picasso.with(imageview_detail.getContext())
                    .load(getArguments().getString(ARG_STANDARD_IMAGE))
                    .resize(getArguments().getInt(ARG_STANDARD_IMAGE_WIDTH), getArguments().getInt(ARG_STANDARD_IMAGE_HEIGHT))
                    .centerCrop()
                    .into(imageview_detail);


            imageview_detail.setOnClickListener(this);

            return rootView;
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.imageview_detail) {
                Intent i = new Intent(getActivity(), WebActivity.class);
                i.putExtra(UtilImages.INTENT_URL, getArguments().getString(ARG_URL_PROFILE));
                getActivity().startActivity(i);
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(ManagerImages.getImagePosition(position));
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return ManagerImages.getCountImages();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (getCount() == 0) {
                return getString(R.string.app_name);
            }

            return getItem(position).getArguments().getString(PlaceholderFragment.ARG_TEXT);
        }
    }
}
