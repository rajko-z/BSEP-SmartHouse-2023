package team14.back.dto.csr;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class CSRRequestDTO {

    @Email
    @NotBlank
    @Length(max = 256)
    private String email;

    @NotBlank
    @Length(max = 256)
    private String firstName;

    @NotBlank
    @Length(max = 256)
    private String lastName;

    public CSRRequestDTO(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public CSRRequestDTO() {
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
}
