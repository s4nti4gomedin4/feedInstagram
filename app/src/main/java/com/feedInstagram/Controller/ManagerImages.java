package com.feedInstagram.Controller;

import android.content.Context;
import android.content.SharedPreferences;

import com.feedInstagram.Connection.Connection;
import com.feedInstagram.entitys.ImageData;
import com.feedInstagram.entitys.ImageDetail;
import com.feedInstagram.interfaces.ImagesLoaderListener;
import com.feedInstagram.utilities.UtilImages;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by santiagomedina on 15/03/16.
 */
public class ManagerImages {
    private static final String DATA = "data";
    private static final String TAGS = "tags";
    private static final String USER = "user";
    private static final String FULL_NAME = "full_name";
    private static final String CREATE_TIME = "created_time";
    private static final String STANDARD_RESOLUTION = "standard_resolution";
    private static final String URL = "url";
    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";
    private static final String IMAGES = "images";
    private static final String LINK = "link";
    private static final String CAPTION = "caption";
    private static final String TEXT = "text";
    private static final String THUMBNAIL = "thumbnail";
    private static final String LOW_RESOLUTION = "low_resolution";
    private static List<ImageDetail> imageDetail = new ArrayList<>();
    private static List<ImagesLoaderListener> imagesLoaderListeners = new ArrayList<>();
    private static boolean updating;


    public static int getCountImages() {
        return getImageSize();
    }

    private static int getImageSize() {

        return imageDetail.size();
    }

    public static void loadImages(Context context) throws IOException, JSONException {
        if (updating) {
            callLoadingStarted();
            return;
        }
        updating = true;


        try {
            callLoadingStart();
            String urlJson = Connection.getJson(UtilImages.URL);

            SharedPreferences preferences = context.getSharedPreferences(
                    UtilImages.PREFERENCE_NAME, Context.MODE_PRIVATE);
            preferences.edit().putString(UtilImages.PREFERENCE_KEY_JSON, urlJson).apply();

            updateImageData(urlJson);
        } finally {
            updating = false;
            callLoadingEnd();

        }


    }

    private static void callLoadingStarted() {
        for (ImagesLoaderListener listener : imagesLoaderListeners) {
            listener.loadingStarted();
        }
    }

    public static boolean loadImagesCache(Context context) {

        SharedPreferences preferences = context.getSharedPreferences(
                UtilImages.PREFERENCE_NAME, Context.MODE_PRIVATE);

        String json = preferences.getString(UtilImages.PREFERENCE_KEY_JSON, "");

        if (!json.isEmpty()) {
            updateImageData(json);
            callLoadingEnd();
            return true;
        } else {
            return false;
        }


    }

    private static void updateImageData(String json) {
        try {
            JSONObject urlJson = new JSONObject(json);
            imageDetail = parseArray(urlJson);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void callLoadingStart() {

        for (ImagesLoaderListener listener : imagesLoaderListeners) {
            listener.loadingStart();
        }

    }

    private static void callLoadingEnd() {
        for (ImagesLoaderListener listener : imagesLoaderListeners) {
            listener.loadingEnd();
        }

    }

    private static List<ImageDetail> parseArray(JSONObject urlJson) throws JSONException, IOException {

        JSONArray images = urlJson.getJSONArray(DATA);
        List<ImageDetail> result = new ArrayList<>();

        for (int i = 0; i < images.length(); i++) {
            ImageDetail newImage = parseJson(images.getJSONObject(i));
            result.add(newImage);
        }

        return result;
    }

    private static ImageDetail parseJson(JSONObject jsonObject) throws JSONException, IOException {
        ImageDetail newImagen = new ImageDetail();
        newImagen.setTAgs(getTags(jsonObject));
        newImagen.setAuthor(getAuthor(jsonObject));
        newImagen.setPublishDate(getPublishDate(jsonObject));
        newImagen.setImageStandard(getImageStandard(jsonObject));
        newImagen.setImageThumbnail(getImageThumbnail(jsonObject));
        newImagen.setImageLowResolution(getImageLowResolution(jsonObject));
        newImagen.setUrlProfile(getUrlProfile(jsonObject));
        newImagen.setText(getText(jsonObject));


        return newImagen;
    }

    private static String getText(JSONObject jsonObject) throws JSONException {
        if (jsonObject == null) {
            return "";
        }
        if (jsonObject.optJSONObject(CAPTION) == null) {
            return "";
        }
        if (jsonObject.getJSONObject(CAPTION).optString(TEXT) == null) {
            return "";
        }

        return jsonObject.getJSONObject(CAPTION).getString(TEXT);
    }

    private static String getUrlProfile(JSONObject jsonObject) throws JSONException {
        return jsonObject.getString(LINK);
    }

    private static ImageData getImageStandard(JSONObject jsonObject) throws JSONException, IOException {
        ImageData newImage = new ImageData();


        JSONObject standarImageJson = jsonObject.getJSONObject(IMAGES).getJSONObject(STANDARD_RESOLUTION);
        newImage.setUrl(standarImageJson.getString(URL));
        newImage.setWidth(standarImageJson.getInt(WIDTH));
        newImage.setHeight(standarImageJson.getInt(HEIGHT));


        return newImage;
    }

    private static ImageData getImageLowResolution(JSONObject jsonObject) throws JSONException, IOException {
        ImageData newImage = new ImageData();


        JSONObject standarImageJson = jsonObject.getJSONObject(IMAGES).getJSONObject(LOW_RESOLUTION);
        newImage.setUrl(standarImageJson.getString(URL));
        newImage.setWidth(standarImageJson.getInt(WIDTH));
        newImage.setHeight(standarImageJson.getInt(HEIGHT));


        return newImage;
    }

    private static ImageData getImageThumbnail(JSONObject jsonObject) throws JSONException, IOException {
        ImageData newImage = new ImageData();


        JSONObject standarImageJson = jsonObject.getJSONObject(IMAGES).getJSONObject(THUMBNAIL);
        newImage.setUrl(standarImageJson.getString(URL));
        newImage.setWidth(standarImageJson.getInt(WIDTH));
        newImage.setHeight(standarImageJson.getInt(HEIGHT));


        return newImage;
    }


    private static long getPublishDate(JSONObject jsonObject) throws JSONException {
        return jsonObject.getLong(CREATE_TIME);
    }

    private static String getAuthor(JSONObject jsonObject) throws JSONException {
        return jsonObject.getJSONObject(USER).getString(FULL_NAME);
    }

    private static String[] getTags(JSONObject jsonObject) throws JSONException {
        JSONArray array = jsonObject.getJSONArray(TAGS);
        String[] tags = new String[array.length()];
        for (int i = 0; i < array.length(); i++) {
            tags[i] = array.getString(i);
        }
        return tags;
    }

    public static ImageDetail getImagePosition(int position) {

        if (imageDetail == null) {
            return null;
        }
        if (imageDetail.size() == 0) {
            return null;
        }
        if (imageDetail.size() < position) {
            return null;
        }
        return imageDetail.get(position);
    }

    public static void addImagesLoaderListener(ImagesLoaderListener listener) {
        imagesLoaderListeners.add(listener);
        if (updating) {
            listener.loadingStarted();
        }
    }

    public static void removeImagesLoaderListener(ImagesLoaderListener listener) {
        imagesLoaderListeners.remove(listener);
    }

    public static boolean loaderUpdate() {
        return updating;
    }


}

