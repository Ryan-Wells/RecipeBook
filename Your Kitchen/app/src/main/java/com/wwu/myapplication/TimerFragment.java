package com.wwu.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;


public class TimerFragment extends Fragment {

    public TimerFragment(){
        //required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_timer, container, false);

        final Button closeButton = view.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().remove(TimerFragment.this).commit();
            }
        });

        ArrayList<Timer> testArray = new ArrayList<>();
        testArray.add(new Timer("Bake for 15 minutes at 425", 100));
        testArray.add(new Timer("Reduce heat, Bake 50 minutes", 1200));



        ListView timerList = view.findViewById(R.id.timerList);
        Context context = getContext();
        timerList.setAdapter(new timerCustomAdapter(testArray, context));

        return view;
    }

    class Timer{
        String name;
        long time;
        Timer(String name, long time){
            this.name = name;
            this.time = time;
        }
    }

}
