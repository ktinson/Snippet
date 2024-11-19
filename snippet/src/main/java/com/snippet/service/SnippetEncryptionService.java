package com.snippet.service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SnippetEncryptionService {

  // 16-byte key (AES-128)
  private static final String ALGORITHM = "AES";
  @Value("$SNIPPET_SECRET_KEY")
  private String secretKey;
    // Encrypt the snippet code
    public String encrypt(String code) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encrypted = cipher.doFinal(code.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    // Decrypt the snippet code
    public String decrypt(String encryptedCode) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedCode));
        return new String(decrypted);
    }
}
