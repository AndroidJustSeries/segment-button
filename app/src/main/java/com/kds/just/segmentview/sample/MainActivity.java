package com.kds.just.segmentview.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.kds.just.segmentview.SegmentedView;

public class MainActivity extends AppCompatActivity implements SegmentedView.OnSelectedItemListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SegmentedView segment = findViewById(R.id.segment);
        segment.addItem("item 0");
        segment.addItem("item 1");
        segment.addItem("item 2");
        segment.addItem("item 3");

        SegmentedView segment01 = findViewById(R.id.segment01);
        segment01.addItem("item 0");
        segment01.addItem("item 1");

        SegmentedView segment02 = findViewById(R.id.segment02);
        segment02.addItem("item 0");

        segment.setOnSelectedItemListener(this);
        segment01.setOnSelectedItemListener(this);
        segment02.setOnSelectedItemListener(this);

    }

    @Override
    public void onSelected(SegmentedView v, View selectedView, int index) {
        Toast.makeText(this,"Select Item [" + ((TextView)selectedView).getText() + "] index : " + index,Toast.LENGTH_SHORT).show();
    }
}
