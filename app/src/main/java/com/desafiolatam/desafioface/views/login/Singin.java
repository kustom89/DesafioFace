package com.desafiolatam.desafioface.views.login;

import android.os.AsyncTask;
import android.util.Log;

import com.desafiolatam.desafioface.models.CurrentUser;
import com.desafiolatam.desafioface.networks.sessions.LoginInterceptor;
import com.desafiolatam.desafioface.networks.sessions.Session;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class Singin {

    private SessionCallback callback;

    public Singin(SessionCallback callback){
        this.callback=callback;
    }

    public void toServer(String email, String password){
        Log.d("Login", email);
        Log.d("Login", password);
        if(email.trim().length()<=0 && password.trim().length()<=0){
            callback.requiredField();
        }else {
            if(!email.contains("@")){
                callback.mailFormat();
            }else {
                new ExampleTask().execute(email, password);
                /*Session session= new LoginInterceptor().get();
                Call<CurrentUser> call=session.loggin(email,password);
                call.enqueue(new Callback<CurrentUser>() {
                    @Override
                    public void onResponse(Call<CurrentUser> call, Response<CurrentUser> response) {
                        if(200 == response.code() && response.isSuccessful()){
                            CurrentUser user = response.body();
                            user.create();
                            callback.success();
                        }else{
                            Log.d("Login", "callback");
                            callback.failure();
                        }
                    }

                    @Override
                    public void onFailure(Call<CurrentUser> call, Throwable t) {
                        Log.d("Login", "failure");
                        callback.failure();

                    }
                });*/


            }
        }
    }

    private static class ExampleTask extends AsyncTask<String, Void, CurrentUser> {

        @Override
        protected CurrentUser doInBackground(String... strings) {
            String email = strings[0];
            String pass = strings[1];

            Session session = new LoginInterceptor().get();
            Call<CurrentUser> call = session.loggin(email, pass);
            try {
                Response<CurrentUser> response = call.execute();
                Log.d("Login", "execute");
                Log.d("Login", String.valueOf(response.code()));

            } catch (IOException e) {
                Log.d("Login", "exception");
                e.printStackTrace();
            }
            return null;
        }
    }
}
