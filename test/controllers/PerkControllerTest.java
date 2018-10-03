package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableMap;
import models.CharacterType;
import models.Perk;
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

public class PerkControllerTest {

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
    public void testGetAllPerks() {
        createPerk().save();
        Logger.info("Testing getAllPerks...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .uri(controllers.routes.PerkController.getAllPerks().url());
        Result result = route(application, request);
        assertEquals(OK, result.status());
        assertTrue(ControllerTestHelper.testContentType("application/json", result));
    }

    @Test
    public void testGetAllPerksWithNone() {
        Logger.info("Testing getAllPerks with none...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .uri(controllers.routes.PerkController.getAllPerks().url());
        Result result = route(application, request);
        assertEquals(NO_CONTENT, result.status());
    }

    @Test
    public void testGetAllPerksTypeWithNone() {
        ObjectNode json = Json.newObject();
        json.put("type",String.valueOf(CharacterType.KILLER));
        Logger.info("Testing getAllPerksType with none...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .bodyJson(json)
                .uri(controllers.routes.PerkController.getAllPerksType().url());
        Result result = route(application, request);
        assertEquals(NO_CONTENT, result.status());
    }

    @Test
    public void testGetAllPerksType() {
        createPerk().save();
        ObjectNode json = Json.newObject();
        json.put("type",String.valueOf(CharacterType.KILLER));
        Logger.info("Testing getAllPerksType...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .bodyJson(json)
                .uri(controllers.routes.PerkController.getAllPerksType().url());
        Result result = route(application, request);
        assertEquals(OK, result.status());
        assertTrue(ControllerTestHelper.testContentType("application/json", result));
    }

    @Test
    public void testGetAllPerksBadType() {
        ObjectNode json = Json.newObject();
        json.put("type","invalid");
        Logger.info("Testing getAllPerksType with bad type...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .bodyJson(json)
                .uri(controllers.routes.PerkController.getAllPerksType().url());
        Result result = route(application, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void testGetAllPerksNoType() {
        ObjectNode json = Json.newObject();
        json.put("boost","chuck");
        Logger.info("Testing getAllPerksType with bad type...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .bodyJson(json)
                .uri(controllers.routes.PerkController.getAllPerksType().url());
        Result result = route(application, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void testCreatePerk() {
        Logger.info("Testing createPerk...");
        JsonNode jsonNode = Json.toJson(createPerk());
        Http.RequestBuilder request = new Http.RequestBuilder().method("POST")
                .bodyJson(jsonNode)
                .uri(controllers.routes.PerkController.createPerk().url());
        Result result = route(application, request);
        assertEquals(OK, result.status());
    }

    @Test
    public void testCreateExistingPerk() {
        Logger.info("Testing createPerk with existing Perk...");
        createPerk().save();
        JsonNode jsonNode = Json.toJson(createPerk());
        Http.RequestBuilder request = new Http.RequestBuilder().method("POST")
                .bodyJson(jsonNode)
                .uri(controllers.routes.PerkController.createPerk().url());
        Result result = route(application, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void testCreatePerkWithNull() {
        Logger.info("Testing createPerk with existing Perk...");
        JsonNode jsonNode = Json.toJson("'nope':'yep'");
        Http.RequestBuilder request = new Http.RequestBuilder().method("POST")
                .bodyJson(jsonNode)
                .uri(controllers.routes.PerkController.createPerk().url());
        Result result = route(application, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void testCreatePerkBadType() {
        ObjectNode json = Json.newObject();
        json.put("name","billy");
        json.put("description","booli");
        json.put("type","invalid");
        Logger.info("Testing createPerk with bad type...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("POST")
                .bodyJson(json)
                .uri(controllers.routes.PerkController.createPerk().url());
        Result result = route(application, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void testGetPerk() {
        Perk test = createPerk();
        test.setId(100);
        test.save();
        Logger.info("Testing getPerk...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .uri(controllers.routes.PerkController.getPerk(100).url());
        Result result = route(application, request);
        assertEquals(OK, result.status());
        assertTrue(ControllerTestHelper.testContentType("application/json", result));
    }

    @Test
    public void testGetNoPerk() {
        Logger.info("Testing getPerk with none...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .uri(controllers.routes.PerkController.getPerk(100).url());
        Result result = route(application, request);
        assertEquals(NO_CONTENT, result.status());
    }

    @Test
    public void testUpdatePerk() {
        Perk testPerk = createPerk();
        testPerk.setId(100);
        testPerk.save();
        testPerk.setName("booli");
        testPerk.setDescription("billy");
        testPerk.setType(CharacterType.SURVIVOR);
        JsonNode json = Json.toJson(testPerk);
        Logger.info("Testing updatePerk...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("PUT")
                .bodyJson(json)
                .uri(controllers.routes.PerkController.updatePerk(100).url());
        Result result = route(application, request);
        assertEquals(OK, result.status());
        assertTrue(ControllerTestHelper.testContentType("application/json", result));
    }

    @Test
    public void testUpdateNoPerk() {
        JsonNode json = Json.toJson(createPerk());
        Logger.info("Testing updatePerk with none...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("PUT")
                .bodyJson(json)
                .uri(controllers.routes.PerkController.updatePerk(100).url());
        Result result = route(application, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void testUpdatePerkBadType() {
        Perk testPerk = createPerk();
        testPerk.setId(100);
        testPerk.save();
        ObjectNode json = Json.newObject();
        json.put("name", "booli");
        json.put("description", "billy");
        json.put("type", "invalid");
        Logger.info("Testing updatePerk with none...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("PUT")
                .bodyJson(json)
                .uri(controllers.routes.PerkController.updatePerk(100).url());
        Result result = route(application, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void testDeletePerk() {
        Perk test = createPerk();
        test.save();
        Logger.info("Testing deletePerk...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("DELETE")
                .uri(controllers.routes.PerkController.deletePerk(test.getId()).url());
        Result result = route(application, request);
        assertEquals(OK, result.status());
        assertTrue(ControllerTestHelper.testContentType("application/json", result));
    }

    @Test
    public void testDeleteNoPerk() {
        Logger.info("Testing deletePerk...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("DELETE")
                .uri(controllers.routes.PerkController.deletePerk(0).url());
        Result result = route(application, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    private Perk createPerk() {
        return new Perk(CharacterType.KILLER, "billy", "booli");
    }
}
