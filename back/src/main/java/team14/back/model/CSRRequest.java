package team14.back.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import java.time.LocalDateTime;

@Document("csr_requests")
public class CSRRequest {

    @Id
    private String email;
    private String firstName;
    private String lastName;
    private LocalDateTime timestamp;

    public CSRRequest() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public CSRRequest(String email, String firstName, String lastName, LocalDateTime timestamp) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.timestamp = timestamp;
    }
}
