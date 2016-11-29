package es.uja.ejemplostema4;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.app.NotificationManager;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServicioConectar extends Service {
    public static final String EXTRA_IP = "servicio_ip";
    public static final String EXTRA_PORT = "servicio_puerto";
    protected String mIp = "150.214.170.105";
    protected int mPuerto = 80;
    private int mId = 1;
    private String mFullData="";
    private Handler mHandler=null;
    private NotificationManager mNotificacionManager=null;

    public ServicioConectar(Handler handler) {
        mHandler=handler;
    }

    public ServicioConectar() {
        mHandler=null;
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

                new Thread(new HebraConectarSimple(direccion)).start();
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

    /**
     * HebraConectarSimple
     * Esta clase permite la conexión con un servidor TCP y la recepcion de la respuesta del mismo
     */
    public class HebraConectarSimple implements Runnable {

        String mResponse = "";
        String mRespuesta = "";
        private InetSocketAddress mIp = null;

        public HebraConectarSimple(InetSocketAddress ip) {
            mIp = ip;
        }

        @Override
        public void run() {
            Socket cliente;
            try {
                //Se crea el socket TCP
                cliente = new Socket();
                //Se realiza la conexión al servidor
                cliente.connect(mIp);
                //Se leen los datos del buffer de entrada
                mostrarNotificacion("Iniciando descarga de "+mIp.toString());
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
