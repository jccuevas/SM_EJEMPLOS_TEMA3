package es.uja.ejemplostema3;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class FragmentoInfo extends Fragment {

	private WebView mInfo = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View fragment = inflater.inflate(R.layout.layout_fragment_info, null);

        mInfo = (WebView) fragment.findViewById(R.id.fragmentinfo_helptext);


        mInfo.loadUrl("file:///android_asset/www/help.html");
        return fragment;
		
	}

}
