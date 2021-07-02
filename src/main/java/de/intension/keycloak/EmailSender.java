package de.intension.keycloak;

import java.util.HashMap;
import java.util.Map;

import org.keycloak.email.EmailException;
import org.keycloak.email.EmailTemplateProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.services.ServicesLogger;

public class EmailSender
{

    /**
     * Sends email with given code to user
     * 
     * @return false if email sending doesn't happen
     */
    public boolean sendEmail(KeycloakSession session, RealmModel realm, UserModel user, String code)
    {

        EmailTemplateProvider emailSender = session.getProvider(EmailTemplateProvider.class);
        emailSender.setRealm(realm);

        boolean result = true;
        try {
            emailSender.setUser(user);
            emailSender.send(code + " Login Code", "loginCode.ftl", createCodeLoginAttributes(code));
        } catch (EmailException e) {
            ServicesLogger.LOGGER.failedToSendEmail(e);
            result = false;
        }
        return result;
    }

    private Map<String, Object> createCodeLoginAttributes(String loginCode)
    {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("loginCode", loginCode);
        return attributes;
    }
}