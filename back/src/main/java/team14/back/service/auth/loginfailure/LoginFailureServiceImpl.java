package team14.back.service.auth.loginfailure;

import lombok.AllArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import team14.back.model.LoginFailure;
import team14.back.repository.LoginFailureRepository;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class LoginFailureServiceImpl implements LoginFailureService {

    private final LoginFailureRepository loginFailureRepository;

    @Override
    public int findNumberOfLoginFailuresInLast10Minutes(String email) {
        return loginFailureRepository
                .findNumberOfLoginFailuresInLast10Minutes(email, LocalDateTime.now().minusMinutes(10)).size();
    }

    @Override
    public void addNewLoginFailureForUser(String email) {
        this.loginFailureRepository.insert(new LoginFailure(email, LocalDateTime.now()));
    }
}
