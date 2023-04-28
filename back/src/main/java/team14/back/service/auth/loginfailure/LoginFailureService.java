package team14.back.service.auth.loginfailure;

public interface LoginFailureService {
    int findNumberOfLoginFailuresInLast10Minutes(String email);

    void addNewLoginFailureForUser(String email);
}
