<#import "template.ftl" as layout>
<@layout.registrationLayout displayInfo=social.displayInfo; section>
    <#if section = "title">
        ${msg("loginTitle",(realm.displayName!''))}
    <#elseif section = "header">
        ${msg("loginTitleHtml",(realm.displayNameHtml!''))?no_esc}
    <#elseif section = "form">
        <#if realm.password>
            <form id="kc-form-login" class="${properties.kcFormClass!}" onsubmit="login.disabled = true; return true;" action="${url.loginAction}" method="post">
                <div class="${properties.kcFormGroupClass!}">
                    <div class="${properties.kcLabelWrapperClass!}">
                        <label for="email" class="${properties.kcLabelClass!}">${msg("enterEmail")}</label>
                    </div>

                    <div class="${properties.kcInputWrapperClass!}">
                        <input tabindex="1" id="email" class="${properties.kcInputClass!}" name="email"  type="text" autofocus autocomplete="on" />
                    </div>
                </div>

                <div class="${properties.kcFormGroupClass!}">
                    <div id="kc-form-options" class="${properties.kcFormOptionsClass!}">
                    </div>

                    <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                        <div class="${properties.kcFormButtonsWrapperClass!}">
                            <input tabindex="4" class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonLargeClass!}" name="sendCode" id="sendCode" type="submit" value="${msg("sendCode")}"}"/>
                        </div>
                     </div>
                </div>
                <#if realm.registrationAllowed??>
                     <div class="card-footer font-weight-light font-size-smaller">
                         <span>${msg("noAccount")} <a href="${url.registrationUrl}" class="font-weight-bold">${msg("doRegister")}</a></span>
                     </div>
                </#if>
            </form>
        </#if>
    </#if>
</@layout.registrationLayout>