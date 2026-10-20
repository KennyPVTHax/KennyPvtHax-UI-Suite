package com.kennypvthax.uisuite;
/** Only selected by a debuggable Activity's explicit test fixture. No accepted key.
 * A real pending controller request, cancelled by the UI/lifecycle, without timers. */
final class DebugPendingAuthProvider implements AuthProvider {
 public Request authenticate(char[] key,Callback callback){return ()->{};}
}
