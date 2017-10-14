package clercky.be.dyxibrowser.fragments;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.ArrayList;

import clercky.be.dyxibrowser.R;
import clercky.be.dyxibrowser.WebViewManager;
import clercky.be.dyxibrowser.font.FontManager;

public class MainBrowserFragment extends Fragment {
    View layout;

    WebViewManager wvm;
    EditText uriEditText;
    ProgressBar loadBar;
    ArrayList<WebViewManager.ScollListener> scrollCalbacks = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.activity_main_browser, container, false);
    }

    @Override
    public void onActivityCreated (Bundle savedInstances) {
        super.onActivityCreated(savedInstances);

        createUI();
    }

    public void createUI() {

        WebView wb = (WebView) getActivity().findViewById(R.id.webview);
        uriEditText = (EditText) getActivity().findViewById(R.id.search_bar);
        loadBar = (ProgressBar) getActivity().findViewById(R.id.loaddBar);

        Typeface font = FontManager.getTypeFace(getActivity().getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(getActivity().findViewById(R.id.wrapper), font, getResources().getString(R.string.fa_tag));

        wvm = new WebViewManager(getActivity(), wb, loadBar);
        wvm.setCallback(new WebViewManager.URLChangedCallback() {
            @Override
            public void onURLChanged(String url) {
                updateUri(url);
            }
        });

        uriEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    onGoPage();
                }
            }
        });
    }

    public void onGoPage() {
        wvm.goPage(uriEditText.getText().toString());
    }

    public void updateUri(String uri) {
        // update uriEditText
        if (uriEditText == null) return;

        uriEditText.setText(uri);
    }

    public void goBack() {
        wvm.goBack();
    }
    public void goForward() { wvm.goForward(); }

    public void setScrollListener(WebViewManager.ScollListener callback) {
        if (wvm == null) {
            scrollCalbacks.add(callback); // save for later
        } else {
            if (callback == null) {
                for (int i = 0; i < scrollCalbacks.size(); i++) {
                    wvm.setScrollListener(scrollCalbacks.get(i));
                }
            } else {
                wvm.setScrollListener(callback);
            }
        }
    }
}
