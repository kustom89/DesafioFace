package com.desafiolatam.desafioface.views.login;

import com.desafiolatam.desafioface.models.CurrentUser;
import com.desafiolatam.desafioface.networks.LoginInterceptor;
import com.desafiolatam.desafioface.networks.Session;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Singin {

    private SessionCallback callback;

    public Singin(SessionCallback callback){
        this.callback=callback;
    }

    public void toServer(String email, String password){

        if(email.trim().length()<=0 && password.trim().length()<=0){
            callback.requiredField();
        }else {
            if(!email.contains("@")){
                callback.mailFormat();
            }else {
                Session session= new LoginInterceptor().get();
                Call<CurrentUser> call=session.loggin(email,password);
                call.enqueue(new Callback<CurrentUser>() {
                    @Override
                    public void onResponse(Call<CurrentUser> call, Response<CurrentUser> response) {
                        if(200 == response.code() && response.isSuccessful()){
                            CurrentUser user = response.body();
                            user.create();
                            callback.success();
                        }else{
                            callback.failure();
                        }
                    }

                    @Override
                    public void onFailure(Call<CurrentUser> call, Throwable t) {
                        callback.failure();

                    }
                });


            }
        }
    }
}
