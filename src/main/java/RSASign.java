import sun.misc.BASE64Encoder;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;

/**
 * Created by webonise on 01-06-2015.
 */
public class RSASign {

    public static void main(String[] args) throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024);
        KeyPair keyPair = kpg.genKeyPair();
        System.out.println("Public Key: "+keyPair.getPublic());
        System.out.println("Private Key: "+keyPair.getPrivate());

        byte[] data = "test".getBytes("UTF8");

        Signature sig = Signature.getInstance("MD5WithRSA");
        sig.initSign(keyPair.getPrivate());
        sig.update(data);
        byte[] signatureBytes = sig.sign();
        System.out.println("Signature:" + new BASE64Encoder().encode(signatureBytes));

        sig.initVerify(keyPair.getPublic());
        sig.update(data);

        System.out.println(sig.verify(signatureBytes));
    }
}
