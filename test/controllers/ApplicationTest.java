package controllers;

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
        assertEquals(OK, result.status());
        if (result.contentType().isPresent() && result.charset().isPresent()) {
            assertEquals("application/json", result.contentType().get());
            assertEquals("UTF-8", result.charset().get());
        } else {
            Logger.error("...Expected type of application/json and charset of utf-8 but did not get one or both");
        }
        assertTrue(contentAsString(result).contains("Welcome to the beginnings of the Dead by Daylight Build Maker"));
    }


}
