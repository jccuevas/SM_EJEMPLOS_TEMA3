package es.uja.ejemplostema4;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class BasicServiceActivity extends AppCompatActivity {


    private EditText mEditUrl=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_servicio);



        mEditUrl = (EditText) findViewById(R.id.seviceactivity_edit_url);


        Button descargar = (Button) findViewById(R.id.seviceactivity_descargar);
        descargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent conecta = new Intent(BasicServiceActivity.this, ServiceFTP.class);
                conecta.putExtra(ServiceFTP.EXTRA_SERVICEFTP_URL, mEditUrl.getEditableText().toString());
                startService(conecta);

            }


        });
    }

}

