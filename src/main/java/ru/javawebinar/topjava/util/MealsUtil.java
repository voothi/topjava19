package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

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

        List<MealTo> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        //        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static final List<MealTo>
    filteredByCycles(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        final Map<LocalDate, Integer> mapOfCaloriesSumPerDay = new HashMap<>();
        final List<Meal> listOfMealByInterval = new ArrayList<>();

        for (Meal meal : meals) {
            mapOfCaloriesSumPerDay.merge(meal.getDate(), meal.getCalories(), Integer::sum);

            if (isBetweenHalfOpen(meal.getTime(), startTime, endTime)) {
                listOfMealByInterval.add(meal);
            }
        }

        return makeListOfMealTo(listOfMealByInterval, mapOfCaloriesSumPerDay, caloriesPerDay);
    }

    private static final List<MealTo>
    makeListOfMealTo(
            List<Meal> listOfMealByInterval,
            Map<LocalDate, Integer> mapOfCaloriesSumPerDay,
            Integer caloriesPerDay) {
        final List<MealTo> listOfMealTo = new ArrayList<>();

        for (Meal meal : listOfMealByInterval) {
            listOfMealTo.add(new MealTo(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                    mapOfCaloriesSumPerDay.get(meal.getDate()) > caloriesPerDay));
        }

        return listOfMealTo;
    }

//    public static List<MealTo>
//    filteredByStreams(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
//        return null;
//    }
}