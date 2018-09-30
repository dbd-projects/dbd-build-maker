package controllers;

import org.junit.Assert;
import org.junit.Test;
import play.Logger;
import play.mvc.Result;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static play.test.Helpers.OK;
import static play.test.Helpers.contentAsString;


/**
 * Simple (JUnit) tests that can call all parts of a play app.
 * If you are interested in mocking a whole application, see the wiki for more details.
 */
public class ApplicationTest {

    /**
     * Test the index call in the Application class
     */
    @Test
    public void testIndex() {
        Logger.info("Testing index...");
        Result result = new Application().index();
        Logger.info("...Test for OK result");
        assertEquals(OK, result.status());
        Assert.assertTrue(ControllerTestHelper.testContentType("application/json", result));
        Logger.info("...Test for expected string output");
        assertTrue(contentAsString(result).contains("Welcome to the beginnings of the Dead by Daylight Build Maker"));
    }


}
