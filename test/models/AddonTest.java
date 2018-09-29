package models;

import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;

public class AddonTest {

    @Mock
    private long id;

    @Mock
    private CharacterType type;

    @Mock
    private String name;

    @Mock
    private String description;

    private Addon testAddon = new Addon(CharacterType.KILLER, "Billy", "booli");

    @Test
    public void constructorTest() {
        Addon addon = new Addon(type, name, description);
        assertEquals(type, addon.getType());
        assertEquals(name, addon.getName());
        assertEquals(description, addon.getDescription());
    }

    @Test
    public void setId() {
        testAddon.setId(id);
        assertEquals(id, testAddon.getId());
    }

    @Test
    public void setType() {
        testAddon.setType(type);
        assertEquals(type, testAddon.getType());
    }

    @Test
    public void setName() {
        testAddon.setName(name);
        assertEquals(name, testAddon.getName());
    }

    @Test
    public void setDescription() {
        testAddon.setDescription(description);
        assertEquals(description, testAddon.getDescription());
    }
}
