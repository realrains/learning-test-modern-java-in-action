package net.realrain.chap11;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class OptionalTest {

    @Test
    void optionalBasic() {
        Optional<Car> optionalCar = Optional.of(new Car());
        Optional<Car> emptyCar = Optional.empty();

        Car nullCar = null;
        Optional<Car> nullableCar = Optional.ofNullable(nullCar);
    }

    @Test
    void optionalMap() {
        Optional<Person> person = Optional.of(new Person());
        String insuranceName = person.flatMap(Person::getCar)
                .flatMap(Car::getInsurance)
                .map(Insurance::getName)
                .orElse("Unknown");
        assertEquals("Unknown", insuranceName);
    }

    @Test
    void optionalStream() {
        List<Person> people = List.of(new Person(), new Person());

        Set<String> insuranceNames = people.stream()
                .map(Person::getCar)
                .map(optCar -> optCar.flatMap(Car::getInsurance))
                .map(optInsurance -> optInsurance.map(Insurance::getName))
                .flatMap(Optional::stream) // filter(Optional::isPresent).map(Optional::get)
                .collect(Collectors.toSet());
    }

    @Test
    void optionalComposition() {
        Optional<Person> person = Optional.of(new Person());
        Optional<Car> car = Optional.empty();
        BiConsumer<Person, Car> doSomething = (p, c) -> System.out.println(p.toString() + " " + c.toString());

        if (person.isPresent() && car.isPresent()) {
            doSomething.accept(person.get(), car.get());
        }

        person.ifPresent(p -> car.ifPresent(c -> doSomething.accept(p, c)));
    }

    @Test
    void optionalFilter() {
        Optional<Insurance> insurance = Optional.of(new Insurance());
        insurance
            .filter(i -> i.getName().equals("CambridgeInsurance"))
            .ifPresent(i -> System.out.println(i.getName()));
    }

}
