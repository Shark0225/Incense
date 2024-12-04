package jec.ac.jp.incense;

public class FavoriteItem {

    private String title;
    private int imageResId;

    public FavoriteItem(String title, int imageResId) {
        this.title = title;
        this.imageResId = imageResId;
    }

    public String getTitle() {
        return title;
    }

    public int getImageResId() {
        return imageResId;
    }
}
