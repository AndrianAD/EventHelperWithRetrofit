package com.example.event_retrofit;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.event_retrofit.Retrofit.Interface_API;
import com.example.event_retrofit.Retrofit.Retrofit;
import com.example.event_retrofit.data.Event;
import com.example.event_retrofit.dragAndDrop.ItemTouchHelperAdapter;
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static android.content.Context.ALARM_SERVICE;
import static com.example.event_retrofit.UtilClass.isEmpty;
import static com.example.event_retrofit.UtilClass.makeToast;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    private ArrayList<Event> arrayList;
    private Context context;
    private Interface_API eventAPI = Retrofit.getAPI();
    private AdapterCallback adapterCallback;

    public void setAdapterCallback(AdapterCallback callback) {
        this.adapterCallback = callback;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public interface AdapterCallback {
        void readEvents();
    }

    public void setArrayList(ArrayList<Event> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.card_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Event event = arrayList.get(position);
        holder.name.setText(event.getName());
        holder.description.setText(event.getDescription());
        holder.time.setText(event.getTime());

        holder.cardView.setOnClickListener(v -> edit(event));
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popup = new PopupMenu(context, holder.cardView, Gravity.RIGHT);
                popup.inflate(R.menu.context_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.notification:

                                new SingleDateAndTimePickerDialog.Builder(context)
                                        .curved()
                                        .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                                            @Override
                                            public void onDisplayed(SingleDateAndTimePicker picker) {
                                            }
                                        })
                                        .minutesStep(1)
                                        .title("Simple")
                                        .listener(new SingleDateAndTimePickerDialog.Listener() {
                                            @Override
                                            public void onDateSelected(Date date) {
                                                makeToast(context, date.toString());

                                                Intent intent = new Intent(context, AlarmReceiver.class);
                                                intent.putExtra("notificationId", 1);
                                                intent.putExtra("todo", event.getName());

                                                PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0,
                                                        intent, PendingIntent.FLAG_CANCEL_CURRENT);
                                                AlarmManager alarm = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                                                Calendar startTime = Calendar.getInstance();
                                                startTime.set(Calendar.DATE, date.getDate());
                                                startTime.set(Calendar.HOUR, date.getHours());
                                                startTime.set(Calendar.MINUTE, date.getMinutes());
                                                long alarmStartTime = startTime.getTimeInMillis();

                                                alarm.set(AlarmManager.RTC_WAKEUP, alarmStartTime, alarmIntent);
                                            }
                                        }).display();

                                return true;
                            case R.id.delete:
                                UtilClass.makeToast(context, "функция не активна");
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.show();
                return false;
            }
        });
    }

    private void edit(Event event) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.save_form);
        dialog.setTitle("Введите название:");
        dialog.show();
        final Button buttonOK = dialog.findViewById(R.id.save_form_bt_OK);
        final EditText et_name = dialog.findViewById(R.id.save_form_et_name);
        final EditText description = dialog.findViewById(R.id.save_form_et_description);
        final ProgressBar dialogProgress = dialog.findViewById(R.id.dialogProgress);

        final ImageView audio = dialog.findViewById(R.id.nameGetAudio);
        audio.setVisibility(View.GONE);

        et_name.setText(event.getName());
        description.setText(event.getDescription());

//            et_name.post(new Runnable() {
//                @Override
//                public void run() {
//                    InputMethodManager inputMethodManager =
//                            (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                    inputMethodManager.toggleSoftInputFromWindow(
//                            et_name.getApplicationWindowToken(), InputMethodManager.SHOW_IMPLICIT, 0);
//                    et_name.requestFocus();
//                }
//            });

        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UtilsKotlin.preventMultiClick()) {
                    return;
                }
                if (isEmpty(et_name)) {
                    UtilClass.makeToast(context, "Заполните название!");
                    return;
                }
                dialogProgress.setVisibility(View.VISIBLE);
                String to_name = et_name.getText().toString();
                String to_description = description.getText().toString();
                int id = Integer.parseInt(event.getEvent_id());
                eventAPI.editEvent(to_name, to_description, id).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        dialog.dismiss();
                        adapterCallback.readEvents();
                        dialogProgress.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        UtilClass.makeToast(context, "Ошибка" + t);
                        dialog.dismiss();
                        notifyDataSetChanged();
                        dialogProgress.setVisibility(View.GONE);

                    }
                });
            }
        });
    }

    private void deleteItem(final Event event, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Вы уверены?")
                .setCancelable(false)
                .setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                notifyDataSetChanged();
                            }
                        }).setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        arrayList.remove(position);
                        notifyItemRemoved(position);
                        UtilClass.deleteEvent(context, event.getEvent_id());

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(arrayList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        final Event event = arrayList.get(position);
        deleteItem(event, position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name, description, time;
        private CardView cardView;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.tv_name);
            description = view.findViewById(R.id.tv_description);
            time = view.findViewById(R.id.tv_time);
            cardView = view.findViewById(R.id.cardView);
        }

    }
}

