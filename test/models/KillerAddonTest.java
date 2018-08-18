package models;

import org.junit.Test;
import play.Logger;

import static org.junit.Assert.assertEquals;

public class KillerAddonTest {

    @Test
    public void emptyConstructorTest() {
        Logger.info("Testing KillerAddon empty constructor...");
        KillerAddon killerAddon = new KillerAddon();
    }

    @Test
    public void dbConstructorTest() {
        Logger.info("Test KillerAddon database constructor...");
        KillerAddon killerAddon = new KillerAddon("Dead Rabbit", 8, 5, 3,
                3, 6);
        assertEquals("Dead Rabbit", killerAddon.getName());
        assertEquals(8, killerAddon.getEarlyScore());
        assertEquals(5, killerAddon.getLateScore());
        assertEquals(3, killerAddon.getGenStopScore());
        assertEquals(3, killerAddon.getHuntScore());
        assertEquals(6, killerAddon.getCampScore());
    }
}
