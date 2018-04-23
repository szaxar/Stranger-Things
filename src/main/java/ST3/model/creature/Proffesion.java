package ST3.model.creature;

import ST3.model.Statistics;

public enum Proffesion {

    KNIGHT,
    MAGE,
    PRIEST,
    SZAMAN;

    public Statistics getStatistics() {
        Statistics statistics = null;

        switch (this) {
            case KNIGHT:
                statistics = new Statistics(200, 20, 100, 70, 100, 70);
                return statistics;
            case MAGE:
                statistics = new Statistics(100, 200, 50, 90, 25, 25);
                return statistics;
            case PRIEST:
                statistics = new Statistics(100, 200, 40, 100, 15, 15);
                return statistics;
            case SZAMAN:
                statistics = new Statistics(150, 150, 75, 80, 75, 75);
                return statistics;
        }
        return null;
    }
}
