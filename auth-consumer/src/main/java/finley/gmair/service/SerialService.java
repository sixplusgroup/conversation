package finley.gmair.service;

import finley.gmair.model.auth.VerificationCode;

public interface SerialService {
    VerificationCode generate(String phone);

    VerificationCode fetch(String phone);
}
