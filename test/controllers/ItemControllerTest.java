package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableMap;
import models.CharacterType;
import models.Item;
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

public class ItemControllerTest {

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
    public void testGetAllItems() {
        createItem().save();
        Logger.info("Testing getAllItems...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .uri(controllers.routes.ItemController.getAllItems().url());
        Result result = route(application, request);
        assertEquals(OK, result.status());
        assertTrue(ControllerTestHelper.testContentType("application/json", result));
    }

    @Test
    public void testGetAllItemsWithNone() {
        Logger.info("Testing getAllItems with none...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .uri(controllers.routes.ItemController.getAllItems().url());
        Result result = route(application, request);
        assertEquals(NO_CONTENT, result.status());
    }

    @Test
    public void testGetAllItemsTypeWithNone() {
        ObjectNode json = Json.newObject();
        json.put("type",String.valueOf(CharacterType.KILLER));
        Logger.info("Testing getAllItemsType with none...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .bodyJson(json)
                .uri(controllers.routes.ItemController.getAllItemsType().url());
        Result result = route(application, request);
        assertEquals(NO_CONTENT, result.status());
    }

    @Test
    public void testGetAllItemsType() {
        createItem().save();
        ObjectNode json = Json.newObject();
        json.put("type",String.valueOf(CharacterType.KILLER));
        Logger.info("Testing getAllItemsType...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .bodyJson(json)
                .uri(controllers.routes.ItemController.getAllItemsType().url());
        Result result = route(application, request);
        assertEquals(OK, result.status());
        assertTrue(ControllerTestHelper.testContentType("application/json", result));
    }

    @Test
    public void testGetAllItemsBadType() {
        ObjectNode json = Json.newObject();
        json.put("type","invalid");
        Logger.info("Testing getAllItemsType with bad type...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .bodyJson(json)
                .uri(controllers.routes.ItemController.getAllItemsType().url());
        Result result = route(application, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void testGetAllItemsNoType() {
        ObjectNode json = Json.newObject();
        json.put("boost","chuck");
        Logger.info("Testing getAllItemsType with bad type...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .bodyJson(json)
                .uri(controllers.routes.ItemController.getAllItemsType().url());
        Result result = route(application, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void testCreateItem() {
        Logger.info("Testing createItem...");
        JsonNode jsonNode = Json.toJson(createItem());
        Http.RequestBuilder request = new Http.RequestBuilder().method("POST")
                .bodyJson(jsonNode)
                .uri(controllers.routes.ItemController.createItem().url());
        Result result = route(application, request);
        assertEquals(OK, result.status());
    }

    @Test
    public void testCreateExistingItem() {
        Logger.info("Testing createItem with existing Item...");
        createItem().save();
        JsonNode jsonNode = Json.toJson(createItem());
        Http.RequestBuilder request = new Http.RequestBuilder().method("POST")
                .bodyJson(jsonNode)
                .uri(controllers.routes.ItemController.createItem().url());
        Result result = route(application, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void testCreateItemWithNull() {
        Logger.info("Testing createItem with existing Item...");
        JsonNode jsonNode = Json.toJson("'nope':'yep'");
        Http.RequestBuilder request = new Http.RequestBuilder().method("POST")
                .bodyJson(jsonNode)
                .uri(controllers.routes.ItemController.createItem().url());
        Result result = route(application, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void testCreateItemBadType() {
        ObjectNode json = Json.newObject();
        json.put("name","billy");
        json.put("description","booli");
        json.put("type","invalid");
        Logger.info("Testing createItem with bad type...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("POST")
                .bodyJson(json)
                .uri(controllers.routes.ItemController.createItem().url());
        Result result = route(application, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void testGetItem() {
        Item test = createItem();
        test.setId(100);
        test.save();
        Logger.info("Testing getItem...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .uri(controllers.routes.ItemController.getItem(100).url());
        Result result = route(application, request);
        assertEquals(OK, result.status());
        assertTrue(ControllerTestHelper.testContentType("application/json", result));
    }

    @Test
    public void testGetNoItem() {
        Logger.info("Testing getItem with none...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .uri(controllers.routes.ItemController.getItem(100).url());
        Result result = route(application, request);
        assertEquals(NO_CONTENT, result.status());
    }

    @Test
    public void testUpdateItem() {
        Item testItem = createItem();
        testItem.setId(100);
        testItem.save();
        testItem.setName("booli");
        testItem.setDescription("billy");
        testItem.setType(CharacterType.SURVIVOR);
        JsonNode json = Json.toJson(testItem);
        Logger.info("Testing updateItem...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("PUT")
                .bodyJson(json)
                .uri(controllers.routes.ItemController.updateItem(100).url());
        Result result = route(application, request);
        assertEquals(OK, result.status());
        assertTrue(ControllerTestHelper.testContentType("application/json", result));
    }

    @Test
    public void testUpdateNoItem() {
        JsonNode json = Json.toJson(createItem());
        Logger.info("Testing updateItem with none...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("PUT")
                .bodyJson(json)
                .uri(controllers.routes.ItemController.updateItem(100).url());
        Result result = route(application, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void testUpdateItemBadType() {
        Item testItem = createItem();
        testItem.setId(100);
        testItem.save();
        ObjectNode json = Json.newObject();
        json.put("name", "booli");
        json.put("description", "billy");
        json.put("type", "invalid");
        Logger.info("Testing updateItem with none...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("PUT")
                .bodyJson(json)
                .uri(controllers.routes.ItemController.updateItem(100).url());
        Result result = route(application, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void testDeleteItem() {
        Item test = createItem();
        test.save();
        Logger.info("Testing deleteItem...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("DELETE")
                .uri(controllers.routes.ItemController.deleteItem(test.getId()).url());
        Result result = route(application, request);
        assertEquals(OK, result.status());
        assertTrue(ControllerTestHelper.testContentType("application/json", result));
    }

    @Test
    public void testDeleteNoItem() {
        Logger.info("Testing deleteItem...");
        Http.RequestBuilder request = new Http.RequestBuilder().method("DELETE")
                .uri(controllers.routes.ItemController.deleteItem(0).url());
        Result result = route(application, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    private Item createItem() {
        return new Item(CharacterType.KILLER, "billy", "booli");
    }
}
