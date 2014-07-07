package com.mb.holochartstest;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends Activity implements BarGraph.OnBarClickedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Bar> points = new ArrayList<Bar>();
        Bar d = new Bar();
        d.setColor(Color.parseColor("#99CC00"));
        d.setName("Zest");
        d.setValue(1);
        Bar d2 = new Bar();
        d2.setColor(Color.parseColor("#FFBB33"));
        d2.setName("Social Intelligence");
        d2.setValue(2);
        Bar d3 = new Bar();
        d3.setColor(Color.parseColor("#0055FF"));
        d3.setName("Self Control");
        d3.setValue(7);

        Bar d4 = new Bar();
        d4.setColor(Color.parseColor("#99CC00"));
        d4.setName("Optimism");
        d4.setValue(6);
        Bar d5 = new Bar();
        d5.setColor(Color.parseColor("#FFBB33"));
        d5.setName("Grit");
        d5.setValue(2);
        Bar d6 = new Bar();
        d6.setColor(Color.parseColor("#0055FF"));
        d6.setName("Gratitude");
        d6.setValue(3);
        Bar d7 = new Bar();
        d7.setColor(Color.parseColor("#0055FF"));
        d7.setName("Curiosity");
        d7.setValue(4);


        points.add(d);
        points.add(d2);
        points.add(d3);
        points.add(d4);
        points.add(d5);
        points.add(d6);
        points.add(d7);


//        MyBarGraph g = (MyBarGraph)findViewById(R.id.graph);
        BarGraph g = (BarGraph)findViewById(R.id.graph);
        g.setBars(points);
        g.setUnit("$");

        g.setOnBarClickedListener(this);
    }

    @Override
    public void onClick(int index) {
        Toast.makeText(this, "Index: " + index, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickDisplay(String barName) {
        Toast.makeText(this, "Clicked: " + barName, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
