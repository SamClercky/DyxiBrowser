package clercky.be.dyxibrowser.clients;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.io.IOException;

import clercky.be.dyxibrowser.UpdateUrl;

/**
 * Created by Clercky on 24/09/2017.
 */

public class WebClient extends WebViewClient {
    private static final String INJECTION_TOKEN = "**injection**";
    UpdateUrl updateObject;
    Context ctx;

    public WebClient(Context ctx, UpdateUrl updateObject) {
        this.updateObject = updateObject;
        this.ctx = ctx;
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
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest req) {
        String url = req.getUrl().toString();

        WebResourceResponse response = super.shouldInterceptRequest(view, req);
        if(url != null && url.contains(INJECTION_TOKEN)) {
            String assetPath = url.substring(url.indexOf(INJECTION_TOKEN) + INJECTION_TOKEN.length(), url.length());
            try {
                response = new WebResourceResponse(
                        "application/javascript",
                        "UTF8",
                        ctx.getAssets().open(assetPath)
                );
            } catch (IOException e) {
                e.printStackTrace(); // Failed to load asset file
            }
        }
        return response;
    }
}
