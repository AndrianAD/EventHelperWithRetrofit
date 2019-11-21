package com.example.event_retrofit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.event_retrofit.data.Event;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    ArrayList<Event> arrayList = new ArrayList<>();
    Context context;

    public RecyclerAdapter(ArrayList new_arrayList) {
        arrayList = new_arrayList;
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

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Вы уверены?")
                        .setCancelable(false)
                        .setNegativeButton("NO",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                }).setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                UtilClass.deleteEvent(context, event.getEvent_id());
                                arrayList.remove(position);
                                notifyDataSetChanged();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton deleteButton;
        private TextView name, description, time;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.tv_name);
            description = view.findViewById(R.id.tv_description);
            time = view.findViewById(R.id.tv_time);
            deleteButton = view.findViewById(R.id.ib_delete);
        }
    }


}
