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
        List<Addon> addons = Addon.find.all();
        if(addons.size() > 0) {
            Logger.info("Returning list of Addons with {} elements", addons.size());
            return ok(Json.toJson(addons));
        }
        Logger.info("No Addons to return");
        return noContent();
    }

    /**
     * HTTP Get request that returns all addons of a type
     *
     * @return Result Json list of addons
     */
    @BodyParser.Of(BodyParser.Json.class)
    public Result getAllAddonsType() {
        List<Addon> addons;
        final JsonNode json = request().body().asJson();
        if(!json.has("type")) {
            Logger.info("No type given for getAllAddonsType");
            return badRequest("No type given");
        }
        try {
            CharacterType type = CharacterType.valueOf(json.get("type").textValue());
            addons = Addon.find.query().where().eq("type", type).findList();
        }catch(Exception e) {
            Logger.info("Invalid type given");
            return badRequest("Invalid type given");
        }
        if(addons.size() > 0) {
            Logger.info("Returning list of Addons with {} elements", addons.size());
            return ok(Json.toJson(addons));
        }
        Logger.info("No Addons to return");
        return noContent();
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
        if(!body.has("name") || !body.has("description") || !body.has("type")) {
            return badRequest("Some data missing from request.");
        }
        final String name = body.get("name").textValue();
        final String description = body.get("description").textValue();
        final String type = body.get("type").textValue();
        if(Addon.find.query().where().eq("name", name).findUnique() != null) {
            return badRequest("A addon already exists with the name, "+name);
        }
        try {
            final CharacterType characterType = CharacterType.valueOf(type);
            final Addon addon = new Addon(characterType, name, description);
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
