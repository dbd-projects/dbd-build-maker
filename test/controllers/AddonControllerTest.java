package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableMap;
import models.Addon;
import models.CharacterType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
        assertEquals(OK, result.status());
        assertTrue(ControllerTestHelper.testContentType("application/json", result));
    }

    @Test
    public void testGetAllAddonsWithNone() {
        Logger.info("Testing getAllAddons with none...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .uri(controllers.routes.AddonController.getAllAddons().url());
        Result result = route(application, request);
        assertEquals(NO_CONTENT, result.status());
    }

    @Test
    public void testGetAllAddonsTypeWithNone() {
        ObjectNode json = Json.newObject();
        json.put("type",String.valueOf(CharacterType.KILLER));
        Logger.info("Testing getAllAddonsType with none...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .bodyJson(json)
                .uri(controllers.routes.AddonController.getAllAddonsType().url());
        Result result = route(application, request);
        assertEquals(NO_CONTENT, result.status());
    }

    @Test
    public void testGetAllAddonsType() {
        createAddon().save();
        ObjectNode json = Json.newObject();
        json.put("type",String.valueOf(CharacterType.KILLER));
        Logger.info("Testing getAllAddonsType...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .bodyJson(json)
                .uri(controllers.routes.AddonController.getAllAddonsType().url());
        Result result = route(application, request);
        assertEquals(OK, result.status());
        assertTrue(ControllerTestHelper.testContentType("application/json", result));
    }

    @Test
    public void testGetAllAddonsBadType() {
        ObjectNode json = Json.newObject();
        json.put("type","invalid");
        Logger.info("Testing getAllAddonsType with bad type...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .bodyJson(json)
                .uri(controllers.routes.AddonController.getAllAddonsType().url());
        Result result = route(application, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void testGetAllAddonsNoType() {
        ObjectNode json = Json.newObject();
        json.put("boost","chuck");
        Logger.info("Testing getAllAddonsType with bad type...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .bodyJson(json)
                .uri(controllers.routes.AddonController.getAllAddonsType().url());
        Result result = route(application, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void testCreateAddon() {
        Logger.info("Testing createAddon...");
        JsonNode jsonNode = Json.toJson(createAddon());
        Http.RequestBuilder request = new Http.RequestBuilder().method("POST")
                .bodyJson(jsonNode)
                .uri(controllers.routes.AddonController.createAddon().url());
        Result result = route(application, request);
        assertEquals(OK, result.status());
    }

    @Test
    public void testCreateExistingAddon() {
        Logger.info("Testing createAddon with existing Addon...");
        createAddon().save();
        JsonNode jsonNode = Json.toJson(createAddon());
        Http.RequestBuilder request = new Http.RequestBuilder().method("POST")
                .bodyJson(jsonNode)
                .uri(controllers.routes.AddonController.createAddon().url());
        Result result = route(application, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void testCreateAddonWithNull() {
        Logger.info("Testing createAddon with existing Addon...");
        JsonNode jsonNode = Json.toJson("'nope':'yep'");
        Http.RequestBuilder request = new Http.RequestBuilder().method("POST")
                .bodyJson(jsonNode)
                .uri(controllers.routes.AddonController.createAddon().url());
        Result result = route(application, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void testCreateAddonBadType() {
        ObjectNode json = Json.newObject();
        json.put("name","billy");
        json.put("description","booli");
        json.put("type","invalid");
        Logger.info("Testing createAddon with bad type...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("POST")
                .bodyJson(json)
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
        assertEquals(OK, result.status());
        assertTrue(ControllerTestHelper.testContentType("application/json", result));
    }

    @Test
    public void testGetNoAddon() {
        Logger.info("Testing getAddon with none...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .uri(controllers.routes.AddonController.getAddon(100).url());
        Result result = route(application, request);
        assertEquals(NO_CONTENT, result.status());
    }

    @Test
    public void testUpdateAddon() {
        Addon testAddon = createAddon();
        testAddon.setId(100);
        testAddon.save();
        testAddon.setName("booli");
        testAddon.setDescription("billy");
        testAddon.setType(CharacterType.SURVIVOR);
        JsonNode json = Json.toJson(testAddon);
        Logger.info("Testing updateAddon...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("PUT")
                .bodyJson(json)
                .uri(controllers.routes.AddonController.updateAddon(100).url());
        Result result = route(application, request);
        assertEquals(OK, result.status());
        assertTrue(ControllerTestHelper.testContentType("application/json", result));
    }

    @Test
    public void testUpdateNoAddon() {
        JsonNode json = Json.toJson(createAddon());
        Logger.info("Testing updateAddon with none...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("PUT")
                .bodyJson(json)
                .uri(controllers.routes.AddonController.updateAddon(100).url());
        Result result = route(application, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void testUpdateAddonBadType() {
        Addon testAddon = createAddon();
        testAddon.setId(100);
        testAddon.save();
        ObjectNode json = Json.newObject();
        json.put("name", "booli");
        json.put("description", "billy");
        json.put("type", "invalid");
        Logger.info("Testing updateAddon with none...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("PUT")
                .bodyJson(json)
                .uri(controllers.routes.AddonController.updateAddon(100).url());
        Result result = route(application, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void testDeleteAddon() {
        Addon test = createAddon();
        test.save();
        Logger.info("Testing deleteAddon...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("DELETE")
                .uri(controllers.routes.AddonController.deleteAddon(test.getId()).url());
        Result result = route(application, request);
        assertEquals(OK, result.status());
        assertTrue(ControllerTestHelper.testContentType("application/json", result));
    }

    @Test
    public void testDeleteNoAddon() {
        Logger.info("Testing deleteAddon...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("DELETE")
                .uri(controllers.routes.AddonController.deleteAddon(0).url());
        Result result = route(application, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    private Addon createAddon() {
        return new Addon(CharacterType.KILLER, "billy", "booli");
    }
}
