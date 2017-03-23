package labs.hellocache;

import android.content.Context;
import android.widget.Toast;

import java.io.File;

/**
 * Created by abhinav.srivastava on 23/03/17.
 */

public class Utils {
    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            Toast.makeText(context, "deleted : "+ emptyDir(dir), Toast.LENGTH_LONG).show();
        } catch (Exception e) {}
    }

    public static boolean emptyDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = emptyDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return true;
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
}
