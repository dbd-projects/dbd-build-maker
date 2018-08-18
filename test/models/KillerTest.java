package models;

import org.junit.Test;
import org.mockito.Mock;
import play.Logger;

import static org.junit.Assert.assertEquals;

public class KillerTest {

    @Mock
    KillerPerk perkOne;
    @Mock
    KillerPerk perkTwo;
    @Mock
    KillerPerk perkThree;
    @Mock
    KillerPerk perkFour;
    @Mock
    KillerAddon addonOne;
    @Mock
    KillerAddon addonTwo;

    @Test
    public void emptyConstructorTest() {
        Logger.info("Testing Killer empty constructor...");
        Killer killerOne = new Killer();
    }

    @Test
    public void dbConstructorTest() {
        Logger.info("Test Killer database constructor...");
        Killer killerTwo = new Killer("Billy", "Chainsaw", 8, 5, 3,
                3, 6);
        assertEquals("Billy", killerTwo.getName());
        assertEquals("Chainsaw", killerTwo.getPowerName());
        assertEquals(8, killerTwo.getEarlyScore());
        assertEquals(5, killerTwo.getLateScore());
        assertEquals(3, killerTwo.getGenStopScore());
        assertEquals(3, killerTwo.getHuntScore());
        assertEquals(6, killerTwo.getCampScore());
    }

    @Test
    public void functionalConstructorTest() {
        Logger.info("Test Killer functional constructor...");
        Killer killerThree = new Killer("Billy", "Chainsaw", 8, 5, 3,
                3, 6, addonOne, addonTwo, perkOne, perkTwo, perkThree, perkFour);
        assertEquals("Billy", killerThree.getName());
        assertEquals("Chainsaw", killerThree.getPowerName());
        assertEquals(8, killerThree.getEarlyScore());
        assertEquals(5, killerThree.getLateScore());
        assertEquals(3, killerThree.getGenStopScore());
        assertEquals(3, killerThree.getHuntScore());
        assertEquals(6, killerThree.getCampScore());
        assertEquals(addonOne, killerThree.getAddonOne());
        assertEquals(addonTwo, killerThree.getAddonTwo());
        assertEquals(perkOne, killerThree.getPerkOne());
        assertEquals(perkTwo, killerThree.getPerkTwo());
        assertEquals(perkThree, killerThree.getPerkThree());
        assertEquals(perkFour, killerThree.getPerkFour());
    }

    @Test
    public void setPerkOneTest() {
        Logger.info("Test Killer setPerkOne...");
        Killer killer = new Killer();
        killer.setPerkOne(perkOne);
        assertEquals(perkOne, killer.getPerkOne());
    }

    @Test
    public void setPerkTwoTest() {
        Logger.info("Test Killer setPerkTwo...");
        Killer killer = new Killer();
        killer.setPerkTwo(perkTwo);
        assertEquals(perkTwo, killer.getPerkTwo());
    }

    @Test
    public void setPerkThreeTest() {
        Logger.info("Test Killer setPerkThree...");
        Killer killer = new Killer();
        killer.setPerkThree(perkThree);
        assertEquals(perkThree, killer.getPerkThree());
    }

    @Test
    public void setPerkFourTest() {
        Logger.info("Test Killer setPerkFour...");
        Killer killer = new Killer();
        killer.setPerkFour(perkFour);
        assertEquals(perkFour, killer.getPerkFour());
    }

    @Test
    public void setAddonOneTest() {
        Logger.info("Test Killer setAddoneOne...");
        Killer killer = new Killer();
        killer.setAddonOne(addonOne);
        assertEquals(addonOne, killer.getAddonOne());
    }

    @Test
    public void setAddonTwoTest() {
        Logger.info("Test Killer setAddonTwo...");
        Killer killer = new Killer();
        killer.setAddonTwo(addonTwo);
        assertEquals(addonTwo, killer.getAddonTwo());
    }
}
