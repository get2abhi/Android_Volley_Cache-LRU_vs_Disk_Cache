package labs.hellocache;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;

public class MainActivity extends AppCompatActivity {
    private long mRequestStartTime;
    private String url = "http://verocento.com/images/slides/1.jpg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((Button)findViewById(R.id.fetch)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ImageView)findViewById(R.id.image)).setImageBitmap(null);
                ((TextView)findViewById(R.id.result)).setText("Fetching Image ...");
                mRequestStartTime = System.currentTimeMillis();
                if(!((CheckBox)findViewById(R.id.uselrucache)).isChecked()){
                    ImageRequest request = new ImageRequest(
                            url,
                            new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap response) {
                                    long totalRequestTime = System.currentTimeMillis() - mRequestStartTime;
                                    ((ImageView)findViewById(R.id.image)).setImageBitmap(response);
                                    ((TextView)findViewById(R.id.result)).setText("Image fetched using ImageRequest(Disk Cache) in "+totalRequestTime+" ms");
                                }
                            },
                            1000,
                            1000,
                            ImageView.ScaleType.CENTER,
                            Bitmap.Config.RGB_565,
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });
                    Queue.getInstance(MainActivity.this).addToRequestQueue(request);
                }else{
                    Queue.getInstance(MainActivity.this).getImageLoader().get(url, new ImageLoader.ImageListener() {
                        @Override
                        public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                            long totalRequestTime = System.currentTimeMillis() - mRequestStartTime;
                            ((ImageView)findViewById(R.id.image)).setImageBitmap(response.getBitmap());
                            ((TextView)findViewById(R.id.result)).setText("Image fetched using ImageLoader(LRU Cache) in "+totalRequestTime+" ms");
                        }

                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                }
            }
        });

        ((Button)findViewById(R.id.clear)).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                Queue.getInstance(MainActivity.this).getCustomImageLoader().getLRUCache().evictAll();
                Utils.deleteCache(MainActivity.this);
            }
        });
    }
}
