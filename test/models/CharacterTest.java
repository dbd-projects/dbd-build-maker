package models;

import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;

public class CharacterTest {

    @Mock
    private long id;

    @Mock
    private CharacterType type;

    @Mock
    private String name;

    @Mock
    private String description;

    private Character testCharacter = new Character(CharacterType.KILLER, "Billy", "booli");

    @Test
    public void constructorTest() {
        Character addon = new Character(type, name, description);
        assertEquals(type, addon.getType());
        assertEquals(name, addon.getName());
        assertEquals(description, addon.getDescription());
    }

    @Test
    public void setId() {
        testCharacter.setId(id);
        assertEquals(id, testCharacter.getId());
    }

    @Test
    public void setType() {
        testCharacter.setType(type);
        assertEquals(type, testCharacter.getType());
    }

    @Test
    public void setName() {
        testCharacter.setName(name);
        assertEquals(name, testCharacter.getName());
    }

    @Test
    public void setDescription() {
        testCharacter.setDescription(description);
        assertEquals(description, testCharacter.getDescription());
    }
}
