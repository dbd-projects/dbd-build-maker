package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import models.Addon;
import models.CharacterType;
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
    public void testGetAllAddons() {
        createAddon().save();
        Logger.info("Testing getAllAddons...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .uri(controllers.routes.AddonController.getAllAddons().url());
        Result result = route(application, request);
        Logger.info("...Test for OK result");
        assertEquals(OK, result.status());
        assertTrue(ControllerTestHelper.testContentType("application/json", result));
    }

    @Test
    public void testGetAllAddonsWithNone() {
        for(Addon addon: Addon.find.all()){
            addon.delete();
        }
        Logger.info("Testing getAllAddons with none...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .uri(controllers.routes.AddonController.getAllAddons().url());
        Result result = route(application, request);
        Logger.info("...Test for NO CONTENT result");
        assertEquals(NO_CONTENT, result.status());
    }

    @Test
    public void testGetAllAddonsWithType() {
        createAddon().save();
        String json = "type:"+CharacterType.KILLER;
        JsonNode jsonNode = Json.toJson(json);
        Logger.info("Testing getAllAddons with type...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .bodyJson(jsonNode)
                .uri(controllers.routes.AddonController.getAllAddons().url());
        Result result = route(application, request);
        Logger.info("...Test for OK result");
        assertEquals(OK, result.status());
        assertTrue(ControllerTestHelper.testContentType("application/json", result));
    }

    @Test
    public void testCreateAddon() {
        JsonNode jsonNode = Json.toJson(createAddon());
        Http.RequestBuilder request = new Http.RequestBuilder().method("POST")
                .bodyJson(jsonNode)
                .uri(controllers.routes.AddonController.createAddon().url());
        Result result = route(application, request);
        assertEquals(OK, result.status());
    }

    @Test
    public void testCreateExistingAddon() {
        createAddon().save();
        JsonNode jsonNode = Json.toJson(createAddon());
        Http.RequestBuilder request = new Http.RequestBuilder().method("POST")
                .bodyJson(jsonNode)
                .uri(controllers.routes.AddonController.createAddon().url());
        Result result = route(application, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void testGetAddon() {
        Addon test = createAddon();
        test.setId(100);
        test.save();
        Logger.info("Testing getAddon...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .uri(controllers.routes.AddonController.getAddon(100).url());
        Result result = route(application, request);
        Logger.info("...Test for OK result");
        assertEquals(OK, result.status());
        assertTrue(ControllerTestHelper.testContentType("application/json", result));
    }

    @Test
    public void testDeleteAddon() {
        Addon test = createAddon();
        test.save();
        Logger.info("Testing deleteAddon...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("DELETE")
                .uri(controllers.routes.AddonController.deleteAddon(test.getId()).url());
        Result result = route(application, request);
        Logger.info("...Test for OK result");
        assertEquals(OK, result.status());
        assertTrue(ControllerTestHelper.testContentType("application/json", result));
    }

    private Addon createAddon(){
        return new Addon(CharacterType.KILLER, "billy", "booli");
    }
}
