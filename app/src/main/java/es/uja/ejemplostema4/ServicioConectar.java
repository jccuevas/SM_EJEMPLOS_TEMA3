package es.uja.ejemplostema4;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.app.NotificationManager;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

public class ServicioConectar extends Service {
    public static final String EXTRA_IP = "servicio_ip";
    public static final String EXTRA_PORT = "servicio_puerto";
    protected String mIp = "150.214.170.105";
    protected int mPuerto = 80;
    private int mId = 1;
    private String mFullData="";
    private NotificationManager mNotificacionManager=null;

    public static final String DEBUG_TAG = "ServicioConectar";


    public ServicioConectar() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        mId=startId; //Se guarda el identificador para poder parar el servicio
                     // si fuera necesario
        mNotificacionManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Toast.makeText(this,getString(R.string.servicio_nuevo), Toast.LENGTH_SHORT).show();

        if (intent != null) {
            if (intent.hasExtra(EXTRA_IP)) {
                mIp = intent.getStringExtra(EXTRA_IP);
                mPuerto = intent.getIntExtra(EXTRA_PORT, mPuerto);
                InetSocketAddress direccion = new InetSocketAddress(mIp, mPuerto);

                new Thread(new HebraConectarSimple(NetworkActivity.mHandler,direccion)).start();
            }
        }
        return START_NOT_STICKY;
    }


    private void mostrarNotificacion(String mensaje) {

        // Si se quiere que se inicie la actividad cuando se toque en
        // la notificación se crea un PendingIntent
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, ServicioConectar.class), 0);

        // Preparar la información a mostrar en el panel de notificaciones.
        NotificationCompat.Builder notificacion =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_stat_communication) // Icono a mostrar
                        .setContentTitle(getText(R.string.servicio_etiqueta)) //Titulo
                        .setContentText(mensaje)// Contenido
                        .setContentIntent(contentIntent); //Intent a abrir


        // Enviar la notificación
        mNotificacionManager.notify(mId, notificacion.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this,"Finalizando servicio...", Toast.LENGTH_SHORT).show();
    }



    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        String result = "";

        HttpURLConnection conn = null;
        try {
            String contentAsString="";
            String tempString="";
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
            final int contentLength = conn.getHeaderFieldInt("Content-length",1000);
            String mimeType=conn.getHeaderField("Content-Type");
            String encoding=mimeType.substring(mimeType.indexOf(";"));


            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            BufferedReader br = new BufferedReader( new InputStreamReader(is, "UTF-8"));

            while((tempString=br.readLine())!=null)
            {
                contentAsString = contentAsString + tempString;

            }


            //Convert the InputStream into a string

            return contentAsString;
        } catch (IOException e) {
            result = "Excepción: " + e.getMessage();
            System.out.println(result);

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

        InetSocketAddress mServerAddress=null;
        String mResponse = "";
        String mRespuesta = "";
        private InetSocketAddress mIp = null;
        private Handler mHandler=null;


        public HebraConectarSimple(Handler handler, InetSocketAddress ip) {

            mHandler=handler;
            mServerAddress = ip;
        }


        private void enviarWeb(String datos,String mimetype){
            if(mHandler!=null) {
                Message mensaje = Message.obtain(mHandler, NetworkActivity.MESSAGE_WEBREAD);
                Bundle datosmensaje = new Bundle();
                datosmensaje.putString(NetworkActivity.MESSAGE_WEBREAD_DATA, datos);
                datosmensaje.putString(NetworkActivity.MESSAGE_WEBREAD_MIMETYPE, mimetype);
                mensaje.sendToTarget();
            }
        }
        @Override
        public void run() {
            Socket cliente;
            try {
                //Se crea el socket TCP
                cliente = new Socket();
                //Se realiza la conexión al servidor
                cliente.connect(mServerAddress);
                //Se leen los datos del buffer de entrada
                mostrarNotificacion("Iniciando descarga de "+mServerAddress.toString());
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                DataOutputStream dos = new DataOutputStream(cliente.getOutputStream());

                dos.writeUTF("get /index.html HTTP/1.1\r\nHost:www10.ujaen.es\r\n\r\n");
                dos.flush();


                while((mRespuesta = bis.readLine())!=null) {

                    mFullData=mFullData+mRespuesta;


                }
                bis.close();
                dos.close();
                cliente.close();
                mostrarNotificacion("Finalizando conexión con "+mIp.toString());
                enviarWeb(mFullData,"text/html");
            } catch (UnknownHostException e) {
                e.printStackTrace();
                mRespuesta = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                e.printStackTrace();
                mRespuesta = "IOException: " + e.toString();
            } finally {
                stopSelf(mId);
            }
        }
    }


}
