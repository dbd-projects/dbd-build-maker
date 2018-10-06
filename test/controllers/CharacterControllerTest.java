package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableMap;
import models.Character;
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

public class CharacterControllerTest {

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
    public void testGetAllCharacters() {
        createCharacter().save();
        Logger.info("Testing getAllCharacters...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .uri(routes.CharacterController.getAllCharacters().url());
        Result result = route(application, request);
        assertEquals(OK, result.status());
        assertTrue(ControllerTestHelper.testContentType("application/json", result));
    }

    @Test
    public void testGetAllCharactersWithNone() {
        Logger.info("Testing getAllCharacters with none...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .uri(routes.CharacterController.getAllCharacters().url());
        Result result = route(application, request);
        assertEquals(NO_CONTENT, result.status());
    }

    @Test
    public void testGetAllCharactersTypeWithNone() {
        ObjectNode json = Json.newObject();
        json.put("type",String.valueOf(CharacterType.KILLER));
        Logger.info("Testing getAllCharactersType with none...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .bodyJson(json)
                .uri(routes.CharacterController.getAllCharactersType().url());
        Result result = route(application, request);
        assertEquals(NO_CONTENT, result.status());
    }

    @Test
    public void testGetAllCharactersType() {
        createCharacter().save();
        ObjectNode json = Json.newObject();
        json.put("type",String.valueOf(CharacterType.KILLER));
        Logger.info("Testing getAllCharactersType...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .bodyJson(json)
                .uri(routes.CharacterController.getAllCharactersType().url());
        Result result = route(application, request);
        assertEquals(OK, result.status());
        assertTrue(ControllerTestHelper.testContentType("application/json", result));
    }

    @Test
    public void testGetAllCharactersBadType() {
        ObjectNode json = Json.newObject();
        json.put("type","invalid");
        Logger.info("Testing getAllCharactersType with bad type...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .bodyJson(json)
                .uri(routes.CharacterController.getAllCharactersType().url());
        Result result = route(application, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void testGetAllCharactersNoType() {
        ObjectNode json = Json.newObject();
        json.put("boost","chuck");
        Logger.info("Testing getAllCharactersType with bad type...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .bodyJson(json)
                .uri(routes.CharacterController.getAllCharactersType().url());
        Result result = route(application, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void testCreateCharacter() {
        Logger.info("Testing createCharacter...");
        JsonNode jsonNode = Json.toJson(createCharacter());
        Http.RequestBuilder request = new Http.RequestBuilder().method("POST")
                .bodyJson(jsonNode)
                .uri(routes.CharacterController.createCharacter().url());
        Result result = route(application, request);
        assertEquals(OK, result.status());
    }

    @Test
    public void testCreateExistingCharacter() {
        Logger.info("Testing createCharacter with existing Character...");
        createCharacter().save();
        JsonNode jsonNode = Json.toJson(createCharacter());
        Http.RequestBuilder request = new Http.RequestBuilder().method("POST")
                .bodyJson(jsonNode)
                .uri(routes.CharacterController.createCharacter().url());
        Result result = route(application, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void testCreateCharacterWithNull() {
        Logger.info("Testing createCharacter with existing Character...");
        JsonNode jsonNode = Json.toJson("'nope':'yep'");
        Http.RequestBuilder request = new Http.RequestBuilder().method("POST")
                .bodyJson(jsonNode)
                .uri(routes.CharacterController.createCharacter().url());
        Result result = route(application, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void testCreateCharacterBadType() {
        ObjectNode json = Json.newObject();
        json.put("name","billy");
        json.put("description","booli");
        json.put("type","invalid");
        Logger.info("Testing createCharacter with bad type...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("POST")
                .bodyJson(json)
                .uri(routes.CharacterController.createCharacter().url());
        Result result = route(application, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void testGetCharacter() {
        Character test = createCharacter();
        test.setId(100);
        test.save();
        Logger.info("Testing getCharacter...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .uri(routes.CharacterController.getCharacter(100).url());
        Result result = route(application, request);
        assertEquals(OK, result.status());
        assertTrue(ControllerTestHelper.testContentType("application/json", result));
    }

    @Test
    public void testGetNoCharacter() {
        Logger.info("Testing getCharacter with none...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .uri(routes.CharacterController.getCharacter(100).url());
        Result result = route(application, request);
        assertEquals(NO_CONTENT, result.status());
    }

    @Test
    public void testUpdateCharacter() {
        Character testCharacter = createCharacter();
        testCharacter.setId(100);
        testCharacter.save();
        testCharacter.setName("booli");
        testCharacter.setDescription("billy");
        testCharacter.setType(CharacterType.SURVIVOR);
        JsonNode json = Json.toJson(testCharacter);
        Logger.info("Testing updateCharacter...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("PUT")
                .bodyJson(json)
                .uri(routes.CharacterController.updateCharacter(100).url());
        Result result = route(application, request);
        assertEquals(OK, result.status());
        assertTrue(ControllerTestHelper.testContentType("application/json", result));
    }

    @Test
    public void testUpdateNoCharacter() {
        JsonNode json = Json.toJson(createCharacter());
        Logger.info("Testing updateCharacter with none...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("PUT")
                .bodyJson(json)
                .uri(routes.CharacterController.updateCharacter(100).url());
        Result result = route(application, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void testUpdateCharacterBadType() {
        Character testCharacter = createCharacter();
        testCharacter.setId(100);
        testCharacter.save();
        ObjectNode json = Json.newObject();
        json.put("name", "booli");
        json.put("description", "billy");
        json.put("type", "invalid");
        Logger.info("Testing updateCharacter with none...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("PUT")
                .bodyJson(json)
                .uri(routes.CharacterController.updateCharacter(100).url());
        Result result = route(application, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void testDeleteCharacter() {
        Character test = createCharacter();
        test.save();
        Logger.info("Testing deleteCharacter...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("DELETE")
                .uri(routes.CharacterController.deleteCharacter(test.getId()).url());
        Result result = route(application, request);
        assertEquals(OK, result.status());
        assertTrue(ControllerTestHelper.testContentType("application/json", result));
    }

    @Test
    public void testDeleteNoCharacter() {
        Logger.info("Testing deleteCharacter...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("DELETE")
                .uri(routes.CharacterController.deleteCharacter(0).url());
        Result result = route(application, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    private Character createCharacter() {
        return new Character(CharacterType.KILLER, "billy", "booli");
    }
}
