package com.example.kluggame2;

import android.content.Context;
import android.media.MediaPlayer;


public class SoundHandler {
    private static MediaPlayer backgroundMusic;
    private static Context context;

    private static boolean isMuted = false;

    public static void setContext(Context cont){
        context = cont;
    }

    public static void playMusic(int resource){
        

        
        try{
            backgroundMusic = MediaPlayer.create(context, resource);

            backgroundMusic.setLooping(true);
            backgroundMusic.setVolume(100, 100);

            if(!isMuted){
                backgroundMusic.start();
            }
        } catch (NullPointerException e){
            //Creating MediaPlayer failed. This happens randomly without any clear reasons.
            e.printStackTrace();
        }
        
    }

    public static void setMuted(boolean muted){
        if(backgroundMusic != null){
            if(muted){
                if(backgroundMusic.isPlaying()){
                    backgroundMusic.stop();
                    isMuted = true;
                }
            } else {
                if(!backgroundMusic.isPlaying()){
                    backgroundMusic.start();
                    isMuted = false;
                }
            }
        }
    }

    public static void pause(){
    	if(backgroundMusic != null){
            backgroundMusic.pause();
        }
        }
    
    
    
    public static void quit(){
        if(backgroundMusic != null){
            backgroundMusic.release();
        }
    }
}