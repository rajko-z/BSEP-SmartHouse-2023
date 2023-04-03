package team14.back.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team14.back.dto.csr.CSRRequestData;
import team14.back.dto.csr.RejectCSRRequestDTO;
import team14.back.dto.csr.SimpleCSRRequestDTO;
import team14.back.dto.TextResponse;
import team14.back.service.csr.CSRRequestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/csrrequests")
@AllArgsConstructor
public class CSRRequestController {

    private final CSRRequestService csrRequestService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public List<SimpleCSRRequestDTO> getAllSimpleCSRRequestsData() {
        return csrRequestService.getSimpleCSRRequestsData();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{email}")
    public ResponseEntity<CSRRequestData> getCSRRequestDataForEmail(@PathVariable String email) {
        CSRRequestData request = csrRequestService.getCSRRequestDataForEmail(email);
        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/reject")
    public ResponseEntity<TextResponse> rejectCSRRequest(@RequestBody @Valid RejectCSRRequestDTO rejectRequest) {
        csrRequestService.rejectCSRRequest(rejectRequest);
        return new ResponseEntity<>(new TextResponse("Success"), HttpStatus.OK);
    }

}
