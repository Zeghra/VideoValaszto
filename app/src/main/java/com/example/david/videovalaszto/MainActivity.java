package com.example.david.videovalaszto;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.VideoView;

import com.example.david.videovalaszto.Utils.Valtozok;

public class MainActivity extends AppCompatActivity {

    private VideoView videoView;
    private int videoId = 0;
    private Uri uri;
    private boolean loop = true;
    private ImageView imageView; //áttűnéshez
    private Bitmap bitmap; // screenshot
    //private VideoView videoView2;
    //private RelativeLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // teljes képernyőre vált
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION); // navigációs gombok elrejtése
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide(); //actionbar-t leveszi

        //View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        videoView = (VideoView) findViewById(R.id.videoView);  // összekötöm a layouttal a videoView-t
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setVisibility(View.GONE);

        videoLejatszas(videoView);

    }

    public void getVideoFrame(Uri uri) {  // függvény a screenshothoz

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {

            retriever.setDataSource(String.valueOf(uri));
            bitmap = null;
            bitmap = retriever.getFrameAtTime(); //ez rakja be a képet a bitmapbe
            imageView.setImageBitmap(bitmap); //beállitja a bitmap képet az imageViewba
            //Log.e("VIDEO", "retrieve.getFrame " + retriever.getFrameAtTime().toString());

        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
            }
        }

    }


    public void fadeOut() {
        //áttűnési effekt

        Animation fadeOut = new AlphaAnimation(1, 0); // teliből átlátszóba

        fadeOut.setInterpolator(new AccelerateInterpolator()); //animációs effekt
        fadeOut.setDuration(Valtozok.ATTUNESI_IDO); // az animálás időtartama
        imageView.setAnimation(fadeOut);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                videoView.setVisibility(View.INVISIBLE);
                //videoView.setAlpha(0);
                videoLejatszas(videoView);
                imageView.setVisibility(View.VISIBLE);
                Log.e("DEJO", "video getAlpha setAlpha előtt: " + videoView.getAlpha());
                /*videoView.setAlpha(0);
                Log.e("DEJO","video getAlpha setAlpha után, animate előtt"+videoView.getAlpha());

                videoView.animate()
                        .alpha(1)
                        .setDuration(Valtozok.ATTUNESI_IDO)
                        .setInterpolator(new AccelerateInterpolator());*/
                Log.e("DEJO", "video getAlpha animate után: " + videoView.getAlpha());
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.setVisibility(View.INVISIBLE);  // animáció végén elrejti a feketeView-t
                videoView.setVisibility(View.VISIBLE);
                //videoView.setAlpha(1);
                bitmap = null;
                //videoLejatszas(videoView);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                bitmap = null;
                imageView.setVisibility(View.INVISIBLE);
            }
        });
        //imageView.setAnimation(fadeOut);


    }


    public void videoLejatszas(final View view) {

        // beállitom, hogy loopolja vagy sem a videoView-t
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(loop);
            }
        });

        if (videoId == 0) { // ha a nulla videot játsza
            // 0. video elérési útja
            uri = Uri.parse(Valtozok.VIDEOPATH + Valtozok.VIDEO_NULLA);
            videoView.setVideoURI(uri); // elérési utat beállit
        }
        videoView.start(); // inditja a videot

        // a video lejátszásának befejeztével
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {  //0. video újrainditása
                videoId = 0;     // 0. video beállit
                loop = true;     // loopolja
                uri = Uri.parse(Valtozok.VIDEOPATH + Valtozok.VIDEO_NULLA);  // 0. video elérési útja
                videoView.setVideoURI(uri); // elérési utat beállit

                videoLejatszas(videoView); // video inditása
                mp.release();   //
                //Log.e("VIDEO", "mp.release meg volt");
            }
        });
    }

    // gomb felengedése
    @Override
    public boolean onKeyUp(final int keyCode, final KeyEvent event) {
        switch (keyCode) {
            case Valtozok.VIDEO_EGY_GOMB:
                if (videoId != 1) { // ha már játsza az 1-es videot, akkor nem engedi újrainditani
                    bitmap = null;
                    getVideoFrame(uri); // screenshot a videoról

                    loop = false;   // ne loopolja az 1-es videot
                    videoId = 1;    // ez az 1-es video
                    uri = Uri.parse(Valtozok.VIDEOPATH + Valtozok.VIDEO_EGY);  // elérési út
                    videoView.setVideoURI(uri);  // elérési út beállitása
                    fadeOut();  // inditja az áttűnést és a videot
                    // video inditása, ez csak akkor kell ha nincs fadeOut(), mert akkor ott indul a video
                    //videoLejatszas(videoView);
                }
                break;

            case Valtozok.VIDEO_KETTO_GOMB:
                if (videoId != 2) {
                    bitmap = null;
                    getVideoFrame(uri);
                    loop = false;
                    videoId = 2;
                    uri = Uri.parse(Valtozok.VIDEOPATH + Valtozok.VIDEO_KETTO);
                    videoView.setVideoURI(uri);
                    fadeOut();
                    //videoLejatszas(videoView);
                }
                break;

            case Valtozok.VIDEO_HAROM_GOMB:
                if (videoId != 3) {
                    bitmap = null;
                    getVideoFrame(uri); // screenshot a videoról
                    loop = false;
                    videoId = 3;
                    uri = Uri.parse(Valtozok.VIDEOPATH + Valtozok.VIDEO_HAROM);
                    videoView.setVideoURI(uri);
                    fadeOut();
                    //videoLejatszas(videoView);
                }
                break;

            default:
                return super.onKeyUp(keyCode, event);
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}
