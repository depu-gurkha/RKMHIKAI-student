package online.rkmhikai.ui.website;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import online.rkmhikai.R;
import online.rkmhikai.config.SharedPrefManager;

public class Website extends Fragment {
    WebView webView;

    private WebsiteViewModel mViewModel;

    public static Website newInstance() {
        return new Website();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.website_fragment, container, false);
        webView=view.findViewById(R.id.web_website);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new  MyWebViewClient());
        webView.loadUrl(SharedPrefManager.getInstance(getContext()).getServerAddress());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(WebsiteViewModel.class);
        // TODO: Use the ViewModel
    }
    // Subclase WebViewClient() para Handling Page Navigation
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Uri.parse(url).getHost().equals(SharedPrefManager.getInstance(getContext()).getServerAddress())) { //Force to open the url in WEBVIEW
                return false;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }

}