#include "Meal.h"

#include <iostream>
using namespace std;

Meal::Meal() {
    meal_type = BREAKFAST;
    weight = LOW;
    is_cooking_required = ((meal_type == DINNER || meal_type == BREAKFAST) && weight == HIGH);
}

Meal::Meal(MealType mealType, Weight w) {
    meal_type = mealType;
    weight = w;
    is_cooking_required = ((meal_type == DINNER || meal_type == BREAKFAST) && weight == HIGH);
}

MealType Meal::getMealType() {
    return meal_type;
}

Weight Meal::getWeight() {
    return weight;
}

bool Meal::isCookingRequired() {
    return is_cooking_required;
}

int Meal::compareTo(Meal m) {
    if (meal_type == m.getMealType()) return 0;
    if (meal_type == DINNER) return 1;
    if (m.getMealType() == DINNER) return -1;
    if (meal_type == LUNCH) return 1;
    if (m.getMealType() == LUNCH) return -1;
    if (meal_type == BREAKFAST) return 1;
    if (m.getMealType() == BREAKFAST) return -1;
    return -1;
}

void Meal::print() {
    const char* mt;
    const char* w;
    const char* cook;
    if(meal_type == SNACK) {
        mt = "SNACK";
    } else if(meal_type == BREAKFAST) {
        mt = "BREAKFAST";
    } else if(meal_type == LUNCH) {
        mt = "LUNCH";
    } else if(meal_type == DINNER) {
        mt = "DINNER";
    }

    if(weight == LOW) {
        w = "LOW";
    } else if(weight == MEDIUM) {
        w = "MEDIUM";
    } else if(weight == HIGH) {
        w = "HIGH";
    }

    if(is_cooking_required) {
        cook = "Yes";
    } else {
        cook = "No";
    }

    cout << "Meal{" <<
        "Meal type: " << mt << ", Weight: " << w << ", Cooking required? " << cook << "}" << endl;
}

bool Meal::equals(Meal m) {
    return isCookingRequired() == m.isCookingRequired() &&
        getMealType() == m.getMealType() &&
        getWeight() == m.getWeight();
}
