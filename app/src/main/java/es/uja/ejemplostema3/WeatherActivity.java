package es.uja.ejemplostema3;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class WeatherActivity extends AppCompatActivity {
	private static final String DEBUG_TAG = null;

    public static final int MESSAGE_WEBREAD=1;
    public static final String MESSAGE_WEBREAD_DATA="webreaddata";
    public static final String MESSAGE_WEBREAD_MIMETYPE="webmimetype";


	private Button mStartService = null;


    public static Handler mHandler=null;//Handler para recibir los mensajes de las hebras de trabajo
	
	boolean isConnected = false;



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_weather_activity);



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
			isConnected = true;
			Toast.makeText(this, "Conectado", Toast.LENGTH_LONG).show();// fetch
																		// data
																		// }
		} else { // display error }
			isConnected = false;
			Toast.makeText(this, "No Conectado", Toast.LENGTH_LONG).show();
		}

        mStartService = (Button)findViewById(R.id.service_button_startup);
	}

    private void updateUI(){

        if(isConnected){

            mStartService.setVisibility(View.VISIBLE);
        }else{
            mStartService.setVisibility(View.INVISIBLE);
        }
    }
	/**
	 * Se ejecuta al pulsar el botón conectar
	 * 
	 * @param view
	 */
	public void onNetworkService(View view) {
			Intent clima= new Intent(this,WeatherService.class);
			startService(clima);
	}

    public void onWeather(View view) {
        Toast.makeText(this,"Icon made by pixel-buddha from www.flaticon.com ",Toast.LENGTH_LONG).show();
    }
}
