package com.tutorkomputer.kasappv3;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.tutorkomputer.kasappv3.helper.CurrentDate;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class EditActivity extends AppCompatActivity {

    RadioGroup radio_status;
    RadioButton radio_masuk,radio_keluar;
    EditText edit_jumlah,edit_keterangan,edit_tanggal;
    Button btn_simpan;

    String status,tanggal;

    RippleView rip_simpan;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        status= "";


        radio_status    =findViewById(R.id.radio_status);
        radio_masuk     =findViewById(R.id.radio_masuk);
        radio_keluar    =findViewById(R.id.radio_keluar);
        edit_jumlah     =findViewById(R.id.edit_jumlah);
        edit_keterangan =findViewById(R.id.edit_keterangan);
        edit_tanggal    =findViewById(R.id.edit_tanggal);
        btn_simpan      =findViewById(R.id.btn_simpan);
        rip_simpan      =findViewById(R.id.rip_simpan);


        status=MainActivity.status;
        switch (status){
            case "MASUK" : radio_masuk.setChecked(true);
                break;
            case "KELUAR" : radio_keluar.setChecked(true);
                break;
        }


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

        edit_jumlah.setText(MainActivity.jumlah);
        edit_keterangan.setText(MainActivity.keterangan);


        tanggal=MainActivity.tanggal;
        edit_tanggal.setText(MainActivity.tanggal2);

        edit_tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               datePickerDialog = new DatePickerDialog(EditActivity.this, new DatePickerDialog.OnDateSetListener() {
                   @Override
                   public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                       NumberFormat numberFormat = new DecimalFormat("00");
                        tanggal =year + "-" + numberFormat.format(month + 1) + "-" + numberFormat.format(dayOfMonth);
                       Log.e("_tanggal",tanggal);

                       edit_tanggal.setText(numberFormat.format(dayOfMonth) + "/" + numberFormat.format((month+1)) + "/" + year );
                   }
               }, CurrentDate.year,CurrentDate.montH,CurrentDate.day);

               datePickerDialog.show();
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
                    simpanEdit();
                }
            }
        });


        getSupportActionBar().setTitle("Edit Data");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


    private void simpanEdit(){
//        Toast.makeText(getApplicationContext(),"Jumlah :" + edit_jumlah.getText().toString() +" status :" + status
//                        + " Keterangan :" + edit_keterangan.getText().toString(),
//                Toast.LENGTH_LONG).show();
        AndroidNetworking.get("https://demo.lazday.com/uangkas/update.php")
                .addQueryParameter("transaksi_id",MainActivity.transaksi_id)
                .addQueryParameter("status",status)
                .addQueryParameter("jumlah",edit_jumlah.getText().toString())
                .addQueryParameter("keterangan",edit_keterangan.getText().toString())
                .addQueryParameter("tanggal",tanggal)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.optString("response").equals("success")){
                            Toast.makeText(getApplicationContext(),"Transaksi Berhasil Di Update" ,
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
}
