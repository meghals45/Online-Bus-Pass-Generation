package com.shah.megh.project2k18;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
/**
 * A simple {@link Fragment} subclass.
 */
public class Generate_Pass extends Fragment {

    private WebView webView;

    public Generate_Pass() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_generate__pass, container, false);
        WebView webView = (WebView)v.findViewById(R.id.wvgenpass);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://www.google.com");
        return v;
    }


}
