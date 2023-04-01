package team14.back.service;

import team14.back.dto.CSRRequestData;
import team14.back.dto.RejectCSRRequestDTO;
import team14.back.dto.SimpleCSRRequestDTO;

import java.util.List;

public interface CSRRequestService {

    List<SimpleCSRRequestDTO> getSimpleCSRRequestsData();

    CSRRequestData getCSRRequestDataForEmail(String email);

    void rejectCSRRequest(RejectCSRRequestDTO data);

    boolean csrRequestForEmailExist(String email);
}
