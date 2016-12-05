package es.uja.ejemplostema4;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;

public class ServiceActivity extends AppCompatActivity {
	private static final String DEBUG_TAG = null;

    public static final int MESSAGE_WEBREAD=1;
    public static final String MESSAGE_WEBREAD_DATA="webreaddata";
    public static final String MESSAGE_WEBREAD_MIMETYPE="webmimetype";

	private ProgressBar progressBar=null;
	private NetworkWebFragment web = null;
	private EditText	postParams=null;
	private WebView 	webView=null;


    public static Handler mHandler=null;//Handler para recibir los mensajes de las hebras de trabajo
	
	boolean conectado = false;



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_service);



        mHandler = new Handler() {
            @Override
            public void handleMessage(Message inputMessage) {
                String datos = "";
                String mimeType = "";
                // Obtiene el mensaje de la hebra de conexión.
                switch (inputMessage.what) {
                    case MESSAGE_WEBREAD:
                        datos = inputMessage.getData().getString(MESSAGE_WEBREAD_DATA);
                        mimeType = inputMessage.getData().getString(MESSAGE_WEBREAD_MIMETYPE);

                        if (datos != null) {


                            Log.d("Handler", "Recibido: " + datos);

                        }
                        break;
                }
            }

        };

		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			conectado = true;
			Toast.makeText(this, "Conectado", Toast.LENGTH_LONG).show();// fetch
																		// data
																		// }
		} else { // display error }
			conectado = false;
			Toast.makeText(this, "No Conectado", Toast.LENGTH_LONG).show();
		}

	}

	/**
	 * Se ejecuta al pulsar el botón conectar
	 * 
	 * @param view
	 */
	public void onNetworkService(View view) {
		FragmentManager fm = getSupportFragmentManager();
		NetworkURLFragment urifragment = (NetworkURLFragment) fm
				.findFragmentById(R.id.layout_fragment_network_URL);

		if (urifragment != null) {
			Intent conecta= new Intent(this,ServicioConectar.class);
			conecta.putExtra(ServicioConectar.EXTRA_IP,urifragment.getURI().getHost());
			conecta.putExtra(ServicioConectar.EXTRA_PORT,urifragment.getURI().getPort());
			startService(conecta);
		}
	}
}
