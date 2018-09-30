package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import models.Addon;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import play.Application;
import play.Logger;
import play.db.Database;
import play.db.Databases;
import play.db.evolutions.Evolutions;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.NO_CONTENT;
import static play.test.Helpers.OK;
import static play.test.Helpers.route;

public class AddonControllerTest {

    @Mock
    Addon addon;

    private Application application;
    private Database database;

    @Before
    public void init() {
        application = Helpers.fakeApplication(Helpers.inMemoryDatabase());
        Helpers.start(application);
        database = Databases.inMemory(
                "testdb",
                ImmutableMap.of(
                        "MODE","MySQL"
                ),
                ImmutableMap.of(
                        "logStatements", true
                )
        );
        Evolutions.applyEvolutions(database);
    }

    @After
    public void cleanUp() {
        Evolutions.cleanupEvolutions(database);
        database.shutdown();
        Helpers.stop(application);
    }

    @Test
    public void testGetZeroAddons() {
        for(Addon addon : Addon.find.all()){
            addon.delete();
        }
        Logger.info("Testing getAllAddons without any addons...");
        Result result = new AddonController().getAllAddons();
        Logger.info("...Test for No Content result");
        assertEquals(NO_CONTENT, result.status());
    }

    @Test
    public void testCreateAddon() {
        JsonNode jsonNode = Json.toJson(addon);
        Http.RequestBuilder request = new Http.RequestBuilder().method("POST")
                .bodyJson(jsonNode)
                .uri(controllers.routes.AddonController.createAddon().url());
        Result result = route(application, request);
        assertEquals(OK, result.status());
    }

    @Test
    public void testCreateExistingAddon() {
        JsonNode jsonNode = Json.toJson(addon);
        Http.RequestBuilder request = new Http.RequestBuilder().method("POST")
                .bodyJson(jsonNode)
                .uri(controllers.routes.AddonController.createAddon().url());
        Result result = route(application, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void testGetAllAddons() {
        Logger.info("Testing getAllAddons...");
        Result result = new AddonController().getAllAddons();
        Logger.info("...Test for OK result");
        assertEquals(OK, result.status());
        assertTrue(ControllerTestHelper.testContentType("application/json", result));
    }

    @Test
    public void testGetAddon() {

    }

    @Test
    public void testDeleteAddon() {

    }
}
