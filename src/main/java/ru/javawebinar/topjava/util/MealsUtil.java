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

        final Map<LocalDate, Integer> tableOfCaloriesPerDay = new LinkedHashMap<>();
        final List<Meal> tableOfMealByInterval = new ArrayList<>();

        for (Meal meal : meals) {
                addMeal(tableOfCaloriesPerDay, meal.getDate(), meal.getCalories());

            if (isBetweenHalfOpen(meal.getTime(), startTime, endTime)) {
                tableOfMealByInterval.add(meal);
            }
        }

        System.out.println("1 tableOfCaloriesPerDay " + tableOfCaloriesPerDay);
        System.out.println("2 tableOfMealByInterval " + tableOfMealByInterval);

        return makeTableOfMealTo(tableOfMealByInterval, tableOfCaloriesPerDay, caloriesPerDay);
    }

    private static final void
    addMeal(Map<LocalDate, Integer> tableOfCaloriesPerDay, LocalDate date, Integer calories) {
        tableOfCaloriesPerDay.merge(date, calories, Integer::sum);
    }

    private static final List<MealTo>
    makeTableOfMealTo(
            List<Meal> tableOfMealByInterval,
            Map<LocalDate, Integer> tableOfCaloriesPerDay,
            Integer caloriesPerDay) {
        final ArrayList<MealTo> tableOfMealTo = new ArrayList<>();
        for (Meal meal : tableOfMealByInterval) {
            tableOfMealTo.add(new MealTo(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                    tableOfCaloriesPerDay.get(meal.getDate()) > caloriesPerDay));

            System.out.println("2 tableOfCaloriesPerDay.get " + tableOfCaloriesPerDay.get(meal.getDate()));
        }
        return tableOfMealTo;
    }

//    public static List<MealTo>
//    filteredByStreams(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
//        return null;
//    }
}