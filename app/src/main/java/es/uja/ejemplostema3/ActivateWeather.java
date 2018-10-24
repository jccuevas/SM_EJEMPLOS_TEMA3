package es.uja.ejemplostema3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Este receptor de contenido se encarga de iniciar el servicio meteorológico cuando se vuelva a tener conexión
 */
public class ActivateWeather extends BroadcastReceiver {
    public ActivateWeather() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
       if(intent!=null)
       if(intent.getAction()== ConnectivityManager.CONNECTIVITY_ACTION)
       {
           final Intent clima= new Intent(context,WeatherService.class);

           Bundle extra = intent.getExtras();
           ConnectivityManager conn =  (ConnectivityManager)
                   context.getSystemService(Context.CONNECTIVITY_SERVICE);
           // Se obtiene qué red está activa, si es que hay una
           // No se emplea el Intent porque falla en algunas versiones
           NetworkInfo networkInfo = conn.getActiveNetworkInfo();

           if(networkInfo != null &&  networkInfo.isConnected()){
               Toast.makeText(context, context.getString(R.string.service_weather_startup), Toast.LENGTH_SHORT).show();

               context.startService(clima);
           }
           else{
               Toast.makeText(context, context.getString(R.string.service_weather_stop), Toast.LENGTH_SHORT).show();
               context.stopService(clima);
           }



       }
    }
}
