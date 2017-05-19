package com.rawrstudio.fademac;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistrarUsuario extends AppCompatActivity  implements TextWatcher{

    @BindView(R.id.tv_correo)
    TextView tvCorreo;
    @BindView(R.id.tv_contrasenia)
    TextView tvContrasenia;
    @BindView(R.id.tv_contrasenia_validacion)
    TextView tvContraseniaValidacion;
    @BindView(R.id.btn_enviar_datos)
    Button btnEnviarDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);
        ButterKnife.bind(this);

tvContrasenia.addTextChangedListener(this);

    }

    @OnClick(R.id.btn_enviar_datos)
    public void onViewClicked() {
        Toast.makeText(this, "funciona?", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
tvContraseniaValidacion.setText(tvContrasenia.getText());
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
