package de.intension.keycloak;

import javax.ws.rs.core.MultivaluedMap;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.authenticators.browser.AbstractUsernameFormAuthenticator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

/**
 * Creates alternative login flow
 * After user enters his email he receives an email containing a code which he
 * has to enter in a follow up view.
 * If all conditions for the code validation are met he gets logged in
 * else he gets redirected to the email input view.
 */

public class TwoStepLoginWithCodeAuthenticator extends AbstractUsernameFormAuthenticator
    implements Authenticator
{

    private static final String FTL_ENTER_EMAIL      = "enter-email.ftl";
    private static final String FTL_ENTER_CODE       = "enter-code.ftl";
    private static final String AUTH_NOTE_USER_EMAIL = "user-email";
    private static final String AUTH_NOTE_EMAIL_CODE = "email-code";
    private static final String AUTH_NOTE_TIMESTAMP  = "timestamp";

    private EmailSender         emailSender;
    private SecureCode          secureCode;

    public TwoStepLoginWithCodeAuthenticator(EmailSender emailSender, SecureCode secureCode)
    {
        this.emailSender = emailSender;
        this.secureCode = secureCode;
    }

    @Override
    public void action(AuthenticationFlowContext context)
    {

        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
        context.challenge(context.form().createForm(FTL_ENTER_CODE));

        String email = formData.getFirst("email");
        String codeInput = formData.getFirst("codeInput");

        if (email != null) {

            context.getAuthenticationSession().setAuthNote(AUTH_NOTE_USER_EMAIL, email);
            UserModel user = getUser(context);

            if (user == null || !user.isEnabled()) {
                context.failure(AuthenticationFlowError.INVALID_USER);
            }
            else {

                String code = secureCode.generateCode(6);
                context.getAuthenticationSession().setAuthNote(AUTH_NOTE_EMAIL_CODE, code);
                context.getAuthenticationSession().setAuthNote(AUTH_NOTE_TIMESTAMP,
                                                               Long.toString(System.currentTimeMillis()));

                emailSender.sendEmail(context.getSession(), context.getRealm(),
                                      getUser(context), secureCode.makeCodeUserFriendly(code));
            }

        }
        else if (codeInput != null && context.getAuthenticationSession().getAuthNote(AUTH_NOTE_EMAIL_CODE) != null) {

            if (secureCode.isValid(codeInput, context.getAuthenticationSession().getAuthNote(AUTH_NOTE_EMAIL_CODE),
                                   context.getAuthenticationSession().getAuthNote(AUTH_NOTE_TIMESTAMP), 20, 2)) {
                context.setUser(getUser(context));
                context.success();
            }
            else {
                context.failureChallenge(AuthenticationFlowError.INVALID_CREDENTIALS,
                                         context.form().createForm(FTL_ENTER_EMAIL));
            }
        }
        else {
            context.challenge(context.form().createForm(FTL_ENTER_EMAIL));
        }
    }

    private UserModel getUser(AuthenticationFlowContext context)
    {
        KeycloakSession session = context.getSession();
        return session.users().getUserByEmail(context.getAuthenticationSession().getAuthNote(AUTH_NOTE_USER_EMAIL), context.getRealm());
    }

    @Override
    public void authenticate(AuthenticationFlowContext context)
    {
        context.challenge(context.form().createForm(FTL_ENTER_EMAIL));
    }

    @Override
    public boolean requiresUser()
    {
        return false;
    }

    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user)
    {
        return true;
    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user)
    {
        // not needed for current version
    }

    @Override
    public void close()
    {
        // not used for current version
    }

}