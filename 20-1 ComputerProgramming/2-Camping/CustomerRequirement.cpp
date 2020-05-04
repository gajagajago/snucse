#include "CustomerRequirement.h"

CustomerRequirement::CustomerRequirement(DaysOnCamp daysOnCamp, Weight preferredItemWeight, Weight preferredMealWeight) {
    days_on_camp = daysOnCamp;
    preferred_item_weight = preferredItemWeight;
    preferred_meal_weight = preferredMealWeight;
}

DaysOnCamp CustomerRequirement::getDaysOnCamp() {
    return days_on_camp;
}

void CustomerRequirement::setDaysOnTrail(DaysOnCamp daysOnCamp) {
    days_on_camp = daysOnCamp;
}

Weight CustomerRequirement::getPreferredItemWeight() {
    return preferred_item_weight;
}

void CustomerRequirement::setPreferredItemWeight(Weight preferredItemWeight) {
    preferred_item_weight = preferredItemWeight;
}

Weight CustomerRequirement::getPreferredMealWeight() {
    return preferred_meal_weight;
}

void CustomerRequirement::setPreferredMealWeight(Weight preferredMealWeight) {
    preferred_meal_weight = preferredMealWeight;
}