package com.example.david.videovalaszto.Utils;

import android.view.KeyEvent;

/**
 * Created by Dávid on 2017. 12. 02..
 */

public class Valtozok {
    public static final String VIDEOPATH = "/sdcard/download/video/"; //"android.resource://com.example.david.videodemo/";
    //public static final String VIDEOPATH = "http://borg.hu/!/";

    //public static final int VIDEO_NULLA = R.raw.nulla;
    public static final String VIDEO_NULLA = "nulla.mp4";

    public static final String VIDEO_EGY = "egy.mp4";
    //public static final int VIDEO_EGY = R.raw.egy;
    public static final int VIDEO_EGY_GOMB = KeyEvent.KEYCODE_1;

    public static final String VIDEO_KETTO = "ketto.mp4";
    //public static final int VIDEO_KETTO = R.raw.ketto;
    public static final int VIDEO_KETTO_GOMB = KeyEvent.KEYCODE_2;

    public static final String VIDEO_HAROM = "harom.mp4";
    //public static final int VIDEO_HAROM = R.raw.harom;
    public static final int VIDEO_HAROM_GOMB = KeyEvent.KEYCODE_3;

    public static final int ATTUNESI_IDO = 1000;  //áttűnési idő milisecond
}
