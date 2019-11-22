package com.example.event_retrofit;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.event_retrofit.Retrofit.Retrofit;
import com.example.event_retrofit.Retrofit.Interface_API;
import com.example.event_retrofit.data.App_User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    static SharedPreferences sharedPreferences;
    static EditText etEmail, etPassword;
    private static String SHARED_EMAIL;
    private static String SHARED_PASSWORD;

    public static void clearPreferances() {
        sharedPreferences.edit().remove(SHARED_EMAIL).remove(SHARED_PASSWORD).commit();
        etPassword.setText("");
        etEmail.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final TextView tvRegisterLink = (TextView) findViewById(R.id.tvRegisterLink);
        final Button bLogin = (Button) findViewById(R.id.bSignIn);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        sharedPreferences = getPreferences(MODE_PRIVATE);
        if (sharedPreferences.contains(SHARED_EMAIL)) {
            loadPreferances();
            loginCheck();
        }

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
        Interface_API loginAPI = Retrofit.getAPI();
        loginAPI.Login(email, password).enqueue(new Callback<List<App_User>>() {
            @Override
            public void onResponse(Call<List<App_User>> call, Response<List<App_User>> response) {
                if (response.body()!=null) {
                    String name = UtilClass.firstUpperCase(response.body().get(0).getName());
                    String lastname = UtilClass.firstUpperCase(response.body().get(0).getLastName());
                    String id = response.body().get(0).getId();
                    UtilClass.makeToast(MainActivity.this, "Welcome " + name + " " + lastname);

                    if (response.body().get(0).getRole().equals("user")) {
                        savePreferances();
                        makeIntent(id, name, lastname, UserAreaActivity.class);
                    }
                    if (response.body().get(0).getRole().equals("admin")) {
                        savePreferances();
                        makeIntent(id, name, lastname, AdminAreaActivity.class);
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Login Failed")
                            .setNegativeButton("Retry", null)
                            .create()
                            .show();
                }
            }

            @Override
            public void onFailure(Call<List<App_User>> call, Throwable t) {
                UtilClass.makeToast(MainActivity.this, "Ошибка" + t);
            }
        });
    }

    private void makeIntent(String id, String name, String lastname, Class toClass) {
        Intent intent = new Intent(MainActivity.this, toClass);
        intent.putExtra("name", name);
        intent.putExtra("lastname", lastname);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    private void savePreferances() {
        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_EMAIL, email);
        editor.putString(SHARED_PASSWORD, password);
        editor.commit();
    }

    private void loadPreferances() {
        sharedPreferences = getPreferences(MODE_PRIVATE);
        String email = sharedPreferences.getString(SHARED_EMAIL, "");
        String password = sharedPreferences.getString(SHARED_PASSWORD, "");
        etEmail.setText(email);
        etPassword.setText(password);
    }


}





