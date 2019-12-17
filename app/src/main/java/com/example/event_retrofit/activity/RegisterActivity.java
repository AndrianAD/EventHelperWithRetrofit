package com.example.event_retrofit.activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.event_retrofit.R;
import com.example.event_retrofit.Retrofit.Retrofit;
import com.example.event_retrofit.Retrofit.Interface_API;
import com.example.event_retrofit.UtilClass;
import com.example.event_retrofit.data.App_User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.event_retrofit.UtilClass.isEmpty;


public class RegisterActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText et_name = findViewById(R.id.name);
        final EditText et_lastName = findViewById(R.id.lastname);
        final EditText et_email = findViewById(R.id.email);
        final EditText et_password = findViewById(R.id.password);
        final Button btn_register = findViewById(R.id.btn_register);


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(isEmpty(et_name)||isEmpty(et_lastName)||isEmpty(et_email)||isEmpty(et_password)) {
                    Toast.makeText(RegisterActivity.this, "Заполните все поля !", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String name = et_name.getText().toString();
                final String lastName = et_lastName.getText().toString();
                final String email = et_email.getText().toString();
                final String password = et_password.getText().toString();
                App_User app_user=new App_User(name,lastName,email,password);
                registerRequest(app_user);

            }
        });

    }

    private void registerRequest(App_User app_user) {
        Interface_API registerAPI= Retrofit.getAPI();
        registerAPI.Register(app_user.getName(),app_user.getLastName(),app_user.getPassword(),app_user.getEmail()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
             if(response.isSuccessful()){
                 UtilClass.makeToast(RegisterActivity.this,response.body().toString());
             }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {


            }
        });

    }
}


