package com.kennypvthax.uisuite;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
public final class MainActivity extends Activity {
 private Gallery gallery;private AppShell app;
 protected void attachBaseContext(Context base){super.attachBaseContext(LocaleSettings.attach(base));}
 public void onCreate(Bundle state){
  super.onCreate(state);getWindow().setStatusBarColor(Tokens.BG);getWindow().setNavigationBarColor(Tokens.BG);
  boolean debug=(getApplicationInfo().flags&ApplicationInfo.FLAG_DEBUGGABLE)!=0;
  if(debug&&getIntent().getBooleanExtra("gallery",false)){
   gallery=new Gallery(this);setContentView(gallery);
   if(getIntent().hasExtra("preview_time"))gallery.freezePreview(getIntent().getFloatExtra("preview_time",0));
  }else{
   app=new AppShell(this,debug);setContentView(app);
   if(debug&&state!=null&&state.getBoolean("development_dashboard",false))app.restorePreview();
   if(debug&&getIntent().hasExtra("preview_time"))app.freezePreview(getIntent().getFloatExtra("preview_time",0));
  }
 }
 protected void onSaveInstanceState(Bundle state){if(app!=null)state.putBoolean("development_dashboard",app.previewActive());super.onSaveInstanceState(state);}
 public void onBackPressed(){if(app==null||!app.back())super.onBackPressed();}
 protected void onResume(){super.onResume();if(gallery!=null)gallery.resume();if(app!=null)app.resume();}
 protected void onPause(){if(gallery!=null)gallery.pause();if(app!=null)app.pause();super.onPause();}
 protected void onDestroy(){if(gallery!=null)gallery.close();if(app!=null)app.close();super.onDestroy();}
}
