package net.nqlab.simple.model;

import android.util.Log;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

public class SecureSaveData {
    private static final String KEY_PROVIDER = "AndroidKeyStore";
    private static final String KEY_ALIAS = "sample key";
    private static final String ALGORITHM = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";

    private static final String TAG = "KeyStoreProvider";

    private KeyStore mKeyStore = null;

    public SecureSaveData()
    {
        prepareKeyStore();
    }

    private void prepareKeyStore() {
        try {
            mKeyStore = KeyStore.getInstance(KEY_PROVIDER);
            mKeyStore.load(null);
            createNewKey();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    private void createNewKey() {
        try {
            // Create new key if needed
            if (!mKeyStore.containsAlias(KEY_ALIAS)) {
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_RSA, KEY_PROVIDER);
                keyPairGenerator.initialize(
                    new KeyGenParameterSpec.Builder(
                        KEY_ALIAS,
                        KeyProperties.PURPOSE_DECRYPT)
                        .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)
                        .build());
                keyPairGenerator.generateKeyPair();
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public String encryptKey(String plainText) {
        String encryptedText = null;
        try {
            PublicKey publicKey = mKeyStore.getCertificate(KEY_ALIAS).getPublicKey();

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CipherOutputStream cipherOutputStream = new CipherOutputStream(
                outputStream, cipher);
            cipherOutputStream.write(plainText.getBytes("UTF-8"));
            cipherOutputStream.close();

            byte [] bytes = outputStream.toByteArray();
            encryptedText = Base64.encodeToString(bytes, Base64.DEFAULT);

        } catch (Exception e) {
            Log.e(TAG, e.toString());

        }

        return encryptedText;
    }

    public String decryptKey(String encryptedText) {
        String plainText = null;
        try {
            PrivateKey privateKey = (PrivateKey) mKeyStore.getKey(KEY_ALIAS, null);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            CipherInputStream cipherInputStream = new CipherInputStream(
                    new ByteArrayInputStream(Base64.decode(encryptedText, Base64.DEFAULT)), cipher
                );

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int b;
            while ((b = cipherInputStream.read()) != -1) {
                outputStream.write(b);
            }
            outputStream.close();
            plainText = outputStream.toString("UTF-8");

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return plainText;
    }

    public byte[] generatePassword(String password) {
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            byte[] salt = new byte[32];
            random.nextBytes(salt);

            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec ks = new PBEKeySpec(password.toCharArray(), salt, 10000, 128);
            SecretKey s = f.generateSecret(ks);

            return s.getEncoded();

        } catch (Exception e) {
            Log.e(TAG, e.toString());

        }
        return null;
    }

    public byte[] generateInitialVector() {
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            byte[] iv = new byte[16];
            random.nextBytes(iv);
            return iv;

        } catch (Exception e) {
            Log.e(TAG, e.toString());

        }
        return null;
    }

    public String fromByteArrayToString(byte[] data) {
        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    public byte[] fromStringToByteArray(String data) {
        return Base64.decode(data, Base64.DEFAULT);
    }

    public String encryptString(byte[] password, byte[] ivData, String data) {
        try {
            SecretKey skey = new SecretKeySpec(password, "AES");
            IvParameterSpec iv = new IvParameterSpec(ivData);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skey, iv);
            byte[] byteResult = cipher.doFinal(data.getBytes("UTF-8"));
            return Base64.encodeToString(byteResult, Base64.DEFAULT);

        } catch (Exception e) {
            Log.e(TAG, e.toString());

        }
        return null;
    }
}


