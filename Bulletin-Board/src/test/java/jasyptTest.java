import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class jasyptTest {
    @Test
    public void encrypt_decrypt_test(){
        // given
        String plainText="hello";
        StandardPBEStringEncryptor jasypt=new StandardPBEStringEncryptor();
        jasypt.setPassword("password");
        jasypt.setAlgorithm("PBEWithMD5AndDES");

        // when
        String encryptedText=jasypt.encrypt(plainText);
        String decryptedText=jasypt.decrypt(encryptedText);

        // then
        assertThat(plainText).isEqualTo(decryptedText);

    }
}
