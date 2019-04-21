package com.tutorkomputer.kasappv3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splashscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

      Thread thread= new Thread(){
          public void run(){
              try{
                  sleep(2000);
              }catch (InterruptedException e){
                  e.printStackTrace();
              }finally {
                  startActivity(new Intent( Splashscreen.this,MainActivity.class ));
                  finish(); //menutup activity
              }
          }
      };
      thread.start();

    }
}
