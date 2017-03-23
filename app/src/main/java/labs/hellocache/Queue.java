package labs.hellocache;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by abhi on 1/5/16.
 */
public class Queue {
    private static Queue mInstance;
    private static Context mCtx;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private CustomImageLoader customImageLoader;

    private Queue(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
        customImageLoader = new CustomImageLoader();
        mImageLoader = customImageLoader.getImageLoader(mRequestQueue);
    }


    public static synchronized Queue getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Queue(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());//, new ProxiedHurlStack());//use this if using proxy
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public CustomImageLoader getCustomImageLoader() {
        return customImageLoader;
    }
}