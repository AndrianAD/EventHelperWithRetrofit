package com.example.event_retrofit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.event_retrofit.Retrofit.ItemTouchHelperAdapter;
import com.example.event_retrofit.data.Event;

import java.util.ArrayList;
import java.util.Collections;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    public ArrayList<Event> arrayList;
    Context context;



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

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteItem(event, position);

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
