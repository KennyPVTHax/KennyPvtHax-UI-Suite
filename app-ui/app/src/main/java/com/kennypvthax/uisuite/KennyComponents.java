package com.kennypvthax.uisuite;
import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.*;
import android.view.*;
import android.widget.*;
/** Custom geometry using ordinary Android semantic controls for accessibility/input. */
public final class KennyComponents {
 static boolean reducedMotion;
 static float dp(Context c,float n){return n*c.getResources().getDisplayMetrics().density;}
 static GradientDrawable surface(Context c,int fill,int edge,float radius){GradientDrawable d=new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,new int[]{fill,fill});d.setCornerRadius(dp(c,radius));d.setStroke(Math.max(1,(int)dp(c,1)),edge);return d;}
 static StateListDrawable buttonSurface(Context c){StateListDrawable states=new StateListDrawable();states.addState(new int[]{-android.R.attr.state_enabled},surface(c,0x99100b0f,0x334b3c43,10));states.addState(new int[]{android.R.attr.state_pressed},surface(c,0xff442029,0xffab6273,10));states.addState(new int[]{android.R.attr.state_focused},surface(c,0xff30141e,0xffab6273,10));states.addState(new int[]{},surface(c,0x99100b0e,0x5c504149,10));return states;}
 public static Button button(Context c,String label,Runnable action){Button b=new KennyButton(c);b.setText(label);b.setAllCaps(false);b.setTextSize(15);b.setTextColor(Tokens.TEXT);b.setMinHeight((int)dp(c,48));b.setMinimumWidth(0);b.setPadding((int)dp(c,16),0,(int)dp(c,16),0);b.setBackground(buttonSurface(c));b.setStateListAnimator(null);b.setOnClickListener(v->action.run());return b;}
 private static final class KennyButton extends Button {
  private final Paint p=new Paint(3); private final RectF box=new RectF(); private float emphasis; private long previous;
  KennyButton(Context c){super(c);setWillNotDraw(false);}
  protected void onDraw(Canvas canvas){float density=getResources().getDisplayMetrics().density;long now=System.nanoTime();float target=isPressed()||isFocused()||isSelected()||"pressed".equals(getTag())?1:0;emphasis=reducedMotion?target:AuroraModel.approach(emphasis,target,18,previous==0?.1f:(now-previous)/1e9f);previous=now;
   if(Math.abs(emphasis-target)>.002f)postInvalidateOnAnimation();
   box.set(.5f*density,.5f*density,getWidth()-.5f*density,getHeight()-.5f*density);p.setStyle(Paint.Style.FILL);p.setColor(isEnabled()?blend("illuminated".equals(getTag())?0xff30141e:0xff100b0e,0xff442029,emphasis):0xff100b0f);canvas.drawRoundRect(box,10*density,10*density,p);p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(density);p.setColor(isEnabled()?blend(0x5c504149,0xffab6273,emphasis):0x334b3c43);canvas.drawRoundRect(box,10*density,10*density,p);p.setStyle(Paint.Style.FILL);if("navigation".equals(getTag())){p.setColor(AuroraBackground.rgba(Tokens.FIRE,emphasis*.65f));canvas.drawRect(16*density,getHeight()-2*density,getWidth()-16*density,getHeight()-density,p);}p.setTypeface(getTypeface());p.setTextSize(getTextSize());p.setColor(isEnabled()?getCurrentTextColor():0xff867579);p.setTextAlign(Paint.Align.CENTER);canvas.drawText(getText().toString(),getWidth()/2f,getHeight()/2f-(p.ascent()+p.descent())/2,p);
  }
 }
 static int blend(int a,int b,float t){int out=0;for(int shift=0;shift<32;shift+=8)out|=((int)(((a>>>shift)&255)*(1-t)+((b>>>shift)&255)*t))<<shift;return out;}
 public static LinearLayout panel(Context c){LinearLayout p=new LinearLayout(c);p.setOrientation(LinearLayout.VERTICAL);p.setPadding((int)dp(c,16),(int)dp(c,16),(int)dp(c,16),(int)dp(c,16));p.setBackground(surface(c,0xc41a1014,0x52665059,12));return p;}
 public static final class Check extends CompoundButton {
  private final Paint p=new Paint(3);private final Path path=new Path();private Shader uncheckedDisc,checkedDisc;private float checked;private long last;
  public Check(Context c,String label){super(c);setText(label);setGravity(Gravity.CENTER_VERTICAL|Gravity.START);setTextColor(Tokens.TEXT);setTextSize(15);setButtonDrawable((Drawable)null);setBackgroundColor(Color.TRANSPARENT);setMinHeight((int)dp(c,48));setPadding((int)dp(c,38),0,0,0);setWillNotDraw(false);setOnCheckedChangeListener((b,on)->invalidate());}
  protected void onSizeChanged(int w,int h,int oldw,int oldh){super.onSizeChanged(w,h,oldw,oldh);float d=getResources().getDisplayMetrics().density,y=h*.5f,r=12*d;uncheckedDisc=new LinearGradient(0,y-r,0,y+r,0xff241b21,0xff0c0b0f,Shader.TileMode.CLAMP);checkedDisc=new LinearGradient(0,y-r,0,y+r,0xffd72a45,0xffa40f29,Shader.TileMode.CLAMP);}
  protected void onDraw(Canvas c){super.onDraw(c);float d=getResources().getDisplayMetrics().density,dt=last==0?.1f:(System.nanoTime()-last)/1e9f;last=System.nanoTime();checked=reducedMotion? (isChecked()?1:0):AuroraModel.approach(checked,isChecked()?1:0,22,dt);if(Math.abs(checked-(isChecked()?1:0))>.002f)postInvalidateOnAnimation();float x=13*d,y=getHeight()*.5f,r=12*d;
   p.setStyle(Paint.Style.FILL);p.setShader(uncheckedDisc);p.setColor(0xffffffff);p.setAlpha(255);c.drawCircle(x,y,r,p);p.setShader(checkedDisc);p.setAlpha(Math.round(checked*255));c.drawCircle(x,y,r,p);p.setShader(null);p.setAlpha(255);p.setColor(0x997a4655);p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(d);c.drawCircle(x,y,r,p);
   p.setColor(AuroraBackground.rgba(Tokens.TEXT,checked));p.setStrokeWidth(2*d);p.setStrokeCap(Paint.Cap.ROUND);p.setStrokeJoin(Paint.Join.ROUND);path.rewind();path.moveTo(x-4*d,y);path.lineTo(x-d,y+3*d);path.lineTo(x+5*d,y-4*d);c.drawPath(path,p);p.setStyle(Paint.Style.FILL);
  }
 }
 public static final class Slider extends SeekBar {
  private final Paint paint=new Paint(3);
  public Slider(Context c){super(c);setMax(100);setProgress(55);setMinHeight((int)dp(c,48));setPadding((int)dp(c,12),0,(int)dp(c,12),0);setBackgroundColor(Color.TRANSPARENT);setThumb(null);setProgressDrawable(null);}
  protected synchronized void onMeasure(int width,int height){setMeasuredDimension(resolveSize((int)dp(getContext(),220),width),resolveSize((int)dp(getContext(),48),height));}
  protected synchronized void onDraw(Canvas c){float d=getResources().getDisplayMetrics().density,x=getPaddingLeft(),end=getWidth()-getPaddingRight(),y=getHeight()*.5f,pos=x+(end-x)*getProgress()/getMax();paint.setStrokeCap(Paint.Cap.ROUND);paint.setStrokeWidth(3*d);paint.setColor(0xff35252d);c.drawLine(x,y,end,y,paint);paint.setColor(Tokens.ACCENT);c.drawLine(x,y,pos,y,paint);paint.setColor(Tokens.FIRE);c.drawCircle(pos,y,7*d,paint);paint.setColor(0x99eca5b2);paint.setStyle(Paint.Style.STROKE);paint.setStrokeWidth(d);c.drawCircle(pos,y,7*d,paint);paint.setStyle(Paint.Style.FILL);}
 }
 public static EditText field(Context c){EditText e=new EditText(c);e.setTextSize(15);e.setTextColor(Tokens.TEXT);e.setHintTextColor(Tokens.SECONDARY);e.setSingleLine();e.setHint("Component label");e.setBackground(surface(c,Tokens.SUBTLE,Tokens.OUTLINE,8));e.setPadding((int)dp(c,12),0,(int)dp(c,12),0);e.setMinHeight((int)dp(c,48));return e;}
 private KennyComponents(){}
}
