package team14.back.service.impl;

import lombok.AllArgsConstructor;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCSException;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import team14.back.converters.CSRRequestConverter;
import team14.back.dto.CSRRequestData;
import team14.back.dto.RejectCSRRequestDTO;
import team14.back.dto.SimpleCSRRequestDTO;
import team14.back.exception.InternalServerException;
import team14.back.exception.NotFoundException;
import team14.back.model.CSRRequest;
import team14.back.repository.CSRRequestRepository;
import team14.back.service.CSRRequestReader;
import team14.back.service.CSRRequestService;
import team14.back.service.EmailService;
import team14.back.utils.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Service
@AllArgsConstructor
public class CSRRequestServiceImpl implements CSRRequestService {

    private final CSRRequestRepository csrRequestRepository;

    private final CSRRequestReader csrRequestReader;

    private final EmailService emailService;

    @Override
    public List<SimpleCSRRequestDTO> getSimpleCSRRequestsData() {
        return csrRequestRepository.findAll()
                .stream()
                .map(CSRRequestConverter::convertToSimpleDTO)
                .sorted(Comparator.comparing(SimpleCSRRequestDTO::getTimestamp))
                .toList();
    }

    @Override
    public CSRRequestData getCSRRequestDataForEmail(String email) {
        if (!csrRequestForEmailExist(email)) {
            throw new NotFoundException("CSR Request for email:" + email + " does not exist");
        }

        PKCS10CertificationRequest csr = csrRequestReader.readCSRForEmail(email);

        X500Name x500Name = csr.getSubject();

        String commonName = getX500Field(Constants.COMMON_NAME, x500Name);
        String organizationUnit = getX500Field(Constants.ORGANIZATION_UNIT, x500Name);
        String organizationName = getX500Field(Constants.ORGANIZATION, x500Name);
        String localeName = getX500Field(Constants.LOCALE, x500Name);
        String stateName = getX500Field(Constants.STATE, x500Name);
        String country = getX500Field(Constants.COUNTRY, x500Name);
        String publicKey = csr.getSubjectPublicKeyInfo().getPublicKeyData().toString();
        String signature = getSignatureHex(csr.getSignature());

        try {
            boolean isSignatureValid = signatureValid(csr);

            return CSRRequestData.builder()
                    .commonName(commonName)
                    .organizationUnit(organizationUnit)
                    .organizationName(organizationName)
                    .localityName(localeName)
                    .stateName(stateName)
                    .countryName(country)
                    .publicKey(publicKey)
                    .signature(signature)
                    .isValidSignature(isSignatureValid)
                    .build();

        } catch (OperatorCreationException | PKCSException ex) {
            throw new InternalServerException("Error happened while validating signature for csr request of email: " + email);
        }
    }

    @Override
    public void rejectCSRRequest(RejectCSRRequestDTO data) {
        Optional<CSRRequest> optCsr = this.csrRequestRepository.findByEmail(data.getEmail());
        if (optCsr.isEmpty()) {
            throw new NotFoundException("Can't find csr request for email: " + data.getEmail());
        }
        CSRRequest csr = optCsr.get();
        this.csrRequestRepository.delete(csr);
        deleteCSRFile(data.getEmail());
        emailService.sendCSRRejectionEmail(data.getEmail(), data.getReason());
    }

    @Override
    public boolean csrRequestForEmailExist(String email) {
        return this.csrRequestRepository.findByEmail(email).isPresent();
    }

    protected void deleteCSRFile(String email) {
        File fileToDelete = FileUtils.getFile("src/main/java/resources/" + email + ".csr");
        FileUtils.deleteQuietly(fileToDelete);
    }

    protected static boolean signatureValid(PKCS10CertificationRequest csr)
            throws OperatorCreationException, PKCSException {
        JcaContentVerifierProviderBuilder builder =
                new JcaContentVerifierProviderBuilder().setProvider(new BouncyCastleProvider());
        return csr.isSignatureValid(builder.build(csr.getSubjectPublicKeyInfo()));
    }

    protected String getX500Field(String asn1ObjectIdentifier, X500Name x500Name) {
        RDN[] rdnArray = x500Name.getRDNs(new ASN1ObjectIdentifier(asn1ObjectIdentifier));

        String retVal = null;
        for (RDN item : rdnArray) {
            retVal = item.getFirst().getValue().toString();
        }
        return retVal;
    }

    protected String getSignatureHex(byte[] bytes) {
        return Hex.encodeHexString(bytes);
    }

}
