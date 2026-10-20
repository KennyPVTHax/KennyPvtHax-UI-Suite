package com.kennypvthax.uisuite;

/** Exact four observed editions; no native integration, offsets or game modification. */
final class GameCatalog {
 static final class Edition {
  final String name,packageName,badge;
  Edition(String name,String packageName,String badge){this.name=name;this.packageName=packageName;this.badge=badge;}
 }
 private static final Edition[] EDITIONS={
  new Edition("PUBG MOBILE GLOBAL","com.tencent.ig","GL"),
  new Edition("PUBG MOBILE KOREA","com.pubg.krmobile","KR"),
  new Edition("PUBG MOBILE TAIWAN","com.rekoo.pubgm","TW"),
  new Edition("PUBG MOBILE VIETNAM","com.vng.pubgmobile","VN")
 };
 static Edition[] editions(){return EDITIONS.clone();}
 static boolean permits(String packageName){for(Edition e:EDITIONS)if(e.packageName.equals(packageName))return true;return false;}
 private GameCatalog(){}
}
