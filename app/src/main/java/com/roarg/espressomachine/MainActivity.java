package com.roarg.espressomachine;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;


public class MainActivity extends ActionBarActivity {

    /**
     * Constants
     */

    public final String TAG = "ESPRESSO";   //For LOGCAT
    private final int MAXENGINEERS = 500;
    private final int MINENGINEERS = 1;
    private final int MAXBUSYTIME = 480;    //In Minutes. a workday(8hours) of busy is max
    private final int MINBUSYTIME = 60;     //In Minutes. 1 hour is min
    private final int MAXPERCENTBUSY = 100;
    private final int MINPERCENTBUSY = 0;

    /**
     * UI elements
     */
    NumberPicker numberPickerNrEngineers = null;
    NumberPicker numberPickerBusyTime = null;
    NumberPicker numberPickerPercentBusy = null;
    Button startButton = null;


    /**
     * Numbers used for the simulator
     */

    private int engineers = 0;
    private int busyEng = 0;
    private int busyMin = 0;

    /**
     * Bundle
     */
    public final static String EXTRA_BUNDLE = "com.roarg.espressoMachine.EXTRABUNDLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //NumberPicker: Number of Engineers
        numberPickerNrEngineers = (NumberPicker) findViewById(R.id.numberPicker_NrEngineers);
        numberPickerNrEngineers.setMaxValue(MAXENGINEERS);
        numberPickerNrEngineers.setMinValue(MINENGINEERS);
        numberPickerNrEngineers.setWrapSelectorWheel(false);


        //NumberPicker: Length of Busy time
        numberPickerBusyTime = (NumberPicker) findViewById(R.id.numberPickerBusyTime);
        numberPickerBusyTime.setMaxValue(MAXBUSYTIME);
        numberPickerBusyTime.setMinValue(MINBUSYTIME);
        numberPickerBusyTime.setWrapSelectorWheel(false);

        //NumberPicker: Percent of Busy Engineers
        numberPickerPercentBusy = (NumberPicker) findViewById(R.id.numberPickerPercentBusy);
        numberPickerPercentBusy.setMaxValue(MAXPERCENTBUSY);
        numberPickerPercentBusy.setMinValue(MINPERCENTBUSY);
        numberPickerPercentBusy.setWrapSelectorWheel(false);

        //Button: StartButton
        startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick((Button) v);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void onButtonClick(Button v) {
        engineers = numberPickerNrEngineers.getValue();
        busyMin = numberPickerBusyTime.getValue();
        busyEng = numberPickerPercentBusy.getValue();

        Log.d(TAG, "Engineers: " + engineers);
        Log.d(TAG, "BusyTime: " + busyMin);
        Log.d(TAG, "Busy Percent: " + busyEng);

        Intent intent = new Intent(this, EspressoMachineActivity.class);
        Bundle extras = new Bundle();
        extras.putInt("EXTRA_ENGINEERS", engineers);
        extras.putInt("EXTRA_BUSYMIN", busyMin);
        extras.putInt("EXTRA_BUSYENG", busyEng);
        intent.putExtra(EXTRA_BUNDLE, extras);
        startActivity(intent);

    }

}

