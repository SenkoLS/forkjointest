package ru.senkols;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Person> persons = Arrays.asList(
                new Person("Leo", 37),
                new Person("Andry", 55),
                new Person("Igor", 26),
                new Person("Neo", 42),
                new Person("Michail", 57)
        );

        Double personMap = persons.stream()
                .collect(Collectors.averagingInt(p->p.age));

        System.out.println(personMap);
    }
}
