package models;

import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;

public class PerkTest {

    @Mock
    private long id;

    @Mock
    private CharacterType type;

    @Mock
    private String name;

    @Mock
    private String description;

    private Perk testPerk = new Perk(CharacterType.KILLER, "Billy", "booli");

    @Test
    public void constructorTest() {
        Perk addon = new Perk(type, name, description);
        assertEquals(type, addon.getType());
        assertEquals(name, addon.getName());
        assertEquals(description, addon.getDescription());
    }

    @Test
    public void setId() {
        testPerk.setId(id);
        assertEquals(id, testPerk.getId());
    }

    @Test
    public void setType() {
        testPerk.setType(type);
        assertEquals(type, testPerk.getType());
    }

    @Test
    public void setName() {
        testPerk.setName(name);
        assertEquals(name, testPerk.getName());
    }

    @Test
    public void setDescription() {
        testPerk.setDescription(description);
        assertEquals(description, testPerk.getDescription());
    }
}
