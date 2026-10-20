package com.kennypvthax.uisuite;
/** Backend boundary. Implementations consume/copy key before returning and never log it.
 * No production endpoint or authentication response is inferred from the video. */
interface AuthProvider {
 interface Request { void cancel(); }
 interface Callback { void complete(AuthController.Result result); }
 Request authenticate(char[] key,Callback callback);
}
final class UnconfiguredAuthProvider implements AuthProvider {
 public Request authenticate(char[] key,Callback callback){callback.complete(AuthController.Result.failure(AuthController.Error.NOT_CONFIGURED));return ()->{};}
}
