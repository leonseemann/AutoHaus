/******************************************************************************
 *  Compilation:  javac RSA.java
 *  Execution:    java RSA M
 *  
 *  Generate an M-bit public and private RSA key and use to encrypt
 *  and decrypt a random message.
 * 
 *  % java RSA 50
 *  public  = 65537
 *  private = 553699199426609
 *  modulus = 825641896390631
 *  message   = 48194775244950
 *  encrpyted = 321340212160104
 *  decrypted = 48194775244950
 *
 *  Known bugs (not addressed for simplicity)
 *  -----------------------------------------
 *  - It could be the case that the message >= modulus. To avoid, use
 *    a do-while loop to generate key until modulus happen to be exactly M bits.
 *
 *  - It's possible that gcd(phi, publicKey) != 1 in which case
 *    the key generation fails. This will only happen if phi is a
 *    multiple of 65537. To avoid, use a do-while loop to generate
 *    keys until the gcd is 1.
 *
 ******************************************************************************/

package de.autohaus.model;

import java.math.BigInteger;
import java.security.SecureRandom;
    

public class RSA {
   private final static BigInteger one = new BigInteger("1");

   private BigInteger privateKey;
   private BigInteger publicKey;
   private BigInteger modulus;

   // generate an M-bit (roughly) public and private key
   public RSA() {
      BigInteger p = new BigInteger("175543116836684002390533932317785518846980360755406982385561308462791083548159673806872140353685384450537690917809149602345441590647323570747158549171796306912075148151601899600918985801200479502637435463714772491347553683694484190002065028159152465052290770868614335965490115492634071485015557501029242399283");
      BigInteger q = new BigInteger("114382210409654424971356120123052948301782647734857786780406406168877691560413582289489982070324691915833305782878665753164415675952479751331249475461736265032666731662667064308761108671748318019335399501637719015524620871560309912954250405601967486946310032189867564277992393546969502501219657629029264063767");
      BigInteger phi = (p.subtract(one)).multiply(q.subtract(one));

      modulus    = p.multiply(q);    // = N                              
      publicKey  = new BigInteger("65537");   // = e  // common value in practice = 2^16 + 1
      privateKey = publicKey.modInverse(phi);  // = d
   }


   public BigInteger encrypt(BigInteger message) {
      return message.modPow(publicKey, modulus);
   }

   public BigInteger decrypt(BigInteger encrypted) {
      return encrypted.modPow(privateKey, modulus);
   }

   public String toString() {
      String s = "";
      s += "public  = " + publicKey  + "\n";
      s += "private = " + privateKey + "\n";
      s += "modulus = " + modulus;
      return s;
   }
 
   public static void main(String[] args) {
      // int M = Integer.parseInt(args[0]);
      RSA key = new RSA();
      System.out.println(key);
 
      // create random message, encrypt and decrypt
      // BigInteger message = new BigInteger(M-1, random);

      //// create message by converting string to integer
      String s = "SeemannJV2911#";
      byte[] bytes = s.getBytes();
      BigInteger message = new BigInteger(bytes);

      BigInteger encrypt = key.encrypt(message);
      BigInteger decrypt = key.decrypt(encrypt);
      System.out.println("message   = " + message);
      System.out.println("encrypted = " + encrypt);
      System.out.println("decrypted = " + decrypt);
   }
}