package es.uja.ejemplostema4;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class WeatherService extends Service {
   // protected String mWeatherWeb = "https://weather.com/es-ES/tiempo/hoy/l/SPXX1343:1:SP";
   protected String mWeatherWeb = "http://www.accuweather.com/es/es/linares/306737/weather-forecast/306737";
    public static final String KEY_START = "<span class=\"large-temp\">";

    private int mId = 1;
    private NotificationManager mNotificacionManager = null;

    public static final String DEBUG_TAG = "ServicioConectar";


    public WeatherService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        mId = startId; //Se guarda el identificador para poder parar el servicio
        // si fuera necesario

        Toast.makeText(this, getString(R.string.service_weather_startup)+" ID="+mId, Toast.LENGTH_SHORT).show();

        return START_STICKY;
    }

    /**
     * Inicia la hebra de comprobación de notificaciones, solo se crea una hebra por servicio ya
     * que onCreate solo se llama una vez en su ciclo de vida
     */
    @Override
    public void onCreate() {
        super.onCreate();
        mNotificacionManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        new Thread(new HebraConectarSimple(mWeatherWeb)).start();
    }

    private void mostrarNotificacion(String mensaje) {

        // Si se quiere que se inicie la actividad cuando se toque en
        // la notificación se crea un PendingIntent
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, WeatherService.class), 0);

        // Preparar la información a mostrar en el panel de notificaciones.
        NotificationCompat.Builder notificacion =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_weather) // Icono a mostrar
                        .setContentTitle(getText(R.string.service_weather_label)) //Titulo
                        .setContentText(mensaje)// Contenido
                        .setContentIntent(contentIntent); //Intent a abrir


        // Enviar la notificación
        mNotificacionManager.notify(mId, notificacion.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mNotificacionManager.cancel(mId);
        Toast.makeText(this, "Finalizando servicio...", Toast.LENGTH_SHORT).show();
    }


    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        String result = "";
        String contentAsString = "";

        HttpURLConnection conn = null;
        try {

            String tempString = "";
            URL url = new URL(myurl);
            System.out.println("Abriendo conexión: " + url.getHost()
                    + " puerto=" + url.getPort());
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            final int response = conn.getResponseCode();

            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            while ((tempString = br.readLine()) != null) {
                contentAsString = contentAsString + tempString;

            }
            //Convert the InputStream into a string


            int index = contentAsString.indexOf(KEY_START);
            if (index >= 0) {

                contentAsString = contentAsString.substring(index);
                index=contentAsString.indexOf("&");
                if (index >= 0) {
                    String temp = contentAsString.substring(KEY_START.length(), index);
                    mostrarNotificacion("Temperatura en Linares "+temp+"º C");
                }

            }


        } catch (IOException e) {
            result = "Excepción: " + e.getMessage();
            System.out.println(result);
            stopSelf();
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
                conn.disconnect();
            }
        }
        return result;
    }

    /**
     * HebraConectarSimple
     * Esta clase permite la conexión con un servidor TCP y la recepcion de la respuesta del mismo
     */
    public class HebraConectarSimple implements Runnable {
        private String mURL = null;


        public HebraConectarSimple(String url) {
            mURL = url;
        }


        @Override
        public void run() {

            while (true) {
                try {
                    downloadUrl(mURL);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(60000);//Actuliza cada minuto
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }


}
