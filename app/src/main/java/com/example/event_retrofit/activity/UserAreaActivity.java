package com.example.event_retrofit.activity;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.event_retrofit.App;
import com.example.event_retrofit.R;
import com.example.event_retrofit.RecyclerAdapter;
import com.example.event_retrofit.Retrofit.Interface_API;
import com.example.event_retrofit.Retrofit.Retrofit;
import com.example.event_retrofit.UtilClass;
import com.example.event_retrofit.UtilsKotlin;
import com.example.event_retrofit.data.Event;
import com.example.event_retrofit.dragAndDrop.SimpleItemTouchHelperCallback;
import java.util.ArrayList;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.example.event_retrofit.ConstantsKt.SORTING_ORDER;
import static com.example.event_retrofit.UtilClass.isEmpty;
import static com.example.event_retrofit.UtilClass.makeToast;
import static com.example.event_retrofit.UtilsKotlin.STORAGE_PERMISSION_CODE;

public class UserAreaActivity extends AppCompatActivity implements RecyclerAdapter.AdapterCallback {
    private static final String CHANNEL_ID = "CHANEL_ID";
    TextView welcomeText;
    int user_id;
    Interface_API eventAPI;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(UserAreaActivity.this);
    RecyclerAdapter adapter;
    SpeechRecognizer speechRecognizer;
    Intent intentSpeechRecognizer;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_area_activity);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String lastname = intent.getStringExtra("lastname");

        Button buttonNewEvent = findViewById(R.id.new_event);
        recyclerView = findViewById(R.id.recyclerView2);
        welcomeText = findViewById(R.id.tv_welcome);
        progressBar = findViewById(R.id.progressBar);


        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
        ) != PackageManager.PERMISSION_GRANTED) {
            UtilsKotlin.requestPermissions(this, getApplicationContext());
        }
        user_id = Integer.parseInt(intent.getStringExtra("id"));

        adapter = new RecyclerAdapter();
        adapter.setArrayList(new ArrayList<>());
        adapter.setContext(UserAreaActivity.this);
        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
        adapter.setAdapterCallback(this);


        welcomeText.setText(name + " " + lastname);
        eventAPI = Retrofit.getAPI();

        recyclerView.setLayoutManager(layoutManager);
        read_events();

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        intentSpeechRecognizer = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intentSpeechRecognizer.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intentSpeechRecognizer.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        buttonNewEvent.setOnClickListener(event -> create_event());


    }

    private void read_events() {
        eventAPI.read(user_id).enqueue(new Callback<ArrayList<Event>>() {
            @Override
            public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
                adapter.setArrayList(response.body());
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<ArrayList<Event>> call, Throwable t) {
                UtilClass.makeToast(UserAreaActivity.this, "Ошибка" + t);
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    @SuppressLint("ClickableViewAccessibility")
    private void create_event() {
        final Dialog dialog = new Dialog(UserAreaActivity.this);
        dialog.setContentView(R.layout.save_form);
        dialog.setTitle("Введите название:");
        dialog.show();

        //set width of dialog MATCH_PARENT
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        int sortingOrder = App.instance.getMySharedPreferences().getInt(SORTING_ORDER, 1);
        final Button buttonOK = (Button) dialog.findViewById(R.id.save_form_bt_OK);
        final EditText et_name = (EditText) dialog.findViewById(R.id.save_form_et_name);
        final EditText description = (EditText) dialog.findViewById(R.id.save_form_et_description);

        final ImageView nameGetAudio = dialog.findViewById(R.id.nameGetAudio);
        final ProgressBar dialogProgress = dialog.findViewById(R.id.dialogProgress);

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
            }

            @Override
            public void onBeginningOfSpeech() {
            }

            @Override
            public void onRmsChanged(float rmsdB) {
            }

            @Override
            public void onBufferReceived(byte[] buffer) {
            }

            @Override
            public void onEndOfSpeech() {
            }

            @Override
            public void onError(int error) {
                makeToast(getApplicationContext(), "Error" + error);
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null) {
                    et_name.setText(matches.get(0));
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

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
                        break;
                    case MotionEvent.ACTION_UP:
                        et_name.setHint("");
                        speechRecognizer.stopListening();
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
                dialogProgress.setVisibility(View.VISIBLE);
                String to_name = et_name.getText().toString();
                String to_description = description.getText().toString();
                String time = UtilClass.getCurrentTime();
                eventAPI.createEvent(to_name, to_description, time, sortingOrder, user_id).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        dialogProgress.setVisibility(View.GONE);

                        SharedPreferences.Editor editor = App.instance.getMySharedPreferences().edit();
                        editor.putInt(SORTING_ORDER, sortingOrder + 1);
                        editor.commit();

                        dialog.dismiss();
                        read_events();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        dialog.dismiss();
                        UtilClass.makeToast(UserAreaActivity.this, "Ошибка" + t);
                        dialogProgress.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    public void logout(View view) {
        App.instance.clearPreferances();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void readEvents() {
        progressBar.setVisibility(View.VISIBLE);
        read_events();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, requestCode);
            }
        }
    }
}







