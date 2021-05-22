package net.realrain.chap5;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PracticeProblems {
    Trader raoul = new Trader("Raoul", "Cambridge");
    Trader mario = new Trader("Mario", "Milan");
    Trader alan = new Trader("Alan", "Cambridge");
    Trader brian = new Trader("Brian", "Cambridge");

    List<Transaction> transactions = Arrays.asList(
            new Transaction(brian, 2011, 300),
            new Transaction(raoul, 2012, 1000),
            new Transaction(raoul, 2011, 400),
            new Transaction(mario, 2012, 710),
            new Transaction(mario, 2012, 700),
            new Transaction(alan, 2012, 950)
    );

    @Test
    void problem_1() {
        List<Transaction> transactionsOn2011 = this.transactions.stream()
                .filter(tr -> tr.getYear() == 2011)
                .sorted(Comparator.comparing(Transaction::getValue))
                .collect(Collectors.toList());

        System.out.println(transactionsOn2011);
    }

    @Test
    void problem_2() {
        List<String> cities = transactions.stream()
                .map(Transaction::getTrader)
                .map(Trader::getCity)
                .distinct()
                .collect(Collectors.toList());

        System.out.println(cities);
    }

    @Test
    void problem_3() {
        List<Trader> traders = transactions.stream()
                .map(Transaction::getTrader)
                .filter(trader -> "Cambridge".equals(trader.getCity()))
                .distinct()
                .sorted(Comparator.comparing(Trader::getName))
                .collect(Collectors.toList());

        System.out.println(traders);
    }

    @Test
    void problem_4() {
        List<String> names = transactions.stream()
                .map(Transaction::getTrader)
                .distinct()
                .map(Trader::getName)
                .sorted(String::compareTo)
                .collect(Collectors.toList());

        System.out.println(names);
    }

    @Test
    void problem_5() {
        boolean isThereTraderInMilan = transactions.stream().anyMatch(tr -> "Milan".equals(tr.getTrader().getCity()));
        System.out.println(isThereTraderInMilan);
    }

    @Test
    void problem_6() {
        List<Integer> values = transactions.stream()
                .filter(tr -> "Cambridge".equals(tr.getTrader().getCity()))
                .map(Transaction::getValue)
                .collect(Collectors.toList());

        System.out.println(values);
    }

    @Test
    void problem_7() {
        Integer max = transactions.stream()
                .map(Transaction::getValue)
                .reduce(Integer.MIN_VALUE, Integer::max);

        System.out.println(max);
    }

    @Test
    void problem_8() {
        Integer min = transactions.stream()
                .map(Transaction::getValue)
                .reduce(Integer.MAX_VALUE, Integer::min);

        System.out.println(min);

//        transactions.stream()
//                .min(Comparator.comparing(Transaction::getValue))

    }
}
