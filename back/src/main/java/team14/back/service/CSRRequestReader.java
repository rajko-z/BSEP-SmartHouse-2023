package team14.back.service;

import org.bouncycastle.pkcs.PKCS10CertificationRequest;

public interface CSRRequestReader {

    PKCS10CertificationRequest readCSRForEmail(String email);
}
