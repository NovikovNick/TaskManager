package com.metalheart.service;

public interface I18nService {

    String translate(KEY key);

    enum KEY {

        EMAIL_RESET_PASSWORD_SUBJECT("email.resetpassword.subject"),
        EMAIL_RESET_PASSWORD_TITLE("email.resetpassword.title"),
        EMAIL_RESET_PASSWORD_CONTENT("email.resetpassword.content"),
        EMAIL_RESET_PASSWORD_BTN("email.resetpassword.btn"),
        EMAIL_RESET_PASSWORD_FOOTER("email.resetpassword.footer"),

        EMAIL_REGISTRATION_SUBJECT("email.registration.subject"),
        EMAIL_REGISTRATION_TITLE("email.registration.title"),
        EMAIL_REGISTRATION_CONTENT("email.registration.content"),
        EMAIL_REGISTRATION_BTN("email.registration.btn"),
        EMAIL_REGISTRATION_FOOTER("email.registration.footer"),

        EMAIL_FOOTER_CONTACT("email.footer.contact");

        private final String key;

        KEY(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }
}
