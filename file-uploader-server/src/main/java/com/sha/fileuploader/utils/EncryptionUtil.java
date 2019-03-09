package com.sha.fileuploader.utils;

import org.apache.commons.codec.binary.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

public class EncryptionUtil {
    private static final byte[] SALT = {(byte) 0x21, (byte) 0x21, (byte) 0xF0, (byte) 0x55, (byte) 0xC3, (byte) 0x9F, (byte) 0x5A, (byte) 0x75};

    private final static int ITERATION_COUNT = 31;


    public static String encrypt(final String parametre) {
        return encryptWithPBE(parametre);
    }

    public static String decrypt(final String parametre) {
        return decryptWithPBE(parametre);
    }

    private static String encryptWithPBE(final String parameter) {
        if(parameter == null) {
            throw new IllegalArgumentException();
        }
        try {

            final KeySpec keySpec = new PBEKeySpec(null, SALT, ITERATION_COUNT);
            final AlgorithmParameterSpec paramSpec = new PBEParameterSpec(SALT, ITERATION_COUNT);

            final SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

            final Cipher ecipher = Cipher.getInstance(key.getAlgorithm());
            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);

            final byte[] enc = ecipher.doFinal(parameter.getBytes());

            String res = new String(Base64.encodeBase64(enc));
            res = res.replace('+', '-').replace('/', '_').replace("%", "%25").replace("\n", "%0A").replace("=", "%3d").replace("&", "%26");

            return res;

        }
        catch(final Exception e) {
        }

        return "";

    }

    private static String decryptWithPBE(final String parameter) {
        if(parameter == null) {
            return null;
        }
        try {
            final String input = parameter.replace("%26", "&").replace("%3d", "=").replace("%0A", "\n").replace("%25", "%").replace('_', '/').replace('-', '+');

            final byte[] dec = Base64.decodeBase64(input.getBytes());

            final KeySpec keySpec = new PBEKeySpec(null, SALT, ITERATION_COUNT);
            final AlgorithmParameterSpec paramSpec = new PBEParameterSpec(SALT, ITERATION_COUNT);

            final SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

            final Cipher dcipher = Cipher.getInstance(key.getAlgorithm());
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

            final byte[] decoded = dcipher.doFinal(dec);

            final String result = new String(decoded);
            return result;

        }
        catch(final Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
