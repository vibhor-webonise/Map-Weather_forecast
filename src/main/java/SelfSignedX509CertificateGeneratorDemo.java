import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V1CertificateGenerator;

import javax.security.auth.x500.X500Principal;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

/**
 * Demo of a generation of a X509 Self Signed Certificate using <a
 * href="http://www.bouncycastle.org/">Bouncy Castle</a> library.
 *
 * @author <a href="mailto:cyrille@cyrilleleclerc.com">Cyrille Le Clerc</a>
 */
public class SelfSignedX509CertificateGeneratorDemo {

    private static final int keysize = 1024;
    private static final String commonName = "www.test.de";
    private static final String organizationalUnit = "IT";
    private static final String organization = "test";
    private static final String city = "test";
    private static final String state = "test";
    private static final String country = "DE";
    private static final long validity = 1096; // 3 years
    private static final String alias = "tomcat";
    private static final char[] keyPass = "changeit".toCharArray();

    static {
        // adds the Bouncy castle provider to java security
        Security.addProvider(new BouncyCastleProvider());
    }

    @SuppressWarnings("deprecation")
    static KeyStore generateSelfSignedX509Certificate() throws NoSuchAlgorithmException, NoSuchProviderException,
            SignatureException, InvalidKeyException, IOException, KeyStoreException, CertificateException {

        // yesterday
        Date validityBeginDate = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        // in 2 years
        Date validityEndDate = new Date(System.currentTimeMillis() + 4 * 365 * 24 * 60 * 60 * 1000);

        // GENERATE THE PUBLIC/PRIVATE RSA KEY PAIR
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
        keyPairGenerator.initialize(keysize, new SecureRandom());

        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        // GENERATE THE X509 CERTIFICATE
        X509V1CertificateGenerator certGen = new X509V1CertificateGenerator();
        X500Principal dnName = new X500Principal("CN=John Doe");

        certGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
        certGen.setSubjectDN(dnName);
        certGen.setIssuerDN(dnName); // use the same
        certGen.setNotBefore(validityBeginDate);
        certGen.setNotAfter(validityEndDate);
        certGen.setPublicKey(publicKey);
        certGen.setSignatureAlgorithm("SHA256WithRSAEncryption");

        X509Certificate cert = certGen.generate(privateKey, "BC");

        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, null);
        X509Certificate[] chain = new X509Certificate[1];
        System.out.println("chain = " + chain);
        chain[0] = cert;
        keyStore.setKeyEntry(alias, privateKey, keyPass, chain);
        //System.out.println("keyStore = " + keyStore.getCertificate(alias));

       /* // DUMP CERTIFICATE AND KEY PAIR
        System.out.println(Strings.repeat("=", 80));
        System.out.println("CERTIFICATE TO_STRING");
        System.out.println(Strings.repeat("=", 80));
        System.out.println();
        System.out.println(cert);
        System.out.println();

        System.out.println(Strings.repeat("=", 80));
        System.out.println("CERTIFICATE PEM (to store in a cert-johndoe.pem file)");
        System.out.println(Strings.repeat("=", 80));
        System.out.println();
        PEMWriter pemWriter = new PEMWriter(new PrintWriter(System.out));
        pemWriter.writeObject(cert);
        pemWriter.flush();
        System.out.println();

        System.out.println(Strings.repeat("=", 80));
        System.out.println("PRIVATE KEY PEM (to store in a priv-johndoe.pem file)");
        System.out.println(Strings.repeat("=", 80));
        System.out.println();
        pemWriter.writeObject(privateKey);
        pemWriter.flush();
        System.out.println();*/

        return keyStore;
    }

    public static void main(String[] args){
        try {
            KeyStore keyStore = generateSelfSignedX509Certificate();
            System.out.println("keyStore = " + keyStore.getCertificate(alias));
            keyStore.store(new FileOutputStream("Keys.keystore"), keyPass);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}