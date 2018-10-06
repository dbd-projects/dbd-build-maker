package models;

import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;

public class ItemTest {

    @Mock
    private long id;

    @Mock
    private CharacterType type;

    @Mock
    private String name;

    @Mock
    private String description;

    private Item testItem = new Item(CharacterType.KILLER, "Billy", "booli");

    @Test
    public void constructorTest() {
        Item addon = new Item(type, name, description);
        assertEquals(type, addon.getType());
        assertEquals(name, addon.getName());
        assertEquals(description, addon.getDescription());
    }

    @Test
    public void setId() {
        testItem.setId(id);
        assertEquals(id, testItem.getId());
    }

    @Test
    public void setType() {
        testItem.setType(type);
        assertEquals(type, testItem.getType());
    }

    @Test
    public void setName() {
        testItem.setName(name);
        assertEquals(name, testItem.getName());
    }

    @Test
    public void setDescription() {
        testItem.setDescription(description);
        assertEquals(description, testItem.getDescription());
    }
}
