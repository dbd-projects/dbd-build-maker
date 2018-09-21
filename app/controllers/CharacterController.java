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
        if(characters == null || characters.size() == 0) {
            return ok("No characters in database");
        }
        return ok(Json.toJson(characters));
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
            return notFound("Could not find character by id: "+id);
        }
        return ok(Json.toJson(character));
    }

    /**
     * HTTP Post request that creates a new Character
     *
     * @param name The String name of a Character
     * @param description The String description of a Character
     * @param type The String type of a Character
     * @return Result Json of a character
     */
    public Result createCharacter(final String name, final String description, final CharacterType type) {
        if(Character.find.query().where().eq("name", name).findUnique() != null) {
            return badRequest("A character already exists with the name, "+name);
        }
        Character character = new Character(type, name, description);
        character.save();
        return ok(Json.toJson(character));
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
