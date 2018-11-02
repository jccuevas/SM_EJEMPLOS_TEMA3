package es.uja.ejemplostema3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class BasicServiceActivity extends AppCompatActivity {


    private EditText mEditUrl=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_servicio);



        mEditUrl = findViewById(R.id.seviceactivity_edit_url);


        Button descargar = findViewById(R.id.seviceactivity_descargar);
        descargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent conecta = new Intent(BasicServiceActivity.this, ServiceFTP.class);
                conecta.putExtra(ServiceFTP.EXTRA_SERVICEFTP_URL, mEditUrl.getEditableText().toString());
                startService(conecta);

            }


        });
    }

}

