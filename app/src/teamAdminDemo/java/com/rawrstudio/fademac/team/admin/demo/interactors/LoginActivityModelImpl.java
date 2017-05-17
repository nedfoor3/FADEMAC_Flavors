package com.rawrstudio.fademac.team.admin.demo.interactors;

import android.util.Log;

import com.rawrstudio.fademac.interfaces.LoginActivityMVP;
import com.rawrstudio.fademac.models.Usuario;

import io.reactivex.Observable;

/**
 * Created by Ricardo on 16/05/2017.
 */

public class LoginActivityModelImpl implements LoginActivityMVP.Model {

    private String mUser = "richardo";
    private String mPass = "123";
    @Override
    public Observable<Usuario> validaUsuario(String usuario, String contrasenia) {
        Usuario usuario1 = new Usuario();


        Log.v("TAGGGG", usuario + "  " + contrasenia);
        if (mUser.compareTo(usuario) == 0 && mPass.compareTo(contrasenia) == 0){



            usuario1.setNombre("RICH");

        }
        return Observable.just(usuario1);
    }
}
