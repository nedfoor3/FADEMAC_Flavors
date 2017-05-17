package com.rawrstudio.fademac.interfaces;

import com.rawrstudio.fademac.models.Usuario;

import io.reactivex.Observable;

/**
 * Created by Ricardo on 16/05/2017.
 */

public interface LoginActivityMVP {

    interface Model{
        Observable<Usuario> validaUsuario(String usuario, String contrasenia);
    }

    interface View{
        void muestraErrorUsuarioContrasenia();

        void ocultaErrorUsuarioContrasenia();

        void muestraProgressbar();

        void ocultaProgressbar();
    }

    interface Presenter{
        void validarCredenciales(String usuario, String contrasenia);
    }

}
