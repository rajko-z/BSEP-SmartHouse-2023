package team14.back.enumerations;

public enum LogAction {
    LOG_IN_SUCCESS,
    INVALID_CREDENTIALS,
    INVALID_2FA,
    LOG_OUT,
    GET_ALL_USERS,
    GET_ALL_CSR_REQUESTS,
    GET_ALL_CERTIFICATES,
    REVOKE_CERTIFICATE,
    REJECT_CERTIFICATE,
    ISSUE_CERTIFICATE,
    SENDING_BLOCKING_USER_EMAIL,
    INVALID_CERTIFICATE,
    GENERATE_CERTIFICATE,
    ERROR_ON_GENERATING_CERTIFICATE,
    STORING_CERTIFICATE,
    ERROR_ON_STORING_CERTIFICATE,
    CREATING_USER,
    ERROR_ON_CREATING_USER,
    SENDING_CERTIFICATE_TO_USER,
    ERROR_ON_SENDING_CERTIFICATE_TO_USER,
    GET_ALL_REVOKED_CERTIFICATES,
    VERIFY_CERTIFICATE,
    GET_ALL_DEVICES,
    UNKNOWN_USER,
    CREATING_NEW_CSR_REQUEST,
    BLOCKING_USER,
    UNBLOCKING_USER,
    CHANGING_PASSWORD,
    ERROR_ON_CHANGING_PASSWORD,
    SAVING_FACILITIES,
    GET_ALL_FACILITIES,
    CHANGING_USER_ROLE,
    DELETING_USER,
    UNKNOWN_DEVICE_TYPE,
    UNKNOWN_ALARM_STATE
}
