package de.intension.keycloak;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class SecureCodeTest
{

    SecureCode code = new SecureCode();

    /**
     * GIVEN: a generated code
     * WHEN: a new code is generated with the same length
     * THEN: both codes are different
     */
    @Test
    void should_create_new_code_each_time()
    {
        String firstCode = code.generateCode(25);

        String secondCode = code.generateCode(25);

        assertThat(firstCode, not(equalTo(secondCode)));
    }

    /**
     * GIVEN: a code of length 5
     * WHEN: it's make user friendly
     * THEN: both code are same
     */
    @Test
    void should_not_split_code_of_length_under_5()
    {
        final String generatedCode = "njkds";

        String userFriendlyCode = code.makeCodeUserFriendly(generatedCode);

        assertThat(userFriendlyCode, is(equalTo(generatedCode)));
    }

    /**
     * GIVEN: a code of length 12
     * WHEN: it's make user friendly
     * THEN: the code has been split into 3 dash-separated parts
     */
    @Test
    void should_split_code_of_length_divisible_by_4_in_4s()
    {
        final String generatedCode = "njkdshgjfgtd";

        String userFriendlyCode = code.makeCodeUserFriendly(generatedCode);

        assertThat(userFriendlyCode, is(equalTo("njkd-shgj-fgtd")));
    }

    /**
     * GIVEN: a code of length 9
     * WHEN: it's make user friendly
     * THEN: the code has been split into 3 dash-separated parts
     */
    @Test
    void should_split_code_of_length_divisible_by_3_in_3s()
    {
        final String generatedCode = "njkshgfgt";

        String userFriendlyCode = code.makeCodeUserFriendly(generatedCode);

        assertThat(userFriendlyCode, is(equalTo("njk-shg-fgt")));
    }

    /**
     * GIVEN: a code of length 10
     * WHEN: it's make user friendly
     * THEN: the code has been split into 3 dash-separated parts
     */
    @Test
    void should_split_code_of_length_not_divisible_by_3_or_4_in_4s()
    {
        final String generatedCode = "njkdshgjfg";

        String userFriendlyCode = code.makeCodeUserFriendly(generatedCode);

        assertThat(userFriendlyCode, is(equalTo("njkd-shgj-fg")));
    }

    /**
     * GIVEN: a code of length 21
     * WHEN: it's make user friendly
     * THEN: the code has been split into 6 dash-separated parts
     */
    @Test
    void should_split_code_of_length_higher_20_in_4s()
    {
        final String generatedCode = "fsfgkslroic30g5n1apv1";

        String userFriendlyCode = code.makeCodeUserFriendly(generatedCode);

        assertThat(userFriendlyCode, is(equalTo("fsfg-kslr-oic3-0g5n-1apv-1")));
    }

    /**
     * GIVEN: correct code to login with valid time stamp
     * WHEN: checked for validity
     * THEN: returns true
     */
    @Test
    void should_validate_code_and_timestamp()
    {
        final String givenCode   = "qwe-rty";
        final String correctCode = "qwerty";
        final String timeStamp   = Long.toString(System.currentTimeMillis() - 5000);

        boolean returns = code.isValid(givenCode, correctCode, timeStamp, 5, 2);

        assertThat(returns, is(true));
    }
    
    /**
     * GIVEN: correct code to login with expired time stamp
     * WHEN: checked for validity
     * THEN: returns false
     */
    @Test
    void should_not_validate_due_to_timeout()
    {
        final String givenCode   = "qwe-rty";
        final String correctCode = "qwerty";
        final String timeStamp   = Long.toString(System.currentTimeMillis() - 500000);

        boolean returns = code.isValid(givenCode, correctCode, timeStamp, 5, 2);

        assertThat(returns, is(false));
    }
    
    /**
     * GIVEN: correct code to login with too recent time stamp
     * WHEN: checked for validity
     * THEN: returns false
     */
    @Test
    void should_not_validate_due_to_activation_delay()
    {
        final String givenCode   = "qwe-rty";
        final String correctCode = "qwerty";
        final String timeStamp   = Long.toString(System.currentTimeMillis());

        boolean returns = code.isValid(givenCode, correctCode, timeStamp, 5, 2);

        assertThat(returns, is(false));
    }
    
    /**
     * GIVEN: correct code to login with valid time stamp
     * WHEN: checked for validity
     * THEN: returns false
     */
    @Test
    void should_validate_due_to_wrong_code()
    {
        final String givenCode   = "osm-btw";
        final String correctCode = "qwerty";
        final String timeStamp   = Long.toString(System.currentTimeMillis() - 5000);

        boolean returns = code.isValid(givenCode, correctCode, timeStamp, 5, 2);

        assertThat(returns, is(false));
    }

}
