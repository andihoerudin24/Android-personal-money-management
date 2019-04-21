package com.tutorkomputer.kasappv3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.leavjenn.smoothdaterangepicker.date.SmoothDateRangePickerFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ListView list_kas;
    SwipeRefreshLayout swipe_refresh;
    ArrayList<HashMap<String, String>> aruskas;
    Cursor cursor;
    Integer angka;

    TextView text_masuk,text_keluar,text_total;

   public static String URL, transaksi_id,jumlah,keterangan,tanggal,tanggal2,status,
           tanggal_dari,tanggal_ke;


   public static boolean filter;
   public static TextView text_filter;
   String query_kas,query_total;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        startActivity(new Intent(MainActivity.this,IntroActivity.class));

        transaksi_id="";
        jumlah="";
        keterangan="";
        tanggal="";
        tanggal2="";
        status="";
        URL="https://demo.lazday.com/uangkas/list.php";

        text_masuk        =findViewById(R.id.text_masuk);
        text_keluar       =findViewById(R.id.text_keluar);
        text_total        =findViewById(R.id.text_total);
        list_kas          =findViewById(R.id.list_kas);
        text_filter       =findViewById(R.id.text_filter);
        swipe_refresh     =findViewById(R.id.swipe_refresh);
        aruskas= new ArrayList<>();



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(MainActivity.this,AddActivity.class));
                startActivity(new Intent(MainActivity.this,TabActivity.class));
