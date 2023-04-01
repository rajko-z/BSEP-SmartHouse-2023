package team14.back.service;

import team14.back.dto.SimpleCSRRequestDTO;

import java.util.List;

public interface CSRRequestService {

    List<SimpleCSRRequestDTO> getSimpleCSRRequestsData();
}
