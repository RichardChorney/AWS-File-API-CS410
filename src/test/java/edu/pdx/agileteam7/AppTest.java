package edu.pdx.agileteam7;

//import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void invalidCredentialsExitsInError() {
        App.AWS_ACCESS_KEYS = "blah";
        App.AWS_SECRET_KEYS = "blah";
        assertThat(App.validateCredentials(), equalTo(false));
    }
}
