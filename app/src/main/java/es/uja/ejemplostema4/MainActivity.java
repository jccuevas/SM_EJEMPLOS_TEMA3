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
                Intent service = new Intent(this, ServiceActivity.class);
                startActivity(service);
                break;
        }
    }


}
