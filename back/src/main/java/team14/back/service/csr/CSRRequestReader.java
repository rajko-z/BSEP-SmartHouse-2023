package team14.back.service.csr;

import org.bouncycastle.pkcs.PKCS10CertificationRequest;

import javax.servlet.http.HttpServletRequest;

public interface CSRRequestReader {

    PKCS10CertificationRequest readCSRForEmail(String email, HttpServletRequest request);
}
