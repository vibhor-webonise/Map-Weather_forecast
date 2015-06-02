import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by webonise on 01-06-2015.
 */
public class VerifySign {

    public static void main(String args[]) {

        /* Verify a DSA signature */
       /* if (args.length != 3) {
            System.out.println("Usage: VerSig public key file signature file datafile");
        }
        else */try{

            /* import encoded public key */

            FileInputStream keyFileInputStream = new FileInputStream("C:/Digital Sign files/suepk.txt");
            byte[] encKey = new byte[keyFileInputStream.available()];
            keyFileInputStream.read(encKey);
            keyFileInputStream.close();

            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);

            KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
            PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);

            /* input the signature bytes */
            FileInputStream sigFileInputStream = new FileInputStream("C:/Digital Sign files/sig.txt");
            byte[] sigToVerify = new byte[sigFileInputStream.available()];
            sigFileInputStream.read(sigToVerify );
            sigFileInputStream.close();

            /* create a Signature object and initialize it with the public key */
            Signature sig = Signature.getInstance("SHA1withDSA", "SUN");
            sig.initVerify(pubKey);

            /* Update and verify the data */

            FileInputStream dataFileInputStream = new FileInputStream("C:/Digital Sign files/Vibhor Kansal.pdf");
            BufferedInputStream bufferedInputStream = new BufferedInputStream(dataFileInputStream);

            byte[] buffer = new byte[1024];
            int len;
            while (bufferedInputStream.available() != 0) {
                len = bufferedInputStream.read(buffer);
                sig.update(buffer, 0, len);
            }
            bufferedInputStream.close();


            boolean verifies = sig.verify(sigToVerify);

            System.out.println("signature verifies: " + verifies);


        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
    }
}
