package com.wwu.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import java.util.ArrayList;


public class timerCustomAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<TimerFragment.Timer> list = new ArrayList<>();
    private Context context;

    timerCustomAdapter(ArrayList<TimerFragment.Timer> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            view = inflater.inflate(R.layout.timer_item, null);
        }

        //Display the timer name
        TextView timerName = view.findViewById(R.id.actualTimer);
        timerName.setText(list.get(position).name);

        //display the time remaining in the timer
        final TextView timeRemaining = view.findViewById(R.id.timeRemaining);
        long time = list.get(position).time;
        String timeString = String.format("%02d:%02d:%02d", time / 3600, time / 60, time % 60);
        timeRemaining.setText(timeString);

        //Handle buttons for adding and removing minutes, and pausing timers
        Button plusOne = view.findViewById(R.id.plusOneButton);
        Button minusOne = view.findViewById(R.id.minusOneButton);
        final Button pauseResume = view.findViewById(R.id.pauseResumeButton);

        //+10 seconds on click, +1 minute on long click
        plusOne.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                list.get(position).time += 10;
                notifyDataSetChanged();
            }
        });
        plusOne.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                list.get(position).time += 60;
                notifyDataSetChanged();
                return true;
            }
        });

        //-10 seconds on click, -1 minute on long click
        minusOne.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                list.get(position).time -= 10;
                notifyDataSetChanged();
            }
        });
        minusOne.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                list.get(position).time -= 60;
                notifyDataSetChanged();
                return true;
            }
        });
        pauseResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pauseResume.getText().equals("pause")){
                   pauseResume.setText(view.getResources().getString(R.string.resume));
                }
                else{
                    pauseResume.setText(view.getResources().getString(R.string.pause));
                }
            }
        });


        //todo: this acts weird. starts out very fast before slowing down, then it never really finishes and every once in a while it jumps back up above zero.

        new CountDownTimer(list.get(position).time * 1000, 1) {
            public void onTick(long millisUntilFinished) {
                list.get(position).time = millisUntilFinished / 1000;
                notifyDataSetChanged();
            }
            public void onFinish() {
                list.get(position).name = context.getString(R.string.done);
                notifyDataSetChanged();
                cancel();
            }
        }.start();


        return view;
    }

}