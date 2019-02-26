package utils;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.util.Date;

public class KeyConverter {

    public static String converter() throws GeneralSecurityException, IOException {
        Date date = new Date();
        String scope = "fullname";
        String timestamp = DateConverter.converterToTimestamp(date).toString();
        String client_id = "OBRN15";

        String res = scope + timestamp + client_id;

        PrivateKey pk = pemFileLoadPrivateKeyPkcs1OrPkcs8Encoded(new File("/home/valerey/IdeaProjects/esiamoduleautorisation/src/main/resources/Keys/frdo_private.key"));
        Signature rsa = Signature.getInstance("SHA256withRSA");
        rsa.initSign(pk);
        rsa.update(res.getBytes()); // здесь нужно подписать то, что нужно
        String sign = new String(rsa.sign()); // подпись
        return sign;
    }


    public static void main(String[] args) throws Exception {
        Date date = new Date();
        String scope = "fullname";
        String timestamp = DateConverter.converterToTimestamp(date).toString();
        String client_id = "OBRN15";

        String res = scope + timestamp + client_id;

        PrivateKey pk = pemFileLoadPrivateKeyPkcs1OrPkcs8Encoded(new File("/home/valerey/IdeaProjects/esiamoduleautorisation/src/main/resources/Keys/frdo_private.key"));
        Signature rsa = Signature.getInstance("SHA256withRSA");
        rsa.initSign(pk);
        rsa.update(res.getBytes()); // здесь нужно подписать то, что нужно
        String sign = new String(rsa.sign()); // подпись
        System.out.println(sign);
    }

    public static PrivateKey pemFileLoadPrivateKeyPkcs1OrPkcs8Encoded(File pemFileName) throws GeneralSecurityException, IOException {
        // PKCS#8 format
        final String PEM_PRIVATE_START = "-----BEGIN PRIVATE KEY-----";
        final String PEM_PRIVATE_END = "-----END PRIVATE KEY-----";

        // PKCS#1 format
        final String PEM_RSA_PRIVATE_START = "-----BEGIN RSA PRIVATE KEY-----";
        final String PEM_RSA_PRIVATE_END = "-----END RSA PRIVATE KEY-----";

        Path path = Paths.get(pemFileName.getAbsolutePath());

        String privateKeyPem = new String(Files.readAllBytes(path));

        if (privateKeyPem.contains(PEM_PRIVATE_START)) { // PKCS#8 format
            privateKeyPem = privateKeyPem.replace(PEM_PRIVATE_START, "").replace(PEM_PRIVATE_END, "");
            privateKeyPem = privateKeyPem.replaceAll("\\s", "");

            byte[] pkcs8EncodedKey = Base64.decode(privateKeyPem);

            KeyFactory factory = KeyFactory.getInstance("RSA");
            return factory.generatePrivate(new PKCS8EncodedKeySpec(pkcs8EncodedKey));

        } else if (privateKeyPem.contains(PEM_RSA_PRIVATE_START)) {  // PKCS#1 format

            privateKeyPem = privateKeyPem.replace(PEM_RSA_PRIVATE_START, "").replace(PEM_RSA_PRIVATE_END, "");
            privateKeyPem = privateKeyPem.replaceAll("\\s", "");

            DerInputStream derReader = new DerInputStream(Base64.decode(privateKeyPem));

            DerValue[] seq = derReader.getSequence(0);

            if (seq.length < 9) {
                throw new GeneralSecurityException("Could not parse a PKCS1 private key.");
            }

            // skip version seq[0];
            BigInteger modulus = seq[1].getBigInteger();
            BigInteger publicExp = seq[2].getBigInteger();
            BigInteger privateExp = seq[3].getBigInteger();
            BigInteger prime1 = seq[4].getBigInteger();
            BigInteger prime2 = seq[5].getBigInteger();
            BigInteger exp1 = seq[6].getBigInteger();
            BigInteger exp2 = seq[7].getBigInteger();
            BigInteger crtCoef = seq[8].getBigInteger();

            RSAPrivateCrtKeySpec keySpec = new RSAPrivateCrtKeySpec(modulus, publicExp, privateExp, prime1, prime2, exp1, exp2, crtCoef);

            KeyFactory factory = KeyFactory.getInstance("RSA");

            return factory.generatePrivate(keySpec);
        }
        throw new GeneralSecurityException("Not supported format of a private key");
    }
}