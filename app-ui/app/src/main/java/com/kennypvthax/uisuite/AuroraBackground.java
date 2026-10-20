package com.kennypvthax.uisuite;
import android.content.Context;
import android.graphics.*;
import android.view.*;
/** Cached triangle arrays reproduce original ribbon tessellation; no shader/texture lifecycle. */
final class AuroraBackground extends View implements Choreographer.FrameCallback {
 final AuroraModel model=new AuroraModel(); boolean reduced,active,down;float tx=.5f,ty=.75f;private long previous;
 private final Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);private final Path clip=new Path();private final RectF bounds=new RectF();
 private final float[] columns=new float[97*5],points=new float[97*19*2],triangles=new float[96*18*6*2];
 private final int[] colors=new int[97*19],triangleColors=new int[96*18*6];private final int[] order={0,1,97,1,98,97};
 AuroraBackground(Context c){super(c);setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_NO);}
 void start(){if(!active){active=true;previous=0;if(!reduced)Choreographer.getInstance().postFrameCallback(this);invalidate();}}
 void stop(){active=false;previous=0;down=false;Choreographer.getInstance().removeFrameCallback(this);}
 void setReduced(boolean value){reduced=value;if(value)model.advance(0,1,false,0,0,true);previous=0;Choreographer.getInstance().removeFrameCallback(this);if(active&&!reduced)Choreographer.getInstance().postFrameCallback(this);invalidate();}
 public void doFrame(long now){if(!active||reduced)return;float dt=previous==0?0:(now-previous)/1e9f;previous=now;model.advance(dt,getWidth()/(float)Math.max(1,getHeight()),down,tx,ty,false);invalidate();Choreographer.getInstance().postFrameCallback(this);}
 void touch(float x,float y,boolean held){down=held;tx=x/Math.max(1,getWidth());ty=y/Math.max(1,getHeight());}
 static int rgba(int rgb,float a){return (Math.round(AuroraModel.clamp(a,0,1)*255)<<24)|(rgb&0xffffff);}
 protected void onDraw(Canvas c){super.onDraw(c);float w=getWidth(),h=getHeight(),scale=getResources().getDisplayMetrics().density;if(w<=0||h<=0)return;
  bounds.set(0,0,w,h);clip.rewind();clip.addRoundRect(bounds,34*scale,34*scale,Path.Direction.CW);c.save();c.clipPath(clip);c.drawColor(Tokens.BG);
  // Original quiet graphite shelves (full-screen proportional adaptation).
  paint.setColor(0x4520171e);c.drawRect(0,0,w,100*scale,paint);paint.setColor(0x59100b0f);c.drawRect(0,100*scale,w,160*scale,paint);
  for(int layer=0;layer<3;layer++){
   for(int x=0;x<=96;x++)AuroraModel.curtain(x/96f,model.time,layer,columns,x*5);
   for(int y=0;y<=18;y++){float v=y/18f,fade=AuroraModel.smooth(v/.33f)*AuroraModel.smooth((1-v)/.13f),hem=(float)Math.exp(-Math.pow((v-.86f)/.115f,2));
    for(int x=0;x<=96;x++){float u=x/96f;int k=x*5,index=y*97+x;float px=u+columns[k+4]*(1-v)*(1-v),py=columns[k]-columns[k+1]*(1-v);
     float dx=(px-model.touchX)*w/h,dy=py-model.touchY,local=AuroraModel.smooth(1-(float)Math.hypot(dx,dy)/.31f)*model.response;px+=local*dx*.025f;py-=local*.022f*(1-v);
     points[index*2]=AuroraModel.clamp(px*w,.8f,w-.8f);points[index*2+1]=AuroraModel.clamp(py*h,.8f,h-.8f);
     float composition=layer==1?.45f+.55f*AuroraModel.smooth(u/.4f):.30f+.70f*AuroraModel.smooth((.83f-u)/.45f);
     float edges=AuroraModel.smooth(u/.13f)*AuroraModel.smooth((1-u)/.14f)*composition,shelf=AuroraModel.smooth((py*h-65*scale)/(240*scale));
     int base=layer==0?0x75142c:layer==1?0xb01938:0xd12b43;float mix=hem*.34f+local*.10f;int rr=(int)(((base>>16)&255)*(1-mix)+243*mix),gg=(int)(((base>>8)&255)*(1-mix)+91*mix),bb=(int)((base&255)*(1-mix)+115*mix);
     colors[index]=rgba((rr<<16)|(gg<<8)|bb,fade*edges*shelf*columns[k+3]*(.24f+.25f*hem)*(1+local*.45f));
    }
   }
   int n=0;for(int y=0;y<18;y++)for(int x=0;x<96;x++){int a=y*97+x;for(int i=0;i<6;i++){int index=a+order[i];triangles[n*2]=points[index*2];triangles[n*2+1]=points[index*2+1];triangleColors[n++]=colors[index];}}
   paint.setColor(Color.WHITE);c.drawVertices(Canvas.VertexMode.TRIANGLES,n*2,triangles,0,null,0,triangleColors,0,null,0,0,paint);
  }
  for(int i=0;i<40;i++){float x=model.x[i]*w,y=model.y[i]*h,twinkle=reduced?.7f:.57f+.43f*AuroraModel.field(i*3.3,model.particleTime*.55,i+1071),shimmer=!reduced&&i%13==0?(float)Math.pow(AuroraModel.noise(model.particleTime*.28,i+1871),12):0;
   float near=AuroraModel.smooth(1-(float)Math.hypot((model.x[i]-model.touchX)*w/h,model.y[i]-model.touchY)/.38f)*model.response,alpha=Math.min(.95f,model.lum[i]*twinkle+shimmer*.35f+near*.2f),radius=Math.max(.55f,model.radius[i]*Math.min(scale,1.5f));
   paint.setColor(rgba(0xff5664,alpha*.085f));c.drawCircle(x,y,radius+2.5f*scale,paint);paint.setColor(rgba(i%4!=0?0xe3bcc7:0xf6eaf0,alpha));c.drawCircle(x,y,radius,paint);
  }c.restore();
 }
}
