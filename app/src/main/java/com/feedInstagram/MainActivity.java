package com.feedInstagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.feedInstagram.Adapters.ImageAdapter;
import com.feedInstagram.Connection.Connection;
import com.feedInstagram.Controller.ManagerImages;
import com.feedInstagram.interfaces.ImagesLoaderListener;
import com.feedInstagram.utilities.UtilImages;

import org.json.JSONException;

import java.io.IOException;


public class MainActivity extends AppCompatActivity implements ImagesLoaderListener, PlaceholderLowImageFragment.ImageLowFragmentListener, ViewPager.OnPageChangeListener {

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
    private Animation mAnimationRotation;
    private boolean updating;
    private ImageAdapter mAdapterThumbnails;
    private RecyclerView mRecyclerViewThumbnails;
    private GridLayoutManager mGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        if (mViewPager != null) {
            mViewPager.setAdapter(mSectionsPagerAdapter);
            mViewPager.addOnPageChangeListener(this);
        }


        mRecyclerViewThumbnails = (RecyclerView) findViewById(R.id.recyclerView_thumbnails);

        mAdapterThumbnails = new ImageAdapter(this);
        mGridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        if (mRecyclerViewThumbnails != null) {
            mRecyclerViewThumbnails.setAdapter(mAdapterThumbnails);
            mRecyclerViewThumbnails.setLayoutManager(mGridLayoutManager);
            mRecyclerViewThumbnails.setItemAnimator(new DefaultItemAnimator());
            mAdapterThumbnails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = mRecyclerViewThumbnails.getChildAdapterPosition(v);

                    if (mViewPager != null)
                        mViewPager.setCurrentItem(position);
                }
            });
        }

        mAnimationRotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        if (mAnimationRotation != null)
            mAnimationRotation.setRepeatCount(Animation.INFINITE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        ManagerImages.addImagesLoaderListener(this);

        if (!updating) {
            if (!Connection.isConnected(getApplication())) {
                Toast.makeText(getApplicationContext(), R.string.error_not_connected, Toast.LENGTH_SHORT).show();
                finish();
            }

            if (ManagerImages.getCountImages() == 0) {

                if (!ManagerImages.loadImagesCache(getApplicationContext())) {
                    update();
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ManagerImages.removeImagesLoaderListener(this);
    }

    View refreshBtn;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem update = menu.findItem(R.id.action_update);


        if (updating) {
            if (refreshBtn == null) {
                update.setActionView(R.layout.updating);
                refreshBtn = update.getActionView();
            }
            refreshBtn.setVisibility(View.VISIBLE);
            refreshBtn.startAnimation(mAnimationRotation);
        } else {
            if (refreshBtn != null) {
                refreshBtn.setVisibility(View.GONE);
                refreshBtn.clearAnimation();
                refreshBtn = null;

            }

        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_update) {
            update();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void update() {

        Runnable loadImages = new Runnable() {
            @Override
            public void run() {
                try {
                    ManagerImages.loadImages(getApplicationContext());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(loadImages).start();

    }

    @Override
    public void loadingStart() {

        updating = true;
        updateMenu();


    }

    @Override
    public void loadingEnd() {
        updating = false;
        updateMenu();
        updateView();

    }

    @Override
    public void loadingStarted() {
        updating = true;
        updateMenu();
    }

    private void updateView() {
        Runnable uiThread = new Runnable() {
            @Override
            public void run() {
                mViewPager.getAdapter().notifyDataSetChanged();
                mAdapterThumbnails.notifyDataSetChanged();

            }
        };
        runOnUiThread(uiThread);
    }

    public void updateMenu() {
        Runnable uiThread = new Runnable() {
            @Override
            public void run() {
                invalidateOptionsMenu();
            }
        };
        runOnUiThread(uiThread);
    }

    @Override
    public void clicImageLowFragmentPosition(int position) {
        Intent i = new Intent(this, DetailActivity.class);
        i.putExtra(UtilImages.INTENT_POSITION, position);
        startActivity(i);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {


        mAdapterThumbnails.setSelected(position);


        mGridLayoutManager.scrollToPosition(position);


    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    /**
     * A placeholder fragment containing a simple view.
     */


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

            return PlaceholderLowImageFragment.newInstance(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {

            return ManagerImages.getCountImages();
        }


        @Override
        public CharSequence getPageTitle(int position) {

            return ManagerImages.getImagePosition(position).getText();
        }
    }
}
