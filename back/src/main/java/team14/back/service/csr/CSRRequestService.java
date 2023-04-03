package team14.back.service.csr;

import team14.back.dto.csr.CSRRequestData;
import team14.back.dto.csr.RejectCSRRequestDTO;
import team14.back.dto.csr.SimpleCSRRequestDTO;

import java.util.List;

public interface CSRRequestService {

    List<SimpleCSRRequestDTO> getSimpleCSRRequestsData();

    CSRRequestData getCSRRequestDataForEmail(String email);

    void rejectCSRRequest(RejectCSRRequestDTO data);

    boolean csrRequestForEmailExist(String email);

    void deleteCSRRequest(String email);
}