//        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

            }
        });




        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               URL="https://demo.lazday.com/uangkas/list.php";
               filter=false;
               selectmysql();
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
        query_kas="";
        query_total="";
        if (filter){
        URL="https://demo.lazday.com/uangkas/filter.php?dari=" + tanggal_dari + "&ke=" + tanggal_ke;
        filter=false;
        }
      selectmysql();

    }

    private void Adapter(){
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,aruskas,R.layout.list_kas,
                new String[]{"transaksi_id","status","jumlah","keterangan","tanggal","tanggal2"},
                new int[] {R.id.text_transaksi_id,R.id.text_status,R.id.text_jumlah,
                           R.id.text_keterangan,R.id.text_tanggal,R.id.text_tanggal2});
        list_kas.setAdapter(simpleAdapter);
        list_kas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                transaksi_id  = ((TextView)view.findViewById(R.id.text_transaksi_id)).getText().toString();
                status        = ((TextView)view.findViewById(R.id.text_status)).getText().toString();
                jumlah        = ((TextView)view.findViewById(R.id.text_jumlah)).getText().toString();
                keterangan    = ((TextView)view.findViewById(R.id.text_keterangan)).getText().toString();
                tanggal       = ((TextView)view.findViewById(R.id.text_tanggal)).getText().toString();
                tanggal2      = ((TextView)view.findViewById(R.id.text_tanggal2)).getText().toString();


                ListMenu();
            }
        });
        swipe_refresh.setRefreshing(false);
    }


    private void kasAdapter(){
        aruskas.clear();
        swipe_refresh.setRefreshing(false);
        list_kas.setAdapter(null);
        angka=11;
        for (int i=0; i < angka; i++){
             HashMap<String, String> map = new HashMap<>();
             map.put("transaksi_id",angka.toString(0));
             map.put("status",angka.toString(1));
             map.put("jumlah",angka.toString(2));
             map.put("keterangan",angka.toString(3));
             map.put("tanggal",angka.toString(4));
             aruskas.add(map);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this,aruskas,R.layout.list_kas,
                new String[]{"transaksi_id","status","jumlah","keterangan","tanggal"},
                new int[] {R.id.text_transaksi_id,R.id.text_status,R.id.text_jumlah,R.id.text_keterangan,R.id.text_tanggal});
        list_kas.setAdapter(simpleAdapter);
        list_kas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   transaksi_id= ((TextView)view.findViewById(R.id.text_transaksi_id)).getText().toString();
                   Log.e("_transaksi_id",transaksi_id);
                   ListMenu();
            }
        });
        //KasTotal();
        selectmysql();
    }

    private void KasTotal(){
//       NumberFormat rupiah = NumberFormat.getCurrencyInstance(Locale.GERMANY);
       text_masuk.setText("90000");
       text_keluar.setText("10000");
       text_total.setText("2000000");

       swipe_refresh.setRefreshing(false);
       if (!filter){
           text_filter.setVisibility(View.GONE);
       }
       filter=false;
    }

    private void ListMenu(){
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.list_menu);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);

        RippleView rip_hapus =dialog.findViewById(R.id.rip_hapus);
        RippleView rip_edit  =dialog.findViewById(R.id.rip_edit);

        rip_hapus.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                dialog.dismiss();
                hapus();
            }
        });

        rip_edit.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                dialog.dismiss();
                startActivity(new Intent(MainActivity.this,EditActivity.class));
            }
        });

        dialog.show();
    }

    private void hapus(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfrimasi");
        builder.setMessage("Yakin Ingin Di hapus ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                 dialog.dismiss();
                AndroidNetworking.get("https://demo.lazday.com/uangkas/delete.php")
                        .addQueryParameter("transaksi_id",transaksi_id)
                        .setPriority(Priority.LOW)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                if (response.optString("response").equals("success")){
                                    Toast.makeText(getApplicationContext(),"Transaksi Berhasil Di hapus" ,
                                            Toast.LENGTH_LONG).show();
                                    selectmysql();
                                }else{
                                    Toast.makeText(getApplicationContext(),"Data Gagal Di hapus" ,
                                            Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {

                            }
                        });


                 Toast.makeText(getApplicationContext(),"Berhasil Di Hapus",Toast.LENGTH_LONG).show();

                 //kasAdapter();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void selectmysql(){
        aruskas.clear();
        AndroidNetworking.get(URL)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                      NumberFormat rupiahformat = NumberFormat.getInstance(Locale.GERMANY);

                      text_masuk.setText(rupiahformat.format(response.optDouble("masuk")));
                      text_keluar.setText(rupiahformat.format(response.optDouble("keluar")));
                      text_total.setText(rupiahformat.format(response.optDouble("saldo")));
                        try {
                            JSONArray jsonArray=response.getJSONArray("result");
                            for (int i=0; i< jsonArray.length(); i++){
                                 JSONObject jsonObject= jsonArray.getJSONObject(i);
 //                                <HashMap><String,String> map =new HashMap<>();
                                 HashMap<String, String> map = new HashMap<>();
                                 map.put("transaksi_id",jsonObject.optString("transaksi_id"));
                                 map.put("status",jsonObject.optString("status"));
                                 map.put("jumlah",jsonObject.optString("jumlah"));
                                 map.put("keterangan",jsonObject.optString("keterangan"));
                                 map.put("tanggal",jsonObject.optString("tanggal"));
                                 map.put("tanggal2",jsonObject.optString("tanggal2"));
                                 aruskas.add(map);

                            }

                            Adapter();

                        } catch (JSONException e) {

                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filter) {
            //startActivity(new Intent(MainActivity.this,FilterActivity.class));
            filtermysql();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void filtermysql(){
        SmoothDateRangePickerFragment smoothDateRangePickerFragment = SmoothDateRangePickerFragment.newInstance(
                new SmoothDateRangePickerFragment.OnDateRangeSetListener() {
                    @Override
                    public void onDateRangeSet(SmoothDateRangePickerFragment view,
                                               int yearStart, int monthStart,
                                               int dayStart, int yearEnd,
                                               int monthEnd, int dayEnd) {
                        // grab the date range, do what you want

                        tanggal_dari =yearStart + "-" + (monthStart+1) + "-" + dayStart;
                        tanggal_ke   =yearEnd   + "-" + (monthEnd+1)   + "-" + dayEnd;


                        text_filter.setVisibility(View.VISIBLE);
                        text_filter.setText(
                                dayStart + "/" + (monthStart+1) +"/" +yearStart + " - " +
                                     dayEnd   + "/" + (monthEnd+1)  +"/"  + yearEnd
                        );
                        URL="https://demo.lazday.com/uangkas/filter.php?dari=" + tanggal_dari + "&ke=" + tanggal_ke;
                        filter=false;

                        Log.e("_logUrl",URL);

                        selectmysql();
                    }
                });

        smoothDateRangePickerFragment.show(getFragmentManager(), "smoothDateRangePicker");
    }
}
