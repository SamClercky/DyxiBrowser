package clercky.be.dyxibrowser.clients;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import clercky.be.dyxibrowser.UpdateUrl;

/**
 * Created by Clercky on 24/09/2017.
 */

public class WebClient extends WebViewClient {
    UpdateUrl updateObject;

    public WebClient(UpdateUrl updateObject) {
        this.updateObject = updateObject;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
    }
    @Override
    public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
        // Redirect to deprecated method, so you can use it in all SDK versions
        Log.i("WEB_VIEW_TEST", "error code:");
    }
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        updateObject.updateUri(url);
        // TODO: set favicon
    }
}
