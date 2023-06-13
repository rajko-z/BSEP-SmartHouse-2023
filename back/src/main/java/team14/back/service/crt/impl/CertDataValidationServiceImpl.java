package team14.back.service.crt.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import team14.back.dto.LogDTO;
import team14.back.dto.crt.NewCertificateDTO;
import team14.back.dto.crt.SubjectDataDTO;
import team14.back.enumerations.LogAction;
import team14.back.exception.BadRequestException;
import team14.back.service.csr.CSRRequestService;
import team14.back.service.crt.CertDataValidationService;
import team14.back.service.log.LogService;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class CertDataValidationServiceImpl implements CertDataValidationService {

    private static final String CLS_NAME = CertDataValidationServiceImpl.class.getName();

    private static final int MAX_FIELD_SIZE = 64;

    private static final int MIN_MOUNTS_VALIDITY = 3;

    private static final int MAX_MOUNTS_VALIDITY = 13;

    private final CSRRequestService csrRequestService;

    private final LogService logService;

    @Override
    public void validateNewCertificateData(NewCertificateDTO certificate) {
        validateEmail(certificate.getSubjectData().getEmail());
        validateSubjectData(certificate.getSubjectData());
        validateValidityPeriod(certificate.getValidUntil());
    }

    private void validateEmail(String email) {
        if (!csrRequestService.csrRequestForEmailExist(email)) {
            String errorMessage = "Can't issue certificate because there is no csr request for email: " + email;
            logService.addErr(new LogDTO(LogAction.INVALID_CERTIFICATE, CLS_NAME, errorMessage));
            throw new BadRequestException(errorMessage);
        }
    }

    private void validateSubjectData(SubjectDataDTO subjectDataDTO) {
        validateCommonName(subjectDataDTO.getCommonName());
        validateOrganizationUnit(subjectDataDTO.getOrganizationUnit());
        validateOrganizationName(subjectDataDTO.getOrganizationName());
        validateLocalityName(subjectDataDTO.getLocalityName());
        validateStateName(subjectDataDTO.getStateName());
        validateCountry(subjectDataDTO.getCountryName());
    }

    private void validateCommonName(String cn) {
        if (cn == null || cn.isBlank()) return;
        validateFieldLength(cn);
        String regex = "^[\\w-]+(?:\\.[\\w-]+)*$";
        if (!Pattern.compile(regex).matcher(cn).matches()) {
            String errorMessage = "Common name: " + cn + " invalid format.";
            logService.addErr(new LogDTO(LogAction.INVALID_CERTIFICATE, CLS_NAME, errorMessage));
            throw new BadRequestException(errorMessage);
        }
    }
    private void validateOrganizationUnit(String ou) {
        if (ou == null || ou.isBlank()) return;
        validateFieldLength(ou);
        String regex = "^[\\w-]+(?:\\s+[\\w-]+)*$";
        if (!Pattern.compile(regex).matcher(ou).matches()) {
            String errorMessage = "Organization unit: " + ou + " invalid format.";
            logService.addErr(new LogDTO(LogAction.INVALID_CERTIFICATE, CLS_NAME, errorMessage));
            throw new BadRequestException(errorMessage);
        }
    }
    private void validateOrganizationName(String o) {
        if (o == null || o.isBlank()) return;
        validateFieldLength(o);
        String regex = "^[\\w\\s]+\\.?$";
        if (!Pattern.compile(regex).matcher(o).matches()) {
            String errorMessage = "Organization name: " + o + " invalid format.";
            logService.addErr(new LogDTO(LogAction.INVALID_CERTIFICATE, CLS_NAME, errorMessage));
            throw new BadRequestException(errorMessage);
        }
    }
    private void validateLocalityName(String l) {
        if (l == null || l.isBlank()) return;
        validateFieldLength(l);
        String regex = "^[\\w\\s]+$";
        if (!Pattern.compile(regex).matcher(l).matches()) {
            String errorMessage = "Locality name: " + l + " invalid format.";
            logService.addErr(new LogDTO(LogAction.INVALID_CERTIFICATE, CLS_NAME, errorMessage));
            throw new BadRequestException(errorMessage);
        }
    }
    private void validateStateName(String st) {
        if (st == null || st.isBlank()) return;
        validateFieldLength(st);
        String regex = "^[\\w\\s]+$";
        if (!Pattern.compile(regex).matcher(st).matches()) {
            String errorMessage = "State name: " + st + " invalid format.";
            logService.addErr(new LogDTO(LogAction.INVALID_CERTIFICATE, CLS_NAME, errorMessage));
            throw new BadRequestException(errorMessage);
        }
    }
    private void validateCountry(String c) {
        if (c == null || c.isBlank()) return;
        validateFieldLength(c);
        String regex = "^[A-Z]{2}$";
        if (!Pattern.compile(regex).matcher(c).matches()) {
            String errorMessage = "Country: " + c + " invalid format.";
            logService.addErr(new LogDTO(LogAction.INVALID_CERTIFICATE, CLS_NAME, errorMessage));
            throw new BadRequestException(errorMessage);
        }
    }

    private void validateValidityPeriod(LocalDateTime validityPeriod) {
        if (validityPeriod.isBefore(LocalDateTime.now().plusMonths(MIN_MOUNTS_VALIDITY)) ||
                validityPeriod.isAfter(LocalDateTime.now().plusMonths(MAX_MOUNTS_VALIDITY))) {
            String errorMessage = "Invalid validity time for certificate. Minimum is 3 months, and maximum 13";
            logService.addErr(new LogDTO(LogAction.INVALID_CERTIFICATE, CLS_NAME, errorMessage));
            throw new BadRequestException(errorMessage);
        }
    }

    private void validateFieldLength(String field) {
        if (field != null && !field.isBlank() && field.length() > MAX_FIELD_SIZE) {
            String errorMessage = "Field: " + field + " is longer than 64 chars";
            logService.addErr(new LogDTO(LogAction.INVALID_CERTIFICATE, CLS_NAME, errorMessage));
            throw new BadRequestException(errorMessage);
        }
    }
}
