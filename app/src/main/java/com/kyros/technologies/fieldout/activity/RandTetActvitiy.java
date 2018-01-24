package com.kyros.technologies.fieldout.activity;

import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.databinding.ActivityRandTetActvitiyBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandTetActvitiy extends AppCompatActivity {
    private  List<Integer> firstArrayList=new ArrayList<>();
    private List<Integer> secondArrayList=new ArrayList<>();
    private List<Integer> thirdArrayList=new ArrayList<>();
    private List<Integer> fourthArrayList=new ArrayList<>();
    private List<Integer> fifthArrayList=new ArrayList<>();
    private ActivityRandTetActvitiyBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_rand_tet_actvitiy);
        new Async().execute();
    binding.refreshButton.setOnClickListener(view ->   new Async().execute());
    }
    private class Async extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            ranMethod();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            bindViews();
            Log.d("Binding Views","");

        }


    }
    private void bindViews() {
        //Column One
        binding.one.setText(String.valueOf(firstArrayList.get(0)));
        binding.six.setText(String.valueOf(firstArrayList.get(1)));
        binding.eleven.setText(String.valueOf(firstArrayList.get(2)));
        binding.sixteen.setText(String.valueOf(firstArrayList.get(3)));
        binding.twentyOne.setText(String.valueOf(firstArrayList.get(4)));

        //Column two
        binding.two.setText(String.valueOf(secondArrayList.get(0)));
        binding.seven.setText(String.valueOf(secondArrayList.get(1)));
        binding.twelve.setText(String.valueOf(secondArrayList.get(2)));
        binding.seventeen.setText(String.valueOf(secondArrayList.get(3)));
        binding.twentyTwo.setText(String.valueOf(secondArrayList.get(4)));

        //Column three
        binding.three.setText(String.valueOf(thirdArrayList.get(0)));
        binding.eight.setText(String.valueOf(thirdArrayList.get(1)));
        binding.thirteen.setText(String.valueOf(thirdArrayList.get(2)));
        binding.eighteen.setText(String.valueOf(thirdArrayList.get(3)));
        binding.twentyThree.setText(String.valueOf(thirdArrayList.get(4)));

        //Column four
        binding.four.setText(String.valueOf(fourthArrayList.get(0)));
        binding.nine.setText(String.valueOf(fourthArrayList.get(1)));
        binding.fourteen.setText(String.valueOf(fourthArrayList.get(2)));
        binding.nineteen.setText(String.valueOf(fourthArrayList.get(3)));
        binding.twentyFour.setText(String.valueOf(fourthArrayList.get(4)));

        //Column five
        binding.five.setText(String.valueOf(fifthArrayList.get(0)));
        binding.ten.setText(String.valueOf(fifthArrayList.get(1)));
        binding.fifteen.setText(String.valueOf(fifthArrayList.get(2)));
        binding.twenty.setText(String.valueOf(fifthArrayList.get(3)));
        binding.twentyFive.setText(String.valueOf(fifthArrayList.get(4)));


    }
    private void clearList(){
        firstArrayList.clear();
        secondArrayList.clear();
        thirdArrayList.clear();
        fourthArrayList.clear();
        fifthArrayList.clear();
    }
    private  void ranMethod(){
            clearList();
        while(firstArrayList.size()<5){
            int ranGenerated=getRandomNumberFromGeneral(1,15);
            if(!firstArrayList.contains(ranGenerated)){
                firstArrayList.add(ranGenerated);
            }
        }
        while(secondArrayList.size()<5){
            int ranGenerated=getRandomNumberFromGeneral(16,30);
            if(!secondArrayList.contains(ranGenerated)){
                secondArrayList.add(ranGenerated);
            }
        }
        while(thirdArrayList.size()<5){
            int ranGenerated=getRandomNumberFromGeneral(31,45);
            if(!thirdArrayList.contains(ranGenerated)){
                thirdArrayList.add(ranGenerated);
            }
        }
        while(fourthArrayList.size()<5){
            int ranGenerated=getRandomNumberFromGeneral(46,60);
            if(!fourthArrayList.contains(ranGenerated)){
                fourthArrayList.add(ranGenerated);
            }
        }
        while(fifthArrayList.size()<5){
            int ranGenerated=getRandomNumberFromGeneral(61,75);
            if(!fifthArrayList.contains(ranGenerated)){
                fifthArrayList.add(ranGenerated);
            }
        }
        for(int i=0;i<5;i++){
            System.out.println(" | "+firstArrayList.get(i)+" | "+secondArrayList.get(i)+" | "+thirdArrayList.get(i)+" | "+fourthArrayList.get(i)+" | "+fifthArrayList.get(i));
        }

    }

    private int getRandomNumberFromGeneral(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

}
