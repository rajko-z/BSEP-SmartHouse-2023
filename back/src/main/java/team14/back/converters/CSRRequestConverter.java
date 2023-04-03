package team14.back.converters;

import team14.back.dto.csr.SimpleCSRRequestDTO;
import team14.back.model.CSRRequest;

public class CSRRequestConverter {

    private CSRRequestConverter() {}

    public static SimpleCSRRequestDTO convertToSimpleDTO(CSRRequest csrRequest) {
        return SimpleCSRRequestDTO.builder()
                .email(csrRequest.getEmail())
                .firstName(csrRequest.getFirstName())
                .lastName(csrRequest.getLastName())
                .timestamp(csrRequest.getTimestamp())
                .build();
    }
}
