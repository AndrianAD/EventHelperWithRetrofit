package com.example.event_retrofit;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.event_retrofit.Retrofit.GoRetrofit;
import com.example.event_retrofit.Retrofit.Interface_API;
import com.example.event_retrofit.data.App_User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    EditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final TextView tvRegisterLink = (TextView) findViewById(R.id.tvRegisterLink);
        final Button bLogin = (Button) findViewById(R.id.bSignIn);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);



        tvRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginCheck();
            }
        });
    }










    private void loginCheck() {
        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();
        Interface_API loginAPI= GoRetrofit.getAPI();
        loginAPI.Login(email,password).enqueue(new Callback<List<App_User>>() {
            @Override
            public void onResponse(Call<List<App_User>> call, Response<List<App_User>> response) {
                if(!response.body().isEmpty()) {
                    String name=UtilClass.firstUpperCase(response.body().get(0).getName());
                    String lastname=UtilClass.firstUpperCase(response.body().get(0).getLastName());
                    UtilClass.makeToast(MainActivity.this, "Welcome " + name +" "+ lastname);


                    Intent intent = new Intent(MainActivity.this, UserAreaActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("lastname", lastname);
                    intent.putExtra("id", response.body().get(0).getId());
                    savePreferances();
                    startActivity(intent);
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Login Failed")
                            .setNegativeButton("Retry", null)
                            .create()
                            .show();
                }
            }

            @Override
            public void onFailure(Call<List<App_User>> call, Throwable t) {
                 UtilClass.makeToast(MainActivity.this, "Ошибка"+t);
            }
        });
    }

    private void savePreferances() {
    }


}





