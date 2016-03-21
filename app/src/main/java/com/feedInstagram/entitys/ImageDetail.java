package com.feedInstagram.entitys;

/**
 * Created by santiagomedina on 15/03/16.
 */
public class ImageDetail {

    private long publishDate;
    private String author;
    private String[] TAgs;
    private ImageData ImageStandard;
    private String urlProfile;
    private String text;
    private ImageData imageThumbnail;
    private ImageData imageLowResolution;

    public long getPublishDate() {
        return publishDate;
    }

    public String getAuthor() {
        return author;
    }

    public String[] getTAgs() {
        return TAgs;
    }

    public void setPublishDate(long publishDate) {
        this.publishDate = publishDate;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTAgs(String[] TAgs) {
        this.TAgs = TAgs;
    }

    public ImageData getImageStandard() {
        return ImageStandard;
    }

    public void setImageStandard(ImageData imageStandard) {
        ImageStandard = imageStandard;
    }

    public String getUrlProfile() {
        return urlProfile;
    }

    public void setUrlProfile(String urlProfile) {
        this.urlProfile = urlProfile;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ImageData getImageThumbnail() {
        return imageThumbnail;
    }

    public void setImageThumbnail(ImageData imageThumbnail) {
        this.imageThumbnail = imageThumbnail;
    }

    public void setImageLowResolution(ImageData imageLowResolution) {
        this.imageLowResolution = imageLowResolution;
    }

    public ImageData getImageLowResolution() {
        return imageLowResolution;
    }
}
