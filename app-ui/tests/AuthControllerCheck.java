package com.kennypvthax.uisuite;
import java.util.Arrays;
public final class AuthControllerCheck {
 private static void check(boolean ok,String message){if(!ok)throw new AssertionError(message);}
 static final class Manual implements AuthProvider {
  Callback callback;char[] received;int requests,cancels;
  public Request authenticate(char[] key,Callback next){callback=next;received=key;requests++;return ()->cancels++;}
 }
 public static void main(String[] args){
  AuthController unavailable=new AuthController(new UnconfiguredAuthProvider());
  unavailable.submit(new char[0]);check(unavailable.error()==AuthController.Error.KEY_REQUIRED,"empty key");
  unavailable.submit("not-a-real-credential".toCharArray());check(unavailable.error()==AuthController.Error.NOT_CONFIGURED,"no invented production auth");
  Manual service=new Manual();AuthController c=new AuthController(service);char[] caller="local-test".toCharArray();c.submit(caller);
  check(c.phase()==AuthController.Phase.LOADING,"real request loading");check(new String(caller).equals("local-test"),"caller ownership");
  for(char value:service.received)check(value==0,"temporary key buffer not wiped");
  c.submit(caller);check(service.requests==1,"duplicate request");AuthProvider.Callback late=service.callback;c.cancel();
  late.complete(AuthController.Result.verified(new AuthController.Session(false)));check(c.phase()==AuthController.Phase.ERROR&&c.session()==null,"late callback ignored");check(service.cancels==1,"cancellation");
  c.submit(caller);service.callback.complete(AuthController.Result.failure(AuthController.Error.FAILED));check(c.phase()==AuthController.Phase.ERROR,"failure");
  c.submit(caller);service.callback.complete(AuthController.Result.verified(new AuthController.Session(false)));check(c.phase()==AuthController.Phase.SIGNED_IN,"verified provider interface transition");c.logout();check(c.phase()==AuthController.Phase.READY&&c.session()==null,"logout state");
  AuthController broken=new AuthController((key,callback)->{throw new IllegalStateException("test provider unavailable");});broken.submit(caller);check(broken.error()==AuthController.Error.FAILED,"provider failure contained");
  c.enterDevelopmentPreview();check(c.phase()==AuthController.Phase.SIGNED_IN&&c.session().development,"explicit development preview");c.logout();check(c.session()==null,"preview logout");
  String[] packages={"com.tencent.ig","com.pubg.krmobile","com.rekoo.pubgm","com.vng.pubgmobile"};
  GameCatalog.Edition[] editions=GameCatalog.editions();check(editions.length==4,"four editions");
  for(int i=0;i<4;i++)check(editions[i].packageName.equals(packages[i])&&GameCatalog.permits(packages[i]),"edition allowlist");
  editions[0]=null;check(GameCatalog.editions()[0]!=null,"catalog array ownership");check(!GameCatalog.permits("unrelated.package")&&!GameCatalog.permits(null),"reject unrelated launch");
  c.close();Arrays.fill(caller,'\0');System.out.println("PASS auth state/cancellation, no configured service, key-buffer ownership, duplicate suppression, provider failure and logout contract; no production authentication claimed");
 }
}
