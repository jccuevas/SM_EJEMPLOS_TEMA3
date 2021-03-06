package es.uja.ejemplostema3;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    public static final String CHANNEL_ID = "weatherservice";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Inicia la actividad de ejemplos de red
     *
     * @param view
     */
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.main_launch_example1:
                Intent networking = new Intent(this, NetworkActivity.class);
                startActivity(networking);
                break;
            case R.id.main_launch_example2:
                Intent serviciobasico = new Intent(this, BasicServiceActivity.class);
                startActivity(serviciobasico);
                break;
            case R.id.main_launch_example3:
                Intent serviceW = new Intent(this, WeatherActivity.class);
                startActivity(serviceW);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        createNotificationChannel(this,"weather","weather service");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {

            new AboutDialog().show(getSupportFragmentManager(),"about");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public static void createNotificationChannel(Context context, CharSequence name, String description) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    public static class AboutDialog extends DialogFragment{
        WebView mAbout= null;
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            View dialog = inflater.inflate(R.layout.dialog_about, null);
            mAbout = dialog.findViewById(R.id.dialog_about_webview);
            loadInfo();
            builder.setView(dialog)
                    // Add action buttons
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                           dismiss();
                        }
                    });
            return builder.create();
        }


        private void loadInfo(){
            mAbout.loadUrl("file:///android_asset/www/help.html");
        }

    }
}
