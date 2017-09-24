package clercky.be.dyxibrowser.clients;

import android.content.Context;
import android.util.Base64;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Clercky on 24/09/2017.
 */

public class ChromeClient extends WebChromeClient {
    // private vars
    ProgressBar loadBar;
    Context ctx;
    Boolean canLoadJs = false;
    ChromeClient.ProgressChangedCalback changedCalback;

    // constructors
    public ChromeClient(Context ctx, ProgressBar loadBar) {
        this.loadBar = loadBar;
        this.ctx = ctx;
    }

    // public members
    @Override
    public void onProgressChanged(WebView view, int progress) {
        super.onProgressChanged(view, progress);
        if (changedCalback != null) changedCalback.onProgressChanged(view, progress);

        if (progress < 100 && loadBar.getVisibility() == ProgressBar.GONE) {
            loadBar.setVisibility(ProgressBar.VISIBLE);
        }

        loadBar.setProgress(progress);

        if (progress == 100) {
            loadBar.setVisibility(ProgressBar.GONE);
        }

        if (progress > 25) {
            injectScriptFile(view, "styles.css");
        }
    }

    private void injectScriptFile(WebView view, String scriptFile) {
        //if (!canLoadJs) return;

        canLoadJs = false;
        InputStream input;
        try {
            input = ctx.getAssets().open(scriptFile);
            byte[] buffer = new byte[input.available()];
            input.read(buffer);
            input.close();

            // String-ify the script byte-array using BASE64 encoding !!!
            String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
            view.loadUrl("javascript:(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "var script = document.createElement('style');" +
                    "script.type = 'text/css';" +
                    // Tell the browser to BASE64-decode the string into your script !!!
                    "script.innerHTML = window.atob('" + encoded + "');" +
                    "parent.appendChild(script)" +
                    "})()");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // public accessors
    public void setOnProgressChanged(ChromeClient.ProgressChangedCalback callback) {
        changedCalback = callback;
    }

    // interfaces
    public interface ProgressChangedCalback {
        public void onProgressChanged(WebView webview, int progress);
    }
}
