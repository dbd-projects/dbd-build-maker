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
        List<Perk> perks;
        try {
            if(request().body().asJson().has("type")) {
                String type = request().body().asJson().get("type").textValue();
                perks = Perk.find.query().where().eq("type", type).findList();
            }else {
                perks = Perk.find.all();
            }
            return ok(Json.toJson(perks));
        }catch (NullPointerException e) {
            return noContent();
        }
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
        final String name = body.get("name").textValue();
        final String description = body.get("description").textValue();
        final String type = body.get("type").textValue();
        if(name == null || description == null || type == null) {
            return badRequest("Some data missing from request.");
        }
        if(Perk.find.query().where().eq("name", name).findUnique() != null) {
            return badRequest("A perk already exists with the name, "+name);
        }
        try {
            CharacterType characterType = CharacterType.valueOf(type);
            Perk perk = new Perk(characterType, name, description);
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
