package clercky.be.dyxibrowser.font;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Clercky on 30/09/2017.
 */

public class FontManager {

    public final static String root = "fonts/";
    public final static String FONTAWESOME = root + "fontawesome-webfont.ttf";
    public final static String OPEN_DYSLEXIC = root + "OpenDyslexic-Regular.otf";
    private static Context context;

    public static Typeface getTypeFace(Context ctx, String font) {
        context = ctx;
        return Typeface.createFromAsset(ctx.getAssets(), font);
    }

    public static void markAsIconContainer(View v, Typeface font, String tag) {
        if (v instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v;

            for (int i = 0;  i < vg.getChildCount(); i++) {
                View child = vg.getChildAt(i);
                markAsIconContainer(child, font, tag);
            }
        } else if (v instanceof TextView) {
            TextView tv = (TextView) v;
            if (tv.getTag() != null && tv.getTag().equals(tag))
                tv.setTypeface(font);
        } else if (v instanceof Button) {
            Button tv = (Button) v;
            if (tv.getTag() != null && tv.getTag().equals(tag))
                tv.setTypeface(font);
        }
    }
}
