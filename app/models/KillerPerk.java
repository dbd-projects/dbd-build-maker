package models;

import com.avaje.ebean.Model;

import javax.persistence.*;

@Entity
@Table(name = "killer_perks")
public class KillerPerk extends Model {

    // Ebean Finder utility
    public static Finder<Long, KillerPerk> find = new Finder<>(KillerPerk.class);

    @Id
    @GeneratedValue
    private long id;
    @Column(unique = true, nullable = false)
    private String name;
    // How much kill potential does the perk have right away
    @Column(name = "early_score", nullable = false)
    private int earlyScore;
    // How much kill potential is gained as the game goes by
    @Column(name = "late_score", nullable = false)
    private int lateScore;
    // How much does this perk slow generator progress (no perks)
    @Column(name = "gen_stop_score", nullable = false)
    private int genStopScore;
    // How well does this perk do when hunting survivors
    @Column(name = "hunt_score", nullable = false)
    private int huntScore;
    // How good is the perk for camping hooks/gens/totems
    @Column(name = "camp_score", nullable = false)
    private int campScore;

    /**
     * The default constructor
     */
    public KillerPerk() {
        // Empty default constructor
    }

    /**
     * The database centric constructor
     *
     * @param name         the name of the perk
     * @param earlyScore   the Perk's early game score
     * @param lateScore    the Perk's late game score
     * @param genStopScore the Perk's generator stopping score
     * @param huntScore    the Perk's hunting score
     * @param campScore    the Perk's camping score
     */
    public KillerPerk(final String name, final int earlyScore, final int lateScore, final int genStopScore,
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
