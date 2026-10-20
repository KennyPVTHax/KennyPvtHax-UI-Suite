package com.kennypvthax.uisuite;
import java.util.Arrays;
/** Testable local navigation/auth state. This controller does not authenticate keys. */
final class AuthController {
 enum Phase { READY,LOADING,ERROR,SIGNED_IN }
 enum Error { NONE,KEY_REQUIRED,NOT_CONFIGURED,FAILED,CANCELLED }
 static final class Session { final boolean development; Session(boolean development){this.development=development;} }
 static final class Result {
  final Session session;final Error error;
  private Result(Session s,Error e){session=s;error=e;}
  static Result failure(Error e){return new Result(null,e);}
  static Result verified(Session session){if(session==null||session.development)throw new IllegalArgumentException("Verified provider session required");return new Result(session,Error.NONE);}
 }
 private final AuthProvider provider;private Runnable listener=()->{};private AuthProvider.Request request;private int generation;
 private Phase phase=Phase.READY;private Error error=Error.NONE;private Session session;
 AuthController(AuthProvider p){provider=p;}
 synchronized void observe(Runnable r){listener=r==null?()->{}:r;}
 synchronized Phase phase(){return phase;}synchronized Error error(){return error;}synchronized Session session(){return session;}
 void submit(char[] key){
  final int own;final char[] copy;
  synchronized(this){if(phase==Phase.LOADING)return;if(key==null||key.length==0||key.length>4096){phase=Phase.ERROR;error=Error.KEY_REQUIRED;listener.run();return;}own=++generation;phase=Phase.LOADING;error=Error.NONE;copy=key.clone();}
  notifyListener();AuthProvider.Request next;
  try{next=provider.authenticate(copy,result->finish(own,result));}catch(RuntimeException failure){finish(own,Result.failure(Error.FAILED));next=()->{};}finally{Arrays.fill(copy,'\0');}
  synchronized(this){if(own==generation&&phase==Phase.LOADING)request=next;else if(next!=null)next.cancel();}
 }
 private void finish(int own,Result result){synchronized(this){if(own!=generation||phase!=Phase.LOADING)return;request=null;if(result!=null&&result.session!=null){session=result.session;phase=Phase.SIGNED_IN;error=Error.NONE;}else{phase=Phase.ERROR;error=result==null?Error.FAILED:result.error;}}notifyListener();}
 void cancel(){AuthProvider.Request old;synchronized(this){++generation;old=request;request=null;if(phase==Phase.LOADING){phase=Phase.ERROR;error=Error.CANCELLED;}}if(old!=null)old.cancel();notifyListener();}
 void logout(){cancel();synchronized(this){session=null;phase=Phase.READY;error=Error.NONE;}notifyListener();}
 private void notifyListener(){Runnable r;synchronized(this){r=listener;}r.run();}
 void close(){observe(null);cancel();}
}
