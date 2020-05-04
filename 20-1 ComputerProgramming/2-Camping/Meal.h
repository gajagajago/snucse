#ifndef MEAL_H
#define MEAL_H

#include "enum.h"

class Meal {
    private:
        MealType meal_type;
        Weight weight;
        bool is_cooking_required;
    
    public:
        Meal();
        Meal(MealType mealType, Weight w);
        MealType getMealType();
        Weight getWeight();
        bool isCookingRequired();
        int compareTo(Meal m);
        void print();
        bool equals(Meal m);
};

#endif