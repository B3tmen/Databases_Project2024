package org.unibl.etf.util;

public class ImagePaths {
    private static final String c = "/";

    // folder paths
    //private static final String RESOURCES_FOLDER = ".." + c + "resources" + c;      // in terms of where Main is
    private static final String MEDIA_FOLDER = c + "media" + c;
    public static final String IMAGES_FOLDER = MEDIA_FOLDER + "images" + c;
    private static final String PRODUCTS_FOLDER = IMAGES_FOLDER + "products" + c;


    // general images
    public static final String APP_ICON = IMAGES_FOLDER + "app-icon.png";
    public static final String STORE_LOGO = IMAGES_FOLDER + "store-logo.png";
    public static final String USER_PROFILE_LOGO = IMAGES_FOLDER + "user-profile-img.png";
    public static final String ALERT_CUSTOM_CHECKMARK = IMAGES_FOLDER + "alert-checkmark-icon.png";


    // products (for testing purposes)
    public static final String PRODUCT_DEFAULT_IMAGE = PRODUCTS_FOLDER + "image-missing.jpg";
}
