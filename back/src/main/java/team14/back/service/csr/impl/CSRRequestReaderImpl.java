package team14.back.service.csr.impl;

import lombok.AllArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.springframework.stereotype.Service;
import team14.back.dto.LogDTO;
import team14.back.enumerations.LogAction;
import team14.back.exception.InternalServerException;
import team14.back.service.csr.CSRRequestReader;
import team14.back.service.log.LogService;
import team14.back.utils.HttpUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@AllArgsConstructor
public class CSRRequestReaderImpl implements CSRRequestReader {

    private static final String CLS_NAME = CSRRequestReaderImpl.class.getName();

    private final LogService logService;

    @Override
    public PKCS10CertificationRequest readCSRForEmail(String email, HttpServletRequest request) {
        String pathFile = "./src/main/resources/data/csr/" + email + ".csr";
        Path path = Paths.get(pathFile);
        File f = path.toFile();
        try (
                final ByteArrayInputStream bis =
                    new ByteArrayInputStream(FileUtils
                            .readFileToByteArray(f));
                final InputStreamReader isr = new InputStreamReader(bis, StandardCharsets.UTF_8);
                final PEMParser pem = new PEMParser(isr)
        ) {
            return (PKCS10CertificationRequest) pem.readObject();
        } catch (IOException e) {
            String errorMessage = "Error happened while reading csr file for user: " + email;
            logService.addErr(new LogDTO(LogAction.ERROR_ON_GENERATING_CERTIFICATE, CLS_NAME, errorMessage, HttpUtils.getRequestIP(request)));
            throw new InternalServerException(errorMessage);
        }
    }
}
