package com.laresx.tally;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.laresx.tally.adapter.RecordPagerAdapter;
import com.laresx.tally.frag_record.IncomFragment;
import com.laresx.tally.frag_record.BaseRecordFragment;
import com.laresx.tally.frag_record.OutcomeFragment;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        tabLayout = findViewById(R.id.record_tabs);
        viewPager = findViewById(R.id.record_vp);

        //set the page of viewpager
        initPager();
    }

    private void initPager() {
        //initiate pages of the viewpager
        List<Fragment> fragmentList = new ArrayList<>();

        //create income and spending pages and put into the fragment
        OutcomeFragment outFrag = new OutcomeFragment();
        IncomFragment inFrag = new IncomFragment();
        fragmentList.add(outFrag);
        fragmentList.add(inFrag);

        //create and set adapter
        RecordPagerAdapter pagerAdapter = new RecordPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(pagerAdapter);

        //connect tabLayout and viewPager
        tabLayout.setupWithViewPager(viewPager);

    }

    //click event
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.record_iv_back:
                finish();
                break;
        }
    }
}