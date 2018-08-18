package models;

import com.avaje.ebean.Model;

import javax.persistence.*;

@Entity
@Table(name = "killers")
public class Killer extends Model {

    // Ebean Finder utility
    public static Finder<Long, Killer> find = new Finder<>(Killer.class);

    @Id
    @GeneratedValue
    private long id;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(name = "power_name", unique = true, nullable = false)
    private String powerName;
    // How much kill potential does the killer have right away
    @Column(name = "early_score", nullable = false)
    private int earlyScore;
    // How much kill potential is gained as the game goes by
    @Column(name = "late_score", nullable = false)
    private int lateScore;
    // How much does this killer slow generator progress (no addons)
    @Column(name = "gen_stop_score", nullable = false)
    private int genStopScore;
    // How well does this killer do when hunting survivors
    @Column(name = "hunt_score", nullable = false)
    private int huntScore;
    // How good is the killer at camping hooks/gens/totems
    @Column(name = "camp_score", nullable = false)
    private int campScore;
    @Transient
    private KillerAddon addonOne;
    @Transient
    private KillerAddon addonTwo;
    @Transient
    private KillerPerk perkOne;
    @Transient
    private KillerPerk perkTwo;
    @Transient
    private KillerPerk perkThree;
    @Transient
    private KillerPerk perkFour;

    /**
     * The default constructor
     */
    public Killer() {
        // Empty default constructor
    }

    /**
     * The database centric constructor
     *
     * @param name         the name of the Killer
     * @param powerName    the name of the Killer's Power
     * @param earlyScore   the Killer's early game score
     * @param lateScore    the Killer's late game score
     * @param genStopScore the Killer's generator stopping score
     * @param huntScore    the Killer's hunting score
     * @param campScore    the Killer's camping score
     */
    protected Killer(final String name, final String powerName, final int earlyScore, final int lateScore,
                     final int genStopScore, final int huntScore, final int campScore) {
        this.name = name;
        this.powerName = powerName;
        this.earlyScore = earlyScore;
        this.lateScore = lateScore;
        this.genStopScore = genStopScore;
        this.huntScore = huntScore;
        this.campScore = campScore;
    }

    /**
     * The functional constructor (contains transient members)
     *
     * @param name         the name of the Killer
     * @param powerName    the name of the Killer's Power
     * @param earlyScore   the Killer's early game score
     * @param lateScore    the Killer's late game score
     * @param genStopScore the Killer's generator stopping score
     * @param huntScore    the Killer's hunting score
     * @param campScore    the Killer's camping score
     * @param aOne         an addon for the Killer
     * @param aTwo         an addon for the Killer
     * @param pOne         a perk for the Killer
     * @param pTwo         a perk for the Killer
     * @param pThree       a perk for the Killer
     * @param pFour        a perk for the Killer
     */
    public Killer(final String name, final String powerName, final int earlyScore, final int lateScore,
                  final int genStopScore, final int huntScore, final int campScore, KillerAddon aOne, KillerAddon aTwo,
                  KillerPerk pOne, KillerPerk pTwo, KillerPerk pThree, KillerPerk pFour) {
        this.name = name;
        this.powerName = powerName;
        this.earlyScore = earlyScore;
        this.lateScore = lateScore;
        this.genStopScore = genStopScore;
        this.huntScore = huntScore;
        this.campScore = campScore;
        this.addonOne = aOne;
        this.addonTwo = aTwo;
        this.perkOne = pOne;
        this.perkTwo = pTwo;
        this.perkThree = pThree;
        this.perkFour = pFour;
    }

    public String getName() {
        return name;
    }

    public String getPowerName() {
        return powerName;
    }

    public int getEarlyScore() {
        return earlyScore;
    }

    public int getLateScore() {
        return lateScore;
    }

    public int getGenStopScore() {
        return genStopScore;
    }

    public int getHuntScore() {
        return huntScore;
    }

    public int getCampScore() {
        return campScore;
    }

    public KillerAddon getAddonOne() {
        return addonOne;
    }

    public void setAddonOne(KillerAddon addonOne) {
        this.addonOne = addonOne;
    }

    public KillerAddon getAddonTwo() {
        return addonTwo;
    }

    public void setAddonTwo(KillerAddon addonTwo) {
        this.addonTwo = addonTwo;
    }

    public KillerPerk getPerkOne() {
        return perkOne;
    }

    public void setPerkOne(KillerPerk perkOne) {
        this.perkOne = perkOne;
    }

    public KillerPerk getPerkTwo() {
        return perkTwo;
    }

    public void setPerkTwo(KillerPerk perkTwo) {
        this.perkTwo = perkTwo;
    }

    public KillerPerk getPerkThree() {
        return perkThree;
    }

    public void setPerkThree(KillerPerk perkThree) {
        this.perkThree = perkThree;
    }

    public KillerPerk getPerkFour() {
        return perkFour;
    }

    public void setPerkFour(KillerPerk perkFour) {
        this.perkFour = perkFour;
    }
}
