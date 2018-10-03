package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Character;
import models.CharacterType;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 * This class controls the flow of Character game data
 *
 * @author Lowell Buttorff
 */
public class CharacterController extends Controller {

    /**
     * HTTP Get request that returns all characters in database
     *
     * @return Result Json list of characters
     */
    public Result getAllCharacters() {
        List<Character> characters = Character.find.all();
        if(characters.size() > 0) {
            Logger.info("Returning list of Characters with {} elements", characters.size());
            return ok(Json.toJson(characters));
        }
        Logger.info("No Characters to return");
        return noContent();
    }

    /**
     * HTTP Get request that returns all characters of a type
     *
     * @return Result Json list of characters
     */
    @BodyParser.Of(BodyParser.Json.class)
    public Result getAllCharactersType() {
        List<Character> characters;
        final JsonNode json = request().body().asJson();
        if(!json.has("type")) {
            Logger.info("No type given for getAllCharactersType");
            return badRequest("No type given");
        }
        try {
            CharacterType type = CharacterType.valueOf(json.get("type").textValue());
            characters = Character.find.query().where().eq("type", type).findList();
        }catch(Exception e) {
            Logger.info("Invalid type given");
            return badRequest("Invalid type given");
        }
        if(characters.size() > 0) {
            Logger.info("Returning list of Characters with {} elements", characters.size());
            return ok(Json.toJson(characters));
        }
        Logger.info("No Characters to return");
        return noContent();
    }

    /**
     * HTTP Get request that returns a character from the database
     *
     * @param id The unique ID of a Character
     * @return Result Json of a character
     */
    public Result getCharacter(final long id) {
        Character character = Character.find.byId(id);
        if(character == null) {
            return noContent();
        }
        return ok(Json.toJson(character));
    }

    /**
     * HTTP Post request that creates a new Character
     *
     * @return Result Json of a character
     */
    @BodyParser.Of(BodyParser.Json.class)
    public Result createCharacter() {
        final JsonNode body = request().body().asJson();
        if(!body.has("name") || !body.has("description") || !body.has("type")) {
            return badRequest("Some data missing from request.");
        }
        final String name = body.get("name").textValue();
        final String description = body.get("description").textValue();
        final String type = body.get("type").textValue();
        if(Character.find.query().where().eq("name", name).findUnique() != null) {
            return badRequest("A character already exists with the name, "+name);
        }
        try {
            final CharacterType characterType = CharacterType.valueOf(type);
            final Character character = new Character(characterType, name, description);
            character.save();
            return ok(Json.toJson(character));
        }catch(Exception e) {
            Logger.info("An invalid CharacterType was supplied for updateCharacter");
            return badRequest("The CharacterType supplied was invalid");
        }
    }

    /**
     * HTTP Put request that updates an existing Character
     *
     * @param id The Unique ID of a Character
     * @return Result Json of a character
     */
    @BodyParser.Of(BodyParser.Json.class)
    public Result updateCharacter(final long id) {
        Character character = Character.find.byId(id);
        if(character == null) {
            return badRequest("There is no character with id, "+id);
        }
        JsonNode body = request().body().asJson();
        String newName = body.findPath("name").textValue();
        if(newName != null) {
            character.setName(newName);
        }
        String newDescription = body.findPath("description").textValue();
        if(newDescription != null) {
            character.setDescription(newDescription);
        }
        String newType = body.findPath("type").textValue();
        if(newName != null) {
            try{
                CharacterType newCharacterType = CharacterType.valueOf(newType);
                character.setType(newCharacterType);
            } catch(Exception e) {
                Logger.info("An invalid CharacterType was supplied for updateCharacter");
                return badRequest("The CharacterType supplied was invalid");
            }
        }
        character.save();
        return ok(Json.toJson(character));
    }

    /**
     * HTTP Delete request that deletes a Character from the database
     *
     * @param id The Unique ID of a Character
     * @return Result Json of a character
     */
    public Result deleteCharacter(final long id) {
        Character character = Character.find.byId(id);
        if(character == null) {
            return badRequest("There is no character with id, "+id);
        }
        character.delete();
        return ok(Json.toJson(character));
    }
}
