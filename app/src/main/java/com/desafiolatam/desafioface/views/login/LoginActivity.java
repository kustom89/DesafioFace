package com.desafiolatam.desafioface.views.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.desafiolatam.desafioface.R;
import com.desafiolatam.desafioface.background.ResentUserService;
import com.desafiolatam.desafioface.views.MainActivity;


public class LoginActivity extends AppCompatActivity implements SessionCallback {

    private TextInputLayout mailWrapper, passWraper;
    private EditText mailEt, passEt;
    private Button button;
    private IntentFilter intentFilter;
    private BroadcastReceiver broadcastReceiver;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mailWrapper = findViewById(R.id.emailTil);
        passWraper = findViewById(R.id.passwordTil);
        mailEt = findViewById(R.id.emailEt);
        passEt = findViewById(R.id.passwordEt);
        button = findViewById(R.id.signBtn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mailEt.getText().toString();
                String password = passEt.getText().toString();
                mailWrapper.setVisibility(View.GONE);
                passWraper.setVisibility(View.GONE);
                button.setVisibility(View.GONE);

                new Singin(LoginActivity.this).toServer(email, password);


            }
        });

        intentFilter = new IntentFilter();
        intentFilter.addAction(ResentUserService.USERS_FINISHED);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (ResentUserService.USERS_FINISHED.equals(action)) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            }
        };


    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    private void restoreView() {
        mailEt.setError(null);
        passEt.setError(null);
        mailWrapper.setVisibility(View.VISIBLE);
        passWraper.setVisibility(View.VISIBLE);
        button.setVisibility(View.VISIBLE);

    }


    @Override
    public void requiredField() {
        restoreView();
        mailEt.setError("REQUERIDO");
        passEt.setError("REQUERIDO");
    }

    @Override
    public void mailFormat() {
        restoreView();
        mailEt.setError("FORMATO INCORRECTO");

    }

    @Override
    public void success() {
        ResentUserService.startActionRecentUsers(this);

    }

    @Override
    public void failure() {
        restoreView();
        Toast.makeText(this, "Mail o password incorrecto", Toast.LENGTH_SHORT).show();


    }
}
