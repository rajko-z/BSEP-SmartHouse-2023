package team14.back.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import team14.back.dto.NewCertificateDTO;
import team14.back.dto.SubjectDataDTO;
import team14.back.exception.BadRequestException;
import team14.back.service.CSRRequestService;
import team14.back.service.CertDataValidationService;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class CertDataValidationServiceImpl implements CertDataValidationService {

    private static final int MAX_FIELD_SIZE = 64;

    private static final int MIN_MOUNTS_VALIDITY = 3;

    private static final int MAX_MOUNTS_VALIDITY = 13;

    private final CSRRequestService csrRequestService;

    @Override
    public void validateNewCertificateData(NewCertificateDTO certificate) {
        validateEmail(certificate.getSubjectData().getEmail());
        validateSubjectData(certificate.getSubjectData());
        validateValidityPeriod(certificate.getValidUntil());
    }

    private void validateEmail(String email) {
        if (!csrRequestService.csrRequestForEmailExist(email)) {
            throw new BadRequestException("Can't issue certificate because there is no csr request for email: " + email);
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
            throw new BadRequestException("Common name: " + cn + " invalid format.");
        }
    }
    private void validateOrganizationUnit(String ou) {
        if (ou == null || ou.isBlank()) return;
        validateFieldLength(ou);
        String regex = "^[\\w-]+(?:\\s+[\\w-]+)*$";
        if (!Pattern.compile(regex).matcher(ou).matches()) {
            throw new BadRequestException("Organization unit: " + ou + " invalid format.");
        }
    }
    private void validateOrganizationName(String o) {
        if (o == null || o.isBlank()) return;
        validateFieldLength(o);
        String regex = "^[\\w\\s]+\\.?$";
        if (!Pattern.compile(regex).matcher(o).matches()) {
            throw new BadRequestException("Organization name: " + o + " invalid format.");
        }
    }
    private void validateLocalityName(String l) {
        if (l == null || l.isBlank()) return;
        validateFieldLength(l);
        String regex = "^[\\w\\s]+$";
        if (!Pattern.compile(regex).matcher(l).matches()) {
            throw new BadRequestException("Locality name: " + l + " invalid format.");
        }
    }
    private void validateStateName(String st) {
        if (st == null || st.isBlank()) return;
        validateFieldLength(st);
        String regex = "^[\\w\\s]+$";
        if (!Pattern.compile(regex).matcher(st).matches()) {
            throw new BadRequestException("State name: " + st + " invalid format.");
        }
    }
    private void validateCountry(String c) {
        if (c == null || c.isBlank()) return;
        validateFieldLength(c);
        String regex = "^[A-Z]{2}$";
        if (!Pattern.compile(regex).matcher(c).matches()) {
            throw new BadRequestException("Country: " + c + " invalid format.");
        }
    }

    private void validateValidityPeriod(LocalDateTime validityPeriod) {
        if (validityPeriod.isBefore(LocalDateTime.now().plusMonths(MIN_MOUNTS_VALIDITY)) ||
                validityPeriod.isAfter(LocalDateTime.now().plusMonths(MAX_MOUNTS_VALIDITY))) {
            throw new BadRequestException("Invalid validity time for certificate. Minimum is 3 months, and maximum 13");
        }
    }

    private void validateFieldLength(String field) {
        if (field != null && !field.isBlank() && field.length() > MAX_FIELD_SIZE) {
            throw new BadRequestException("Field: " + field + " is longer than 64 chars");
        }
    }
}
