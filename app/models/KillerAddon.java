package models;

import com.avaje.ebean.Model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class KillerAddon extends Model {

    // Ebean Finder utility
    public static Finder<Long, KillerAddon> find = new Finder<>(KillerAddon.class);

    @Id
    @GeneratedValue
    private long id;
    @Column(unique = true, nullable = false)
    private String name;
    // How much kill potential does the addon have right away
    @Column(name = "early_score", nullable = false)
    private int earlyScore;
    // How much kill potential is gained as the game goes by
    @Column(name = "late_score", nullable = false)
    private int lateScore;
    // How much does this addon slow generator progress (no addons)
    @Column(name = "gen_stop_score", nullable = false)
    private int genStopScore;
    // How well does this addon do when hunting survivors
    @Column(name = "hunt_score", nullable = false)
    private int huntScore;
    // How good is the addon for camping hooks/gens/totems
    @Column(name = "camp_score", nullable = false)
    private int campScore;

    /**
     * The default constructor
     */
    public KillerAddon() {
        // Empty default constructor
    }

    /**
     * The database centric constructor
     *
     * @param name         the name of the addon
     * @param earlyScore   the Addon's early game score
     * @param lateScore    the Addon's late game score
     * @param genStopScore the Addon's generator stopping score
     * @param huntScore    the Addon's hunting score
     * @param campScore    the Addon's camping score
     */
    public KillerAddon(final String name, final int earlyScore, final int lateScore, final int genStopScore,
                       final int huntScore, final int campScore) {
        this.name = name;
        this.earlyScore = earlyScore;
        this.lateScore = lateScore;
        this.genStopScore = genStopScore;
        this.huntScore = huntScore;
        this.campScore = campScore;
    }

    public String getName() {
        return name;
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
}
