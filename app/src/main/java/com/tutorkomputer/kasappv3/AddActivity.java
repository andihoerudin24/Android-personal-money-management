package com.tutorkomputer.kasappv3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

public class AddActivity extends AppCompatActivity {

    RadioGroup radio_status;
    RadioButton radio_masuk,radio_keluar;
    EditText edit_jumlah,edit_keterangan;
    Button btn_simpan;
    String status;
    RippleView rip_simpan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        status= "";

        radio_status    =findViewById(R.id.radio_status);
        radio_masuk     =findViewById(R.id.radio_masuk);
        radio_keluar    =findViewById(R.id.radio_keluar);
        edit_jumlah     =findViewById(R.id.edit_jumlah);
        edit_keterangan =findViewById(R.id.edit_keterangan);
        btn_simpan      =findViewById(R.id.btn_simpan);
        rip_simpan      =findViewById(R.id.rip_simpan);

        radio_status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radio_masuk: status = "MASUK";
                    break;
                    case R.id.radio_keluar: status ="KELUAR";
                    break;
                }
            }
        });

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"Jumlah :" + edit_jumlah.getText().toString() +" status :" + status,
//                Toast.LENGTH_LONG).show();
            }
        });

        rip_simpan.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
               if (status.equals("")){
                   Toast.makeText(getApplicationContext(),"Status Harus Dilengkapi" ,
                           Toast.LENGTH_LONG).show();
                   radio_status.requestFocus();
               }else if (edit_jumlah.getText().toString().equals("")){
                   Toast.makeText(getApplicationContext(),"Jumlah Harus Di Isi" ,
                           Toast.LENGTH_LONG).show();
                   edit_jumlah.requestFocus();
               }else if (edit_keterangan.getText().toString().equals("")){
                   Toast.makeText(getApplicationContext(),"Keterangn Harus Di Isi" ,
                           Toast.LENGTH_LONG).show();
                   edit_keterangan.requestFocus();
               }else{
                  insertMysql();
               }


            }
        });


        getSupportActionBar().setTitle("Tambah Data");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void insertMysql(){
        AndroidNetworking.get("https://demo.lazday.com/uangkas/add.php")
                .addQueryParameter("status",status)
                .addQueryParameter("jumlah",edit_jumlah.getText().toString())
                .addQueryParameter("keterangan",edit_keterangan.getText().toString())
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.optString("response").equals("success")){
                            Toast.makeText(getApplicationContext(),"Transaksi Berhasil Di Simpan" ,
                                    Toast.LENGTH_LONG).show();
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(),"Data Gagal Di Masukan" ,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void simpanData(){
//        Toast.makeText(getApplicationContext(),"Jumlah :" + edit_jumlah.getText().toString() +" status :" + status
//                        + " Keterangan :" + edit_keterangan.getText().toString(),
//                Toast.LENGTH_LONG).show();
        finish();
    }
}
