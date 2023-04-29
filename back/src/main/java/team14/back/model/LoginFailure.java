package team14.back.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("login_failures")
public class LoginFailure {
    private String email;
    private LocalDateTime timestamp;

    public LoginFailure() {}

    public LoginFailure(String email, LocalDateTime timestamp) {
        this.email = email;
        this.timestamp = timestamp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
