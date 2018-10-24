package es.uja.ejemplostema3;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class ServiceFTP extends Service {
    public static final String EXTRA_SERVICEFTP_URL="url";

    private NotificationManager mNotificacionManager=null;
    private int mId=-1;//Identificador del servicio
    private String mURL="";

    public ServiceFTP() {
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

        if (intent != null) {
            if (intent.hasExtra(EXTRA_SERVICEFTP_URL)) {
                mURL = intent.getStringExtra(EXTRA_SERVICEFTP_URL);
                mostrarNotificacion("Iniciando descarga de: "+mURL);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        descargaFTP(mURL, "UTF8");
                    }
                }).start();
                return START_REDELIVER_INTENT;

            }
        }

        return START_NOT_STICKY;
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this,"Finalizando servicio "+mId, Toast.LENGTH_SHORT).show();
    }

    private void mostrarNotificacion(String mensaje) {

        // Si se quiere que se inicie la actividad cuando se toque en
        // la notificación se crea un PendingIntent
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, ServiceFTP.class), 0);

        // Preparar la información a mostrar en el panel de notificaciones.
        NotificationCompat.Builder notificacion =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_stat_download) // Icono a mostrar
                        .setContentTitle(getText(R.string.service_basic_label)) //Titulo
                        .setContentText(mensaje)// Contenido
                        .setContentIntent(contentIntent); //Intent a abrir


        // Enviar la notificación
        mNotificacionManager.notify(mId, notificacion.build());
    }

    public void descargaFTP(String destino, final String codificacion) {
        InputStream is;
        byte[] datos = null;
        try {
            URL url = new URL(destino);

            URLConnection urlConnection = url.openConnection();
            is = urlConnection.getInputStream();
            datos = readStream(is, codificacion);
            if (datos != null){
                //final String contenido = new String(datos);
                mostrarNotificacion("Servicio "+mId+" Recurso descargado "+datos.length+" bytes");
                String filepath=url.getFile();
                String filename= filepath.substring(filepath.lastIndexOf("/"),filepath.length());
                saveFile(datos,filename);
            }
            is.close();
        } catch (MalformedURLException exurl) {
            exurl.printStackTrace();
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
    }

    protected void saveFile(byte[] data,String name){
        File download = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File fichero = new File(download,name);
        try {
            FileOutputStream outputStream = new FileOutputStream(fichero);
            outputStream.write(data);
            outputStream.close();
        }catch (FileNotFoundException fnfex){} catch (IOException e) {
            e.printStackTrace();
        }


    }

    public byte[] readStream(InputStream is, final String codificacion) {
        byte[] datos = new byte[64];

        BufferedReader in
                = null;
        try {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);

            while (is.available() > 0) {

                System.out.println(new String(datos));
                baos.write(is.read());
            }
            return baos.toByteArray();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
        return null;
    }
}
