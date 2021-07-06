# What is keycloak-code-login?

It is an alternative authentication method, sending the user a different login code to their email for each login.

It grants improved security, by removing the security issues of user created passwords.

## Keycloak support

Developed and tested in Keycloak version 12.0.1.
Tested for 13.0.1.

## Testing

For the LoginFlowTest testing the Maildev container must be running.

## How to configure in Keycloak

 1. Once  `keycloak-otp-login` is deployed on your server, (re-)start said server.
 
    You can use `./restart.sh` in the root directory of the project.
 
 2. Now open the web admin console of keycloak, under Configure go to `Authentication`. In the `Flows` tab create a copy of the `Browser` flow.
   ![image](images/create-copy-of-browser-flow.png)
   
 3. Delete `Username Password Form` and `Browser - Conditional OTP` in your new flows Forms.
   ![image](images/executions-to-delete.png)
   
 4. Add a new execution to `*your flow name* Forms` using the `keycloak-otp-login` provider and set its requirement to `REQUIRED`.
   ![image](images/add-execution.png)
   
 5. Switch to the `Bindings` tab and change your Browser Flow to your new flow and save the settings.
   ![image](images/change-browser-flow.png)
   
 6. Now open the `Realm Settings`, go to the `Themes` tab and select the `Otp-Login` for the Login- aswell as the Email Theme.
   ![image](images/change-themes.png)

 7. Enable `Internationalization` and remove all the `Supported Locales` except `de` and `en` (and any languages you added) and set the `Default Locale` to your preference and save.
   ![image](images/choose-locales.png)
   
## Credit

 Inspired by [Magic Link authenticator for Keycloak](https://github.com/stianst/keycloak-experimental/tree/master/magic-link)
  