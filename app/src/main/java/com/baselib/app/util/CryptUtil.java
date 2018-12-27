
package com.baselib.app.util;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * Created by snowd on 14-8-22.
 */
public class CryptUtil {

    // =========== this iv bytes is very important!!! ======
    private static byte[] ivBytes = {
            1, 2, 3, 4, 5, 6, 7, 8
    };

    /**
     * 对称加密字节数组并返回
     * 
     * @param byteSource 需要加密的数据
     * @return 经过加密的数据
     * @throws Exception
     */
    public static byte[] preformDESEncrypto(byte[] byteSource, byte[] byteKey) throws Exception {
        try {
            int mode = Cipher.ENCRYPT_MODE;
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            DESKeySpec keySpec = new DESKeySpec(byteKey);
            Key key = keyFactory.generateSecret(keySpec);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivBytes);
            cipher.init(mode, key, iv);
            byte[] result = cipher.doFinal(byteSource);
            return result;
        } catch (Exception e) {
            throw e;
        } finally {
        }
    }

    /**
     * 对称解密字节数组并返回
     * 
     * @param byteSource 需要解密的数据
     * @return 经过解密的数据
     * @throws Exception
     */
    public static byte[] preformDESDecrypto(byte[] byteSource, byte[] byteKey) throws Exception {
        try {
            int mode = Cipher.DECRYPT_MODE;
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            DESKeySpec keySpec = new DESKeySpec(byteKey);
            Key key = keyFactory.generateSecret(keySpec);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivBytes);
            cipher.init(mode, key, iv);
            byte[] result = cipher.doFinal(byteSource);
            return result;
        } catch (Exception e) {
            throw e;
        } finally {
        }
    }

}
