package team14.back.converters;

import team14.back.dto.SimpleCSRRequestDTO;
import team14.back.model.CSRRequest;

public class CSRRequestConverter {

    private CSRRequestConverter() {}

    public static SimpleCSRRequestDTO convertToSimpleDTO(CSRRequest csrRequest) {
        return SimpleCSRRequestDTO.builder()
                .id(csrRequest.getId())
                .firstName(csrRequest.getFirstName())
                .lastName(csrRequest.getLastName())
                .email(csrRequest.getEmail())
                .filePath(csrRequest.getFilePath())
                .timestamp(csrRequest.getTimestamp())
                .build();
    }
}
