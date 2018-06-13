package com.desafiolatam.desafioface.views.splash;

import com.desafiolatam.desafioface.data.CurrentUsersQueries;

public class LoginValidation {

    private LoginCallback callback;

    public LoginValidation( LoginCallback callback) {
        this.callback=callback;

    }


    public void init(){

        if(new CurrentUsersQueries().isLogged()){
            callback.signed();
        }else{
            callback.signup();
        }

    }
}
