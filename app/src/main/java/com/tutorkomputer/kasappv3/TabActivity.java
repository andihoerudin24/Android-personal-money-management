package com.tutorkomputer.kasappv3;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tutorkomputer.kasappv3.adapter.ViewPagerAdapter;
import com.tutorkomputer.kasappv3.fragment.KeluarFragment;
import com.tutorkomputer.kasappv3.fragment.MasukFragment;

public class TabActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        tabLayout=findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.view_pager);

        addTabs(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        getSupportActionBar().setTitle("Tambah");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void addTabs(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addfrag(new MasukFragment(),"Pemasukan");
        adapter.addfrag(new KeluarFragment(),"Pengeluaran");
        viewPager.setAdapter(adapter);
    }
}
