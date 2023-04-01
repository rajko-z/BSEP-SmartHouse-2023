package team14.back.service.impl;

import lombok.AllArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import team14.back.exception.InternalServerException;
import team14.back.service.CSRRequestReader;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Service
public class CSRRequestReaderImpl implements CSRRequestReader {

    @Override
    public PKCS10CertificationRequest readCSRForEmail(String email) {
        try (
            final ByteArrayInputStream bis =
                    new ByteArrayInputStream(FileUtils
                            .readFileToByteArray(ResourceUtils.getFile("classpath:" + email + ".csr")));
            final InputStreamReader isr = new InputStreamReader(bis, StandardCharsets.UTF_8);
            final PEMParser pem = new PEMParser(isr)
        ) {
            return (PKCS10CertificationRequest) pem.readObject();
        } catch (IOException e) {
            throw new InternalServerException("Error happened while reading csr file for user: " + email);
        }
    }
}
