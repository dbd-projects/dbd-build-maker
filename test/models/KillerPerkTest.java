package models;

import org.junit.Test;
import play.Logger;

import static org.junit.Assert.assertEquals;

public class KillerPerkTest {

    @Test
    public void emptyConstructorTest() {
        Logger.info("Testing KillerPerk empty constructor...");
        KillerPerk killerPerk = new KillerPerk();
    }

    @Test
    public void dbConstructorTest() {
        Logger.info("Test KillerPerk database constructor...");
        KillerPerk killerPerk = new KillerPerk("Ruin", 8, 5, 3,
                3, 6);
        assertEquals("Ruin", killerPerk.getName());
        assertEquals(8, killerPerk.getEarlyScore());
        assertEquals(5, killerPerk.getLateScore());
        assertEquals(3, killerPerk.getGenStopScore());
        assertEquals(3, killerPerk.getHuntScore());
        assertEquals(6, killerPerk.getCampScore());
    }

}
