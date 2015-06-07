package com.roarg.espressomachine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;


public class EspressoMachineActivity extends ActionBarActivity {

    public static final String TAG = "ESPRESSO";

    /**
     * Regulated the time simulation
     */
    private static final int COFFEIN_EVERY = 3600;  //In Seconds Coffein every hour
    private static final int MACHINE_CD = 30000; //In MS How long does it take to make a espresso 3 min to make espresso
    private static final int BUSYTIME_CONVERT = 1; //For simulating  set 60 to make hours minutes


    /**
     * Fields
     */
    private int engineers = 0;
    private int busyMin = 0;
    private int busyChance = 0;   //Uses Int for convenience


    private Timer workingTimer;
    private Timer runQueueTimer;
    private Timer updateTimer;


    /**
     * Queues
     */
    private LinkedList<Engineer> engiQueue = new LinkedList<>();
    private LinkedList<Engineer> busyQueue = new LinkedList<>();
    private LinkedList<Engineer> workingEngi = new LinkedList<>();

    /**
     * UI elements
     */
    public TextView espressoQueueNumberText;
    public TextView numberOfWorkingEngineerText;
    private ImageView imageView;


    public EspressoMachineActivity(int engineers, int busyMin, int busyChance) {
        this.engineers = engineers;
        this.busyMin = busyMin;
        this.busyChance = busyChance;
    }

    public EspressoMachineActivity() {

    }

    /**
     * "Makes" Engineers according to input given by user
     *
     * Gets private int engineers = 0;
     * private int busyEng = 0;
     * private int busyMin = 0;
     */
    private void makeEngineers(int engineers, int busyMin, int busyChance, LinkedList busyQueue, LinkedList engiQueue, LinkedList workingEngi) {

        busyMin = busyMin / BUSYTIME_CONVERT;
        //Do a random chance to se what kind of engineer to make
        for (int i = 0; i < engineers; i++) {
            double randomC = Math.random();
            int randomChance = (int) (randomC * 100);

            //Make busy engineer
            if (randomChance <= busyChance) {
                this.busyQueue.add(new Engineer(busyMin, workingEngi));
                Log.d(TAG, "busyQueue : " + this.busyQueue);
            //Make engineer
            } else if (randomChance > busyChance) {
                this.engiQueue.add(new Engineer(workingEngi));
                Log.d(TAG, "EngiQueue : " + this.engiQueue);
            } else {
                Log.d(TAG, "Av en eller annen grunn traff ikke random");
            }
        }

        /**
         * Start the queue and the simulation
         */
        runQueue();
        working();
    }

    private void working() {

        workingTimer = new  Timer();
        workingTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                if (workingEngi.size() != 0) {
                    Date time =  new Date();

                    if (((time.getTime() - workingEngi.getFirst().gotCaffein.getTime()) / 1000) % 60 >= COFFEIN_EVERY) {

                        //Add Engineer in the right queue according to busy state
                        if (workingEngi.getFirst().stillBusy()){
                            busyQueue.add(workingEngi.pop());
                        }
                        else{
                            engiQueue.add(workingEngi.pop());
                        }
                    }
                }
            }
        }, 1000, 500);
    }

    private void runQueue() {

        runQueueTimer = new Timer();
        runQueueTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //if engineer at BusyQueue pop one of busyQueue FIFO
                if (!busyQueue.isEmpty()) {
                    busyQueue.pop().gotEspresso(); //POP
                }
                //if engineer at queue pop one of queue FIFO
                else if (!engiQueue.isEmpty()) {
                    engiQueue.pop().gotEspresso(); //POP
                }
                else {
                    Log.d(TAG, "Both queues are empty");
                }
            }
        }, 500, MACHINE_CD);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_espresso_machine);

        //Getting the Intent from the MainActivity
        Intent intent = getIntent();
        Bundle message = intent.getBundleExtra(MainActivity.EXTRA_BUNDLE);

        //Assigning the variable from the MainActivity
        engineers = message.getInt("EXTRA_ENGINEERS");
        busyMin = message.getInt("EXTRA_BUSYMIN");
        busyChance = message.getInt("EXTRA_BUSYENG");

        //Sets the textView for the showing the numbers on screen
        numberOfWorkingEngineerText = (TextView) findViewById(R.id.workingEngineersNumber);
        espressoQueueNumberText = (TextView) findViewById(R.id.espressoQueueNumber);
        imageView = (ImageView) findViewById(R.id.imageView);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.espresso);
        imageView.setImageBitmap(bm);

        //instantiate the engineers and starts the simulation
        makeEngineers(engineers, busyMin, busyChance, engiQueue, busyQueue, workingEngi);
        updateTimer = new Timer();

    }

    protected void onStart() {
        super.onStart();
        updateTimer.scheduleAtFixedRate(new UpdateTask(new Handler(), this), 500, 1000);
    }

    protected void onStop() {
        super.onStart();
        updateTimer.cancel();
    }

    /**
     * Update the UI
     */
    public void update() {
        if (busyQueue !=null) {
            setEspressoQueueNumberText(String.valueOf(busyQueue.size() + engiQueue.size()));
            setNumberOfWorkingEngineerText(String.valueOf(workingEngi.size()));
        }
    }

    private class UpdateTask extends TimerTask {
        Handler handler;
        EspressoMachineActivity ref;

        public UpdateTask (Handler handler, EspressoMachineActivity ref) {
            super();
            this.handler = handler;
            this.ref = ref;

        }

        @Override
        public void run() {
            handler.post(new Runnable() {

                @Override
                public void run() {
                    ref.update();
                }
            });
        }
    }

    public void setEspressoQueueNumberText (String value) {
        espressoQueueNumberText.setText(value);
    }

    public void setNumberOfWorkingEngineerText (String value) {
        numberOfWorkingEngineerText.setText(value);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_espresso_machine, menu);
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

    /**
     * Cancel the timers when hiting the back button
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
            workingTimer.cancel();
            runQueueTimer.cancel();
            updateTimer.cancel();

        }
        return super.onKeyDown(keyCode, event);
    }
}
