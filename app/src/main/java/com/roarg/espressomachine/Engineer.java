package com.roarg.espressomachine;


import java.util.Date;
import java.util.LinkedList;
import java.util.Timer;


/**
 * Created by RoarG on 04.06.2015.
 * Engineer Class for the EspressoMachine Simulator.
 */

public class Engineer {

    private final static String TAG = "ESPRESSO"; //For LOGCAT
    /**
     * Private Fields
     */
    private int busyMin;
    private int busyTo;

    private LinkedList workingEngi;

    /**
     * Public Fields
     */
    public Date gotCaffein;
   // public Date busyFrom;
    public boolean busy = false;


    /**
     * Constructors
     * @param busyMin
     * @param
     */
    public Engineer(int busyMin, LinkedList workingEngi) {
        this.busy = true;
        this.busyTo = (int) (((System.currentTimeMillis() / 1000) % 60) + busyMin);
        this.busyMin = busyMin;
        this.workingEngi = workingEngi;
    }


    public Engineer(LinkedList workingEngi) {
        this.busy = false;
        this.workingEngi = workingEngi;
    }


    public void gotEspresso() {
        gotCaffein = new Date();
        workingEngi.add(this);
    }

    /**
     *
     * @return true if busyTo time has past
     */
    public boolean stillBusy() {
        return busyTo < ((System.currentTimeMillis() / 1000) % 60);
    }





}
