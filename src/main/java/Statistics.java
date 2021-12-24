
public class Statistics {
    String country;
    String region;
    int happinessRank;
    double happinessScore;
    double lowerConfInterval;
    double upperConfInterval;
    double economy;
    double family;
    double health;
    double freedom;
    double trust;
    double generosity;
    double dystopiaResidual;

    public Statistics(String country, String region, int happinessRank, double happinessScore,
                      double lowerConfInterval, double upperConfInterval, double economy, double family,
                      double health, double freedom, double trust, double generosity, double dystopiaResidual) {
        this.country = country;
        this.region = region;
        this.happinessRank = happinessRank;
        this.happinessScore = happinessScore;
        this.lowerConfInterval = lowerConfInterval;
        this.upperConfInterval = upperConfInterval;
        this.economy = economy;
        this.family = family;
        this.health = health;
        this.freedom = freedom;
        this.trust = trust;
        this.generosity = generosity;
        this.dystopiaResidual = dystopiaResidual;
    }
}
