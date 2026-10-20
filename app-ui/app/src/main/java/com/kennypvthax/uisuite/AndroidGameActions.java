package com.kennypvthax.uisuite;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;

/** Public Android package visibility/launcher API only. No storage permissions or game file access. */
final class AndroidGameActions implements GameActions {
 private final Activity activity;
 AndroidGameActions(Activity activity){this.activity=activity;}
 public Status inspect(String packageName){
  if(!GameCatalog.permits(packageName))return new Status(Availability.UNKNOWN,false);
  try{
   PackageManager manager=activity.getPackageManager();manager.getApplicationInfo(packageName,0);
   return new Status(Availability.INSTALLED,manager.getLaunchIntentForPackage(packageName)!=null);
  }catch(PackageManager.NameNotFoundException e){return new Status(Availability.NOT_INSTALLED,false);}
  catch(SecurityException e){return new Status(Availability.UNKNOWN,false);}
 }
 public boolean launch(String packageName){
  if(!GameCatalog.permits(packageName))return false;
  try{
   Intent intent=activity.getPackageManager().getLaunchIntentForPackage(packageName);
   if(intent==null)return false;
   activity.startActivity(intent);return true;
  }catch(ActivityNotFoundException|SecurityException e){return false;}
 }
 public boolean fileActionsAvailable(){return false;}
}
