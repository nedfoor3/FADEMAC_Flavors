package com.rawrstudio.fademac.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.rawrstudio.fademac.R;
import com.rawrstudio.fademac.RegistrarUsuario;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_alta_usuario)
    Button btnAltaUsuario;
    private Button btnAltaJugador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        iniGuiMain();
        iniListenersMain();
    }

    public void iniGuiMain() {
        btnAltaJugador = (Button) findViewById(R.id.btn_alta_jugador);
    }

    public void iniListenersMain() {
        btnAltaJugador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegistroJugador.class));
            }
        });

        btnAltaUsuario.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegistrarUsuario.class));
            }
        });
    }
}
