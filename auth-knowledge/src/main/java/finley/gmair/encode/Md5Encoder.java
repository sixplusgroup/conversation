package finley.gmair.encode;

import com.google.common.annotations.Beta;
import finley.gmair.util.Encryption;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Md5Encoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence charSequence) {
        return Encryption.md5(charSequence.toString());
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return this.encode(charSequence).equals(s);
    }
}
