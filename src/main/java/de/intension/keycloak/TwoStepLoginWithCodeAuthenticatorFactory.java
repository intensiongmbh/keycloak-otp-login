package de.intension.keycloak;

import java.util.List;

import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

public class TwoStepLoginWithCodeAuthenticatorFactory
    implements AuthenticatorFactory
{

    public static final String                                      ID                  = "two-step-login-with-code";

    private static final AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
            AuthenticationExecutionModel.Requirement.REQUIRED,
            AuthenticationExecutionModel.Requirement.ALTERNATIVE,
            AuthenticationExecutionModel.Requirement.DISABLED,
            AuthenticationExecutionModel.Requirement.CONDITIONAL
    };

    @Override
    public Authenticator create(KeycloakSession session)
    {
        return new TwoStepLoginWithCodeAuthenticator(new EmailSender(), new SecureCode());
    }

    @Override
    public String getId()
    {
        return ID;
    }

    @Override
    public String getReferenceCategory()
    {
        return "twoStepLoginWithCode";
    }

    @Override
    public boolean isConfigurable()
    {
        return false;
    }

    @Override
    public boolean isUserSetupAllowed()
    {
        return true;
    }

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices()
    {
        return REQUIREMENT_CHOICES;
    }

    @Override
    public String getDisplayType()
    {
        return "Two-step login with code";
    }

    @Override
    public String getHelpText()
    {
        return "Two-step login with code";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties()
    {
        return null;
    }

    @Override
    public void init(Config.Scope config)
    {
        // not needed for current version
    }

    @Override
    public void postInit(KeycloakSessionFactory factory)
    {
        // not needed for current version
    }

    @Override
    public void close()
    {
        // not used for current version
    }

}