package com.example.event_retrofit;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.event_retrofit.Retrofit.Interface_API;
import com.example.event_retrofit.Retrofit.Retrofit;
import com.example.event_retrofit.data.Event;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.event_retrofit.UtilClass.isEmpty;

public class UserAreaActivity extends AppCompatActivity {
    TextView welcomeText;

    int user_id;
    Interface_API eventAPI;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(UserAreaActivity.this);
    RecyclerAdapter adapter;
    SpeechRecognizer speechRecognizer;
    Intent intentSpeechRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_area_activity);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Button buttonNewEvent = findViewById(R.id.new_event);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String lastname = intent.getStringExtra("lastname");
        user_id = Integer.parseInt(intent.getStringExtra("id"));
        recyclerView = findViewById(R.id.recyclerView2);
        welcomeText = findViewById(R.id.tv_welcome);
        welcomeText.setText(name + " " + lastname);
        eventAPI = Retrofit.getAPI();

        recyclerView.setLayoutManager(layoutManager);
        read_events();

        buttonNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_event();

            }
        });

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        intentSpeechRecognizer = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intentSpeechRecognizer.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intentSpeechRecognizer.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());


    }

    private void read_events() {
        eventAPI.Read(user_id).enqueue(new Callback<ArrayList<Event>>() {
            @Override
            public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {

                if (adapter == null) {
                    adapter = new RecyclerAdapter();
                    adapter.setArrayList(response.body());
                    recyclerView.setAdapter(adapter);
                    ItemTouchHelper.Callback callback =
                            new SimpleItemTouchHelperCallback(adapter);
                    ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
                    touchHelper.attachToRecyclerView(recyclerView);
                } else {
                    adapter.setArrayList(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Event>> call, Throwable t) {
                UtilClass.makeToast(UserAreaActivity.this, "Ошибка" + t);
            }
        });

    }

    @SuppressLint("ClickableViewAccessibility")
    private void create_event() {
        final Dialog dialog = new Dialog(UserAreaActivity.this);
        dialog.setContentView(R.layout.save_form);
        dialog.setTitle("Введите название:");
        dialog.show();
        final Button buttonOK = (Button) dialog.findViewById(R.id.save_form_bt_OK);
        final EditText et_name = (EditText) dialog.findViewById(R.id.save_form_et_name);
        final EditText description = (EditText) dialog.findViewById(R.id.save_form_et_description);

        final ImageView nameGetAudio = dialog.findViewById(R.id.nameGetAudio);


        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {

                ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null) {
                    et_name.setText(matches.get(0));
                }

            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });


        nameGetAudio.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        et_name.setText("");
                        et_name.setHint("Listening...");
                        speechRecognizer.startListening(intentSpeechRecognizer);

//                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) nameGetAudio.getLayoutParams();
//                        params.width = 40;
//                        params.height = 40;
//                        nameGetAudio.setLayoutParams(params);
                        break;

                    case MotionEvent.ACTION_UP:
                        et_name.setHint("");
                        break;
                }
                return false;
            }
        });


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
                if (UtilsKotlin.preventMultiClick()) {
                    return;
                }
                if (isEmpty(et_name)) {
                    UtilClass.makeToast(UserAreaActivity.this, "Заполните название!");
                    return;
                }
                String to_name = et_name.getText().toString();
                String to_description = description.getText().toString();
                String time = UtilClass.getCurrentTime();
                eventAPI.CreateEvent(to_name, to_description, time, user_id).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
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


interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}






