package com.kennypvthax.uisuite;
import android.content.Context;
import android.content.res.Configuration;
import java.util.Locale;
final class LocaleSettings {
 static final String[] LANGUAGES={"en","zh","hi","ar"};
 static String normalize(String value){for(String lang:LANGUAGES)if(lang.equals(value))return lang;return "en";}
 static String current(Context c){try{return normalize(c.getSharedPreferences("kenny-ui-suite",0).getString("language","en"));}catch(ClassCastException e){return "en";}}
 static Context attach(Context base){Locale locale=Locale.forLanguageTag(current(base));Configuration cfg=new Configuration(base.getResources().getConfiguration());cfg.setLocale(locale);cfg.setLayoutDirection(locale);return base.createConfigurationContext(cfg);}
 static boolean save(Context c,String language){return c.getSharedPreferences("kenny-ui-suite",0).edit().putString("language",normalize(language)).commit();}
}
