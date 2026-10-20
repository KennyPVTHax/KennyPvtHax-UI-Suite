import com.kennypvthax.uisuite.AuroraModel;
public final class AuroraCheck {
 public static void main(String[] args){
  if(args.length>0){float[] col=new float[5];for(int i=0;i<40;i++){System.out.printf(java.util.Locale.ROOT,"%.9f %.9f %.9f\n",AuroraModel.unit(i*11+319),AuroraModel.homeX(i,3),AuroraModel.homeY(i,3));}for(int l=0;l<3;l++)for(int i=0;i<97;i++){AuroraModel.curtain(i/96f,3,l,col,0);for(float v:col)System.out.printf(java.util.Locale.ROOT,"%.9f ",v);System.out.println();}return;}
  AuroraModel a=new AuroraModel(),b=new AuroraModel();for(int n=0;n<1200;n++)a.advance(1f/120,1.7f,false,.5f,.5f,false);for(int n=0;n<300;n++)b.advance(1f/30,1.7f,false,.5f,.5f,false);
  for(int i=0;i<40;i++){if(Math.abs(a.x[i]-b.x[i])>1e-5||Math.abs(a.y[i]-b.y[i])>1e-5)throw new AssertionError("rate mismatch");}
  for(int hz:new int[]{60,90}){AuroraModel c=new AuroraModel();for(int n=0;n<hz*10;n++)c.advance(1f/hz,1.7f,false,.5f,.5f,false);for(int i=0;i<40;i++)if(Math.abs(c.x[i]-a.x[i])>1e-5||Math.abs(c.y[i]-a.y[i])>1e-5)throw new AssertionError("60/90Hz parity");}
  double before=0;for(int i=0;i<40;i++)before+=Math.hypot((a.x[i]-.5)*1.7,a.y[i]-.5);
  float initial=a.x[0];for(int n=0;n<1200;n++)a.advance(1f/120,1.7f,true,.5f,.5f,false);if(a.response<.9)throw new AssertionError("no touch response");double after=0;for(int i=0;i<40;i++)after+=Math.hypot((a.x[i]-.5)*1.7,a.y[i]-.5);if(after>=before-.01)throw new AssertionError("no attraction");for(int n=0;n<2400;n++)a.advance(1f/120,1.7f,false,.5f,.5f,false);if(a.response>.001)throw new AssertionError("release");
  for(int i=0;i<40;i++){if(a.x[i]<.065f||a.x[i]>.935f||a.y[i]<.065f||a.y[i]>.935f)throw new AssertionError("bounds");if(Math.abs(a.x[i]-AuroraModel.homeX(i,a.particleTime))>.003)throw new AssertionError("home spring");}
  double frozen=a.time;a.advance(1,1,false,0,0,true);if(a.time!=frozen)throw new AssertionError("reduced motion");a.advance(Float.NaN,1,true,Float.NaN,0,false);if(!Double.isFinite(a.time))throw new AssertionError("NaN");a.advance(600,1,false,0,0,false);if(a.time-frozen>.101)throw new AssertionError("stall clamp");
  System.out.println("PASS deterministic initialization, 30/120Hz parity, touch/release, bounds, reduced motion, NaN and stall clamp");
 }
}
