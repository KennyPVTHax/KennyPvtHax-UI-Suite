package com.kennypvthax.uisuite;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

/** Reusable glass card: typography and button paint come from the existing component system. */
final class GameCard extends LinearLayout {
 private final TextView availability;
 private final Button launch;
 GameCard(Context context,GameCatalog.Edition edition,int id,TapFeedback sound,Runnable action){
  super(context);setId(id);setOrientation(VERTICAL);int padding=dp(16);setPadding(padding,padding,padding,padding);
  setBackground(KennyComponents.surface(context,0xc41a1014,0x52665059,12));
  LinearLayout heading=new LinearLayout(context);heading.setGravity(Gravity.CENTER_VERTICAL);addView(heading,new LayoutParams(-1,-2));
  TextView badge=new TextView(context);badge.setText(edition.badge);badge.setTextDirection(View.TEXT_DIRECTION_LTR);badge.setTextColor(Tokens.SELECTED);badge.setTextSize(18);badge.setGravity(Gravity.CENTER);badge.setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_NO);badge.setBackground(KennyComponents.surface(context,Tokens.SUBTLE,Tokens.OUTLINE,12));heading.addView(badge,new LayoutParams(dp(50),dp(50)));
  LinearLayout words=new LinearLayout(context);words.setOrientation(VERTICAL);LayoutParams titleParams=new LayoutParams(0,-2,1);titleParams.setMarginStart(dp(12));heading.addView(words,titleParams);
  TextView name=label(edition.name,16,Tokens.TEXT);name.setTextDirection(View.TEXT_DIRECTION_LTR);words.addView(name,new LayoutParams(-1,-2));
  availability=label(context.getString(R.string.checking_edition),12,Tokens.SECONDARY);words.addView(availability,new LayoutParams(-1,-2));
  TextView packageName=label(edition.packageName,13,Tokens.SECONDARY);packageName.setTextDirection(View.TEXT_DIRECTION_LTR);LayoutParams packageParams=new LayoutParams(-1,-2);packageParams.topMargin=dp(16);addView(packageName,packageParams);
  LinearLayout buttons=new LinearLayout(context);
  Button files=KennyComponents.button(context,context.getString(R.string.files),()->{});files.setId(fileId(id));files.setEnabled(false);files.setContentDescription(context.getString(R.string.files_unavailable));
  LayoutParams half=new LayoutParams(0,dp(50),1);half.setMarginEnd(dp(8));buttons.addView(files,half);
  launch=KennyComponents.button(context,context.getString(R.string.open_game),()->{sound.play();action.run();});launch.setId(launchId(id));launch.setTag("illuminated");launch.setEnabled(false);buttons.addView(launch,new LayoutParams(0,dp(50),1));
  LayoutParams actions=new LayoutParams(-1,-2);actions.topMargin=dp(14);addView(buttons,actions);
 }
 private TextView label(String value,int size,int color){TextView v=new TextView(getContext());v.setText(value);v.setTextSize(size);v.setTextColor(color);v.setTextDirection(View.TEXT_DIRECTION_FIRST_STRONG);return v;}
 private int dp(float n){return (int)KennyComponents.dp(getContext(),n);}
 void apply(GameActions.Status status){
  int label=status.availability==GameActions.Availability.INSTALLED?R.string.installed:status.availability==GameActions.Availability.NOT_INSTALLED?R.string.not_installed:R.string.edition_unknown;
  availability.setText(label);availability.setTextColor(status.launchable?Tokens.SELECTED:Tokens.SECONDARY);launch.setEnabled(status.launchable);
 }
 private static int launchId(int id){if(id==R.id.game_global)return R.id.launch_global;if(id==R.id.game_korea)return R.id.launch_korea;if(id==R.id.game_taiwan)return R.id.launch_taiwan;return R.id.launch_vietnam;}
 private static int fileId(int id){if(id==R.id.game_global)return R.id.files_global;if(id==R.id.game_korea)return R.id.files_korea;if(id==R.id.game_taiwan)return R.id.files_taiwan;return R.id.files_vietnam;}
}
