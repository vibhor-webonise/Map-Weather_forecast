import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.*;

/**
 * Created by webonise on 01-06-2015.
 */
public class GenSign {

    public static void main(String[] args) {

        /* Generate a DSA signature */
       /* if (args.length != 1) {
            System.out.println("Usage: GenSig nameOfFileToSign");
        }
        else*/ try{
            /* Generate a key pair */

            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");

            keyGen.initialize(1024, random);

            KeyPair pair = keyGen.generateKeyPair();
            PrivateKey privateKey = pair.getPrivate();
            PublicKey publicKey = pair.getPublic();

            /* Create a Signature object and initialize it with the private key */

            Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");
            dsa.initSign(privateKey);

            /* Update and sign the data */

            FileInputStream fileInputStream = new FileInputStream("C:/Digital Sign files/Vibhor Kansal.pdf");
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[1024];
            int len;
            while (bufferedInputStream.available() != 0) {
                len = bufferedInputStream.read(buffer);
                dsa.update(buffer, 0, len);
            }
            bufferedInputStream.close();

            /* Now that all the data to be signed has been read in,
                    generate a signature for it */

            byte[] realSig = dsa.sign();

            /* Save the signature in a file */
            FileOutputStream signFileOutputStream = new FileOutputStream("C:/Digital Sign files/sig.txt");
            signFileOutputStream.write(realSig);
            signFileOutputStream.close();


            /* Save the public key in a file */
            byte[] key = publicKey.getEncoded();
            FileOutputStream keyFileOutputStream = new FileOutputStream("C:/Digital Sign files/suepk.txt");
            keyFileOutputStream.write(key);
            keyFileOutputStream.close();

        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }

    }

}
