package es.uja.ejemplostema4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Inicia la actividad de ejemplos de red
     * @param view
     */
    public void onClick(View view){
        Intent networking = new Intent(this, NetworkActivity.class);
        startActivity(networking);
    }
}
