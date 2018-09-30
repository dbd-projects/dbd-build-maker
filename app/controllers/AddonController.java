package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Addon;
import models.CharacterType;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 * This class controls the flow of Addon game data
 *
 * @author Lowell Buttorff
 */
public class AddonController extends Controller {

    /**
     * HTTP Get request that returns all addons in database
     *
     * @return Result Json list of addons
     */
    public Result getAllAddons() {
        List<Addon> addons;
        try {
            if(request().body().asJson().has("type")) {
                String type = request().body().asJson().get("type").textValue();
                addons = Addon.find.query().where().eq("type", type).findList();
            }else {
                addons = Addon.find.all();
            }
            return ok(Json.toJson(addons));
        }catch (NullPointerException e) {
            return noContent();
        }
    }

    /**
     * HTTP Get request that returns a addon from the database
     *
     * @param id The unique ID of a Addon
     * @return Result Json of a addon
     */
    public Result getAddon(final long id) {
        Addon addon = Addon.find.byId(id);
        if(addon == null) {
            return noContent();
        }
        return ok(Json.toJson(addon));
    }

    /**
     * HTTP Post request that creates a new Addon
     *
     * @return Result Json of a addon
     */
    @BodyParser.Of(BodyParser.Json.class)
    public Result createAddon() {
        final JsonNode body = request().body().asJson();
        final String name = body.get("name").textValue();
        final String description = body.get("description").textValue();
        final String type = body.get("type").textValue();
        if(name == null || description == null || type == null) {
            return badRequest("Some data missing from request.");
        }
        if(Addon.find.query().where().eq("name", name).findUnique() != null) {
            return badRequest("A addon already exists with the name, "+name);
        }
        try {
            CharacterType characterType = CharacterType.valueOf(type);
            Addon addon = new Addon(characterType, name, description);
            addon.save();
            return ok(Json.toJson(addon));
        }catch(Exception e) {
            Logger.info("An invalid CharacterType was supplied for updateAddon");
            return badRequest("The CharacterType supplied was invalid");
        }
    }

    /**
     * HTTP Put request that updates an existing Addon
     *
     * @param id The Unique ID of a Addon
     * @return Result Json of a addon
     */
    @BodyParser.Of(BodyParser.Json.class)
    public Result updateAddon(final long id) {
        Addon addon = Addon.find.byId(id);
        if(addon == null) {
            return badRequest("There is no addon with id, "+id);
        }
        JsonNode body = request().body().asJson();
        String newName = body.findPath("name").textValue();
        if(newName != null) {
            addon.setName(newName);
        }
        String newDescription = body.findPath("description").textValue();
        if(newDescription != null) {
            addon.setDescription(newDescription);
        }
        String newType = body.findPath("type").textValue();
        if(newName != null) {
            try{
                CharacterType newCharacterType = CharacterType.valueOf(newType);
                addon.setType(newCharacterType);
            } catch(Exception e) {
                Logger.info("An invalid CharacterType was supplied for updateAddon");
                return badRequest("The CharacterType supplied was invalid");
            }
        }
        addon.save();
        return ok(Json.toJson(addon));
    }

    /**
     * HTTP Delete request that deletes a Addon from the database
     *
     * @param id The Unique ID of a Addon
     * @return Result Json of a addon
     */
    public Result deleteAddon(final long id) {
        Addon addon = Addon.find.byId(id);
        if(addon == null) {
            return badRequest("There is no addon with id, "+id);
        }
        addon.delete();
        return ok(Json.toJson(addon));
    }
}
