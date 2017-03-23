package labs.hellocache;

import android.graphics.Bitmap;
import android.media.Image;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

/**
 * Created by abhinav.srivastava on 23/03/17.
 */

public class CustomImageLoader {
    private ImageLoader.ImageCache imageCache = null;
    private LruCache<String, Bitmap> lrucache;

    public ImageLoader getImageLoader(RequestQueue mRequestQueue) {
        return new ImageLoader(mRequestQueue, getImageCache());
    }

    public LruCache<String, Bitmap> getLRUCache(){
        if(lrucache == null){
            lrucache = new LruCache<String, Bitmap>(20);
        }
        return lrucache;
    }

    public ImageLoader.ImageCache getImageCache(){
        if(imageCache == null){
            imageCache = new ImageLoader.ImageCache() {
                @Override
                public Bitmap getBitmap(String url) {return getLRUCache().get(url);}

                @Override
                public void putBitmap(String url, Bitmap bitmap) {getLRUCache().put(url, bitmap);}
            };
        }
        return imageCache;
    }
}
