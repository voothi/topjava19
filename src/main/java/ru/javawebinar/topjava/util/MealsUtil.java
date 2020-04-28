package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.TimeUtil.isBetweenHalfOpen;

public class MealsUtil {
    public static void main(String[] args) {
        List<Meal> meals = Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        System.out.println("filteredByCycles: ");
        List<MealTo> mealsToCycles = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsToCycles.forEach(System.out::println);

        System.out.println("filteredByStreams: ");
        List<MealTo> mealsToStreams = filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsToStreams.forEach(System.out::println);
    }

    public static final List<MealTo>
    filteredByCycles(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        final Map<LocalDate, Integer> mapOfCaloriesSumPerDay = new HashMap<>();
        for (Meal meal : meals) {
            mapOfCaloriesSumPerDay.merge(meal.getDate(), meal.getCalories(), Integer::sum);
        }

        final List<MealTo> listOfMealTo = new ArrayList<>();
        for (Meal meal : meals) {
            if (isBetweenHalfOpen(meal.getTime(), startTime, endTime)) {
                listOfMealTo.add(new MealTo(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                        mapOfCaloriesSumPerDay.get(meal.getDate()) > caloriesPerDay));
            }
        }
        return listOfMealTo;
    }

    public static List<MealTo>
    filteredByStreams(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> mapOfCaloriesSumPerDay =
                meals.stream()
                        .collect(Collectors
                                .toMap(Meal::getDate, Meal::getCalories, Integer::sum));

        return meals.stream()
                .filter(meal -> isBetweenHalfOpen(meal.getTime(), startTime, endTime))
                .map(meal -> new MealTo(meal.getDateTime(),
                        meal.getDescription(),
                        meal.getCalories(),
                        mapOfCaloriesSumPerDay.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }
}