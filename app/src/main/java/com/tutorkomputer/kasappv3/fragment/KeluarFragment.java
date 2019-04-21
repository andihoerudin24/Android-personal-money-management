package com.tutorkomputer.kasappv3.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.tutorkomputer.kasappv3.R;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class KeluarFragment extends Fragment {


    EditText edit_jumlah,edit_keterangan;
    RippleView rip_simpan;

    public KeluarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_keluar, container, false);

        edit_jumlah =v.findViewById(R.id.edit_jumlah);
        edit_keterangan =v.findViewById(R.id.edit_keterangan);
        rip_simpan =v.findViewById(R.id.rip_simpan);

        rip_simpan.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                insertMysql();
            }
        });

        return v;
    }

    private void insertMysql(){
        AndroidNetworking.get("https://demo.lazday.com/uangkas/add.php")
                .addQueryParameter("status","KELUAR")
                .addQueryParameter("jumlah",edit_jumlah.getText().toString())
                .addQueryParameter("keterangan",edit_keterangan.getText().toString())
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.optString("response").equals("success")){
                            Toast.makeText(getContext(),"Transaksi Berhasil Di Simpan" ,
                                    Toast.LENGTH_LONG).show();
                            getActivity().finish();
                        }else{
                            Toast.makeText(getContext(),"Data Gagal Di Masukan" ,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

}
