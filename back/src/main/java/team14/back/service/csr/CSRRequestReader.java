package team14.back.service.csr;

import org.bouncycastle.pkcs.PKCS10CertificationRequest;

public interface CSRRequestReader {

    PKCS10CertificationRequest readCSRForEmail(String email);
}
