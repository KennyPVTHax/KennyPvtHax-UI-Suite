package com.kennypvthax.uisuite;
/** Direct mathematical adaptation of KennyAurora.h and KennyEffects::Hash.
 * Fixed storage; no Android imports, rendering, SDK, hooks, media, or IO. */
public final class AuroraModel {
 public static final int STARS=40,COLUMNS=96,ROWS=18,LAYERS=3;
 public static final float STEP=1f/120;
 public final float[] x=new float[STARS],y=new float[STARS],vx=new float[STARS],vy=new float[STARS],radius=new float[STARS],lum=new float[STARS];
 public double time,particleTime; public float response,touchX=.5f,touchY=.75f; private float accumulator;
 public AuroraModel(){for(int i=0;i<STARS;i++){x[i]=homeX(i,0);y[i]=homeY(i,0);radius[i]=.55f+unit(i*11+887)*.85f;lum[i]=.25f+unit(i*11+997)*.48f;}}
 public static float clamp(float x,float a,float b){return Math.max(a,Math.min(b,x));}
 public static float smooth(float v){v=clamp(v,0,1);return v*v*(3-2*v);}
 public static float delta(float dt){return Float.isFinite(dt)?clamp(dt,0,.1f):0;}
 public static float approach(float a,float b,float rate,float dt){return a+(b-a)*(1-(float)Math.exp(-rate*delta(dt)));}
 public static float unit(int x){x^=x>>>16;x*=0x7feb352d;x^=x>>>15;x*=0x846ca68b;x^=x>>>16;return (x&65535)/65535f;}
 public static float noise(double x,int seed){double cell=Math.floor(x);int key=(int)(long)cell;float f=smooth((float)(x-cell)),a=unit(key^seed);return a+(unit((key+1)^seed)-a)*f;}
 public static float field(double x,double t,int seed){return .57f*noise(x+t,seed)+.28f*noise(x*2.17-t*.73,seed+71)+.15f*noise(x*4.31+t*.37,seed+131);}
 public static float homeX(int i,double t){return .09f+.82f*unit(i*11+319)+.025f*(field(i*1.73,t*(.028f+.018f*unit(i*11+503)),47)-.5f);}
 public static float homeY(int i,double t){return .09f+.82f*unit(i*11+761)+.035f*(field(i*2.11,t*(.028f+.018f*unit(i*11+503)),211)-.5f);}
 public void advance(float dt,float aspect,boolean down,float tx,float ty,boolean reduced){
  if(reduced){response=0;accumulator=0;return;}dt=delta(dt);if(!Float.isFinite(tx)||!Float.isFinite(ty))down=false;
  if(down){touchX=approach(touchX,clamp(tx,0,1),14,dt);touchY=approach(touchY,clamp(ty,0,1),14,dt);}accumulator+=dt;
  for(int n=0;n<12&&accumulator+1e-7f>=STEP;n++){step(STEP,Math.max(.01f,aspect),down);accumulator=Math.max(0,accumulator-STEP);}
 }
 private void step(float dt,float aspect,boolean down){time+=dt;particleTime+=dt;response=approach(response,down?1:0,down?7:2.8f,dt);float damping=(float)Math.exp(-4.8f*dt);
  for(int i=0;i<STARS;i++){
   float dx=(touchX-x[i])*aspect,dy=touchY-y[i],distance=(float)Math.hypot(dx,dy),weight=down?smooth(1-distance/.38f)*response:0;
   float ax=(homeX(i,particleTime)-x[i])*3.5f+(touchX-x[i])*22*weight,ay=(homeY(i,particleTime)-y[i])*3.5f+(touchY-y[i])*22*weight;
   vx[i]=(vx[i]+ax*dt)*damping;vy[i]=(vy[i]+ay*dt)*damping;float speed=(float)Math.hypot(vx[i]*aspect,vy[i]);if(speed>.28f){vx[i]*=.28f/speed;vy[i]*=.28f/speed;}
   x[i]+=vx[i]*dt;y[i]+=vy[i]*dt;if(x[i]<.065f){x[i]=.065f;vx[i]=Math.max(0,vx[i]);}if(x[i]>.935f){x[i]=.935f;vx[i]=Math.min(0,vx[i]);}if(y[i]<.065f){y[i]=.065f;vy[i]=Math.max(0,vy[i]);}if(y[i]>.935f){y[i]=.935f;vy[i]=Math.min(0,vy[i]);}
  }
 }
 /** Five values per column: lower hem, height, fold, illumination, lean. */
 public static void curtain(float x,double time,int layer,float[] out,int offset){int seed=137+layer*313;double drift=time*(.105+layer*.031);float sweep=field(x*4.6,drift,seed),fold=field(x*14.3,drift*1.31,seed+9),ray=noise(x*47.7-drift*.57,seed+37);
  out[offset]=.67f+layer*.10f+(sweep-.5f)*.28f+(fold-.5f)*.035f;out[offset+1]=.27f+field(x*5.1,drift*.64,seed+29)*.31f;out[offset+2]=fold;out[offset+3]=(.16f+.28f*fold+.56f*ray*ray)*(.78f+.22f*field(x*5,time*.08,layer+939));out[offset+4]=(field(x*3.7,drift*.43,seed+63)-.5f)*.115f;
 }
}
