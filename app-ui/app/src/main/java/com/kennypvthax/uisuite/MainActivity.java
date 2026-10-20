package com.kennypvthax.uisuite;
import android.app.Activity;
import android.os.Bundle;
public final class MainActivity extends Activity {
 private Gallery gallery;
 public void onCreate(Bundle state){super.onCreate(state);getWindow().setStatusBarColor(Tokens.BG);getWindow().setNavigationBarColor(Tokens.BG);gallery=new Gallery(this);setContentView(gallery);if(getIntent().hasExtra("preview_time"))gallery.freezePreview(getIntent().getFloatExtra("preview_time",0));}
 protected void onResume(){super.onResume();if(gallery!=null)gallery.resume();}
 protected void onPause(){if(gallery!=null)gallery.pause();super.onPause();}
 protected void onDestroy(){if(gallery!=null)gallery.close();super.onDestroy();}
}
