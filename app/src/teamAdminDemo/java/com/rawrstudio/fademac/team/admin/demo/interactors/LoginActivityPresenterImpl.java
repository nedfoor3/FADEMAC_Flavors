package com.rawrstudio.fademac.team.admin.demo.interactors;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.widget.Toast;

import com.rawrstudio.fademac.interfaces.LoginActivityMVP;
import com.rawrstudio.fademac.models.Usuario;
import com.rawrstudio.fademac.views.MainActivity;

import org.reactivestreams.Subscription;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by Ricardo on 16/05/2017.
 */

public class LoginActivityPresenterImpl implements LoginActivityMVP.Presenter {

    private LoginActivityMVP.View view;
    private Activity context;
    private LoginActivityMVP.Model model;
    private Subscription subscription = null;



    public LoginActivityPresenterImpl(Activity view) {
        this.view = (LoginActivityMVP.View) view;
        this.context = view;
        model = new LoginActivityModelImpl();
    }
    @Override
    public void validarCredenciales(String usuario, final String contrasenia) {
        if (view != null){
            view.muestraProgressbar();
            /*subscription =*/ model.validaUsuario(usuario,contrasenia).subscribe(new Observer<Usuario>() {

                @Override
                public void onSubscribe(@NonNull Disposable d) {

                }

                @Override
                public void onNext(@NonNull final Usuario usuario) {


                    new CountDownTimer(2000, 1000) {
                        public void onFinish() {
                            // When timer is finished
                            // Execute your code here
                            if (usuario.getNombre()== null){
                                view.muestraErrorUsuarioContrasenia();

                            }else {
                                context.startActivity(new Intent(context, MainActivity.class));

                            }

                            view.ocultaProgressbar();
                        }

                        public void onTick(long millisUntilFinished) {
                            // millisUntilFinished    The amount of time until finished.
                        }
                    }.start();

                }


                @Override
                public void onError(@NonNull Throwable e) {

                }


                @Override
                public void onComplete() {
                    //aqui se oculta progressbar
                    // view.ocultaProgressbar();


                }
            });
        }

    }
}
