package com.rawrstudio.fademac.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rawrstudio.fademac.R;
import com.rawrstudio.fademac.interfaces.LoginActivityMVP;
import com.rawrstudio.fademac.team.admin.demo.interactors.LoginActivityPresenterImpl;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements LoginActivityMVP.View {

    LoginActivityMVP.Presenter presenter;

    @BindView(R.id.tv_usuario)
    EditText tvUsuario;
    @BindView(R.id.tv_contrasenia)
    EditText tvContrasenia;
    @BindView(R.id.btn_ingresar)
    Button btnIngresar;
    @BindView(R.id.tv_error_credenciales)
    TextView tvErrorCredenciales;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;
    @BindView(R.id.progressbar_layout)
    RelativeLayout progressbarLayout;
    @BindView(R.id.layout_principal)
    RelativeLayout layoutPrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        presenter = new LoginActivityPresenterImpl(this);

    }

    @Override
    public void muestraErrorUsuarioContrasenia() {
        tvErrorCredenciales.setVisibility(View.VISIBLE);
        tvUsuario.setText("");
        tvContrasenia.setText("");
    }

    @Override
    public void ocultaErrorUsuarioContrasenia() {
        tvErrorCredenciales.setVisibility(View.INVISIBLE);


    }

    @Override
    public void muestraProgressbar() {
        tvUsuario.setEnabled(false);
        tvContrasenia.setEnabled(false);
        btnIngresar.setEnabled(false);
        progressbarLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void ocultaProgressbar() {
        tvUsuario.setEnabled(true);
        tvContrasenia.setEnabled(true);
        btnIngresar.setEnabled(true);
        progressbarLayout.setVisibility(View.INVISIBLE);
    }

    public void validaUsuario(View view) {
        ocultaErrorUsuarioContrasenia();
        presenter.validarCredenciales(tvUsuario.getText().toString(), tvContrasenia.getText().toString());


    }
}
