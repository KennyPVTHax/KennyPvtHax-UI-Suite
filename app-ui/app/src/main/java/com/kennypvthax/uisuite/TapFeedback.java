package com.kennypvthax.uisuite;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.SystemClock;
/** One cached sample, one stream; no voice assets or native menu audio engine. */
final class TapFeedback implements AutoCloseable {
 private final SoundPool pool; private final int sample; private volatile boolean ready; private long last=-1000;
 boolean enabled=true;
 TapFeedback(Context c){pool=new SoundPool.Builder().setMaxStreams(1).setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()).build();pool.setOnLoadCompleteListener((p,id,status)->ready=status==0);sample=pool.load(c,R.raw.tap,1);}
 void play(){long now=SystemClock.uptimeMillis();if(enabled&&ready&&now-last>=65){last=now;pool.play(sample,.18f,.18f,1,0,1);}}
 public void close(){ready=false;pool.release();}
}
