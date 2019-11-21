package com.example.event_retrofit;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.event_retrofit.Retrofit.Retrofit;
import com.example.event_retrofit.Retrofit.Interface_API;
import com.example.event_retrofit.data.Event;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.event_retrofit.UtilClass.isEmpty;


public class UserAreaActivity extends AppCompatActivity {
    TextView welcomeText;
    int user_id;
    Interface_API eventAPI;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_area_activity);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String lastname = intent.getStringExtra("lastname");
        user_id = Integer.parseInt(intent.getStringExtra("id"));
        recyclerView = findViewById(R.id.recyclerView2);

        welcomeText = findViewById(R.id.tv_welcome);
        welcomeText.setText(name + " " + lastname);

        eventAPI = Retrofit.getAPI();
        read_events();


    }


    private void read_events() {
        eventAPI.Read(user_id).enqueue(new Callback<ArrayList<Event>>() {
            @Override
            public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
                UtilClass.makeToast(UserAreaActivity.this, "ВСЕ ОК");
                final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(UserAreaActivity.this);
                RecyclerView.Adapter adapter = new RecyclerAdapter(response.body());
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(layoutManager);
            }

            @Override
            public void onFailure(Call<ArrayList<Event>> call, Throwable t) {
                UtilClass.makeToast(UserAreaActivity.this, "Ошибка" + t);
            }
        });

    }

    private void create_event() {
        final Dialog dialog = new Dialog(UserAreaActivity.this);
        dialog.setContentView(R.layout.save_form);
        dialog.setTitle("Введите название:");
        dialog.show();
        final Button buttonOK = (Button) dialog.findViewById(R.id.save_form_bt_OK);
        final EditText et_name = (EditText) dialog.findViewById(R.id.save_form_et_name);
        final EditText description = (EditText) dialog.findViewById(R.id.save_form_et_description);
        et_name.post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager =
                        (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInputFromWindow(
                        et_name.getApplicationWindowToken(), InputMethodManager.SHOW_IMPLICIT, 0);
                et_name.requestFocus();
            }
        });
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty(et_name) || isEmpty(description)) {
                    UtilClass.makeToast(UserAreaActivity.this, "Заполните все поля !");
                    return;
                }
                String to_name = et_name.getText().toString();
                String to_description = description.getText().toString();
                String time = UtilClass.getCurrentTime();
                eventAPI.CreateEvent(to_name, to_description, time, user_id).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        UtilClass.makeToast(UserAreaActivity.this, "Сделано");
                        dialog.dismiss();
                        read_events();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        UtilClass.makeToast(UserAreaActivity.this, "Ошибка" + t);
                    }
                });


            }
        });
    }

    public void newEvent(View view) {
        create_event();
    }

    public void logout(View view) {
        MainActivity.clearPreferances();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}





