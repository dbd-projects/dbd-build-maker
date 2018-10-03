package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.CharacterType;
import models.Perk;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 * This class controls the flow of Perk game data
 *
 * @author Lowell Buttorff
 */
public class PerkController extends Controller {

    /**
     * HTTP Get request that returns all perks in database
     *
     * @return Result Json list of perks
     */
    public Result getAllPerks() {
        List<Perk> perks = Perk.find.all();
        if(perks.size() > 0) {
            Logger.info("Returning list of Perks with {} elements", perks.size());
            return ok(Json.toJson(perks));
        }
        Logger.info("No Perks to return");
        return noContent();
    }

    /**
     * HTTP Get request that returns all perks of a type
     *
     * @return Result Json list of perks
     */
    @BodyParser.Of(BodyParser.Json.class)
    public Result getAllPerksType() {
        List<Perk> perks;
        final JsonNode json = request().body().asJson();
        if(!json.has("type")) {
            Logger.info("No type given for getAllPerksType");
            return badRequest("No type given");
        }
        try {
            CharacterType type = CharacterType.valueOf(json.get("type").textValue());
            perks = Perk.find.query().where().eq("type", type).findList();
        }catch(Exception e) {
            Logger.info("Invalid type given");
            return badRequest("Invalid type given");
        }
        if(perks.size() > 0) {
            Logger.info("Returning list of Perks with {} elements", perks.size());
            return ok(Json.toJson(perks));
        }
        Logger.info("No Perks to return");
        return noContent();
    }

    /**
     * HTTP Get request that returns a perk from the database
     *
     * @param id The unique ID of a Perk
     * @return Result Json of a perk
     */
    public Result getPerk(final long id) {
        Perk perk = Perk.find.byId(id);
        if(perk == null) {
            return noContent();
        }
        return ok(Json.toJson(perk));
    }

    /**
     * HTTP Post request that creates a new Perk
     *
     * @return Result Json of a perk
     */
    @BodyParser.Of(BodyParser.Json.class)
    public Result createPerk() {
        final JsonNode body = request().body().asJson();
        if(!body.has("name") || !body.has("description") || !body.has("type")) {
            return badRequest("Some data missing from request.");
        }
        final String name = body.get("name").textValue();
        final String description = body.get("description").textValue();
        final String type = body.get("type").textValue();
        if(Perk.find.query().where().eq("name", name).findUnique() != null) {
            return badRequest("A perk already exists with the name, "+name);
        }
        try {
            final CharacterType characterType = CharacterType.valueOf(type);
            final Perk perk = new Perk(characterType, name, description);
            perk.save();
            return ok(Json.toJson(perk));
        }catch(Exception e) {
            Logger.info("An invalid CharacterType was supplied for updatePerk");
            return badRequest("The CharacterType supplied was invalid");
        }
    }

    /**
     * HTTP Put request that updates an existing Perk
     *
     * @param id The Unique ID of a Perk
     * @return Result Json of a perk
     */
    @BodyParser.Of(BodyParser.Json.class)
    public Result updatePerk(final long id) {
        Perk perk = Perk.find.byId(id);
        if(perk == null) {
            return badRequest("There is no perk with id, "+id);
        }
        JsonNode body = request().body().asJson();
        String newName = body.findPath("name").textValue();
        if(newName != null) {
            perk.setName(newName);
        }
        String newDescription = body.findPath("description").textValue();
        if(newDescription != null) {
            perk.setDescription(newDescription);
        }
        String newType = body.findPath("type").textValue();
        if(newName != null) {
            try{
                CharacterType newCharacterType = CharacterType.valueOf(newType);
                perk.setType(newCharacterType);
            } catch(Exception e) {
                Logger.info("An invalid CharacterType was supplied for updatePerk");
                return badRequest("The CharacterType supplied was invalid");
            }
        }
        perk.save();
        return ok(Json.toJson(perk));
    }

    /**
     * HTTP Delete request that deletes a Perk from the database
     *
     * @param id The Unique ID of a Perk
     * @return Result Json of a perk
     */
    public Result deletePerk(final long id) {
        Perk perk = Perk.find.byId(id);
        if(perk == null) {
            return badRequest("There is no perk with id, "+id);
        }
        perk.delete();
        return ok(Json.toJson(perk));
    }
}
