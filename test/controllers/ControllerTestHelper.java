package controllers;

import play.Logger;
import play.mvc.Result;

class ControllerTestHelper {

    static boolean testContentType(final String expected, final Result result) {
        if(result.contentType().isPresent()) {
            Logger.info("...Test for application/json type");
            return expected.equals(result.contentType().get());
        }else {
            Logger.error("...Expected content type of application/json");
            return false;
        }
    }
}
