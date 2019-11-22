package com.example.event_retrofit;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import com.example.event_retrofit.Retrofit.Interface_API;
import com.example.event_retrofit.Retrofit.Retrofit;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class UtilClass {
    static boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    static void makeToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }


    static String firstUpperCase(String word) {
        if (word == null || word.isEmpty()) return "";
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    static String getCurrentTime() {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
    }

    static void deleteEvent(final Context context, String event_id) {
        Interface_API eventAPI = Retrofit.getAPI();
        eventAPI.Delete(Integer.parseInt(event_id)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                UtilClass.makeToast(context, "Ошибка" + t);
            }
        });


    }
}
