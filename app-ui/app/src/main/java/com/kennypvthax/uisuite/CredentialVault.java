package com.kennypvthax.uisuite;
import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import java.security.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
/** App-private AES-GCM ciphertext. Key material stays in Android Keystore.
 * Called only on the shell's bounded IO worker, never from Canvas or audio. */
final class CredentialVault {
 private static final String ALIAS="kenny_license_key_v1";
 private final SharedPreferences prefs;
 CredentialVault(Context c){prefs=c.getSharedPreferences("kenny-credentials",Context.MODE_PRIVATE);}
 private SecretKey key()throws GeneralSecurityException,java.io.IOException{
  KeyStore ks=KeyStore.getInstance("AndroidKeyStore");ks.load(null);java.security.Key existing=ks.getKey(ALIAS,null);if(existing instanceof SecretKey)return (SecretKey)existing;
  KeyGenerator g=KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,"AndroidKeyStore");g.init(new KeyGenParameterSpec.Builder(ALIAS,KeyProperties.PURPOSE_ENCRYPT|KeyProperties.PURPOSE_DECRYPT).setBlockModes(KeyProperties.BLOCK_MODE_GCM).setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE).build());return g.generateKey();
 }
 void save(char[] value)throws GeneralSecurityException,java.io.IOException{
  if(value.length==0||value.length>4096)throw new IllegalArgumentException("Invalid key length");byte[] plain=new String(value).getBytes(StandardCharsets.UTF_8);
  try{Cipher c=Cipher.getInstance("AES/GCM/NoPadding");c.init(Cipher.ENCRYPT_MODE,key());byte[] encrypted=c.doFinal(plain);if(!prefs.edit().putString("iv",Base64.encodeToString(c.getIV(),Base64.NO_WRAP)).putString("ciphertext",Base64.encodeToString(encrypted,Base64.NO_WRAP)).commit())throw new java.io.IOException("Credential write failed");}finally{Arrays.fill(plain,(byte)0);}
 }
 char[] load()throws GeneralSecurityException,java.io.IOException{
  String data=prefs.getString("ciphertext",null);if(data==null)return new char[0];String ivText=prefs.getString("iv","");if(data.length()>24000||ivText.length()>32)throw new GeneralSecurityException("Credential data invalid");byte[] iv=Base64.decode(ivText,Base64.DEFAULT),encrypted=Base64.decode(data,Base64.DEFAULT);if(iv.length!=12||encrypted.length<16||encrypted.length>16400)throw new GeneralSecurityException("Credential data invalid");Cipher c=Cipher.getInstance("AES/GCM/NoPadding");c.init(Cipher.DECRYPT_MODE,key(),new GCMParameterSpec(128,iv));byte[] plain=c.doFinal(encrypted);try{return new String(plain,StandardCharsets.UTF_8).toCharArray();}finally{Arrays.fill(plain,(byte)0);}
 }
 void forget()throws java.io.IOException{if(!prefs.edit().remove("iv").remove("ciphertext").commit())throw new java.io.IOException("Credential removal failed");}
}
