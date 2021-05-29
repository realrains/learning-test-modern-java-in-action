package net.realrain.chap19;

public class TrainJourney {
    public int price;
    public TrainJourney onward;

    public TrainJourney(int price, TrainJourney onward) {
        this.price = price;
        this.onward = onward;
    }

    // has side effect, 'from' is changed
    static TrainJourney link(TrainJourney from, TrainJourney to) {
        if (from == null) {
            return to;
        }

        TrainJourney t = from;
        while (t.onward != null) {
            t = t.onward;
        }
        t.onward = to;
        return from;
    }

    static TrainJourney append(TrainJourney from, TrainJourney to) {
        return from == null ? to : new TrainJourney(from.price, append(from.onward, to));
    }
}
