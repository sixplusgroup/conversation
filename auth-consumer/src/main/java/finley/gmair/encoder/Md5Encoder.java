package finley.gmair.encoder;

import finley.gmair.util.Encryption;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Md5Encoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence charSequence) {
        return Encryption.md5(charSequence.toString());
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        if (charSequence.length() == 0)
            return true;
        return this.encode(charSequence).equals(s);
    }
}
