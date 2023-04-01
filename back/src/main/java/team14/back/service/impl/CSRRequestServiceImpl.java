package team14.back.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import team14.back.converters.CSRRequestConverter;
import team14.back.dto.SimpleCSRRequestDTO;
import team14.back.repository.CSRRequestRepository;
import team14.back.service.CSRRequestService;

import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class CSRRequestServiceImpl implements CSRRequestService {

    private final CSRRequestRepository csrRequestRepository;

    @Override
    public List<SimpleCSRRequestDTO> getSimpleCSRRequestsData() {
        return csrRequestRepository.findAll()
                .stream()
                .map(CSRRequestConverter::convertToSimpleDTO)
                .sorted(Comparator.comparing(SimpleCSRRequestDTO::getTimestamp))
                .toList();
    }
}
