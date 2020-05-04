#ifndef CUSTOMERREQUIREMENT_H
#define CUSTOMERREQUIREMENT_H

#include "enum.h"

class CustomerRequirement {
    private:
        DaysOnCamp days_on_camp;
        Weight preferred_item_weight;
        Weight preferred_meal_weight;

    public:
        CustomerRequirement(DaysOnCamp daysOnCamp, Weight preferredItemWeight, Weight preferredMealWeight);
        DaysOnCamp getDaysOnCamp();
        void setDaysOnTrail(DaysOnCamp daysOnCamp);
        Weight getPreferredItemWeight();
        void setPreferredItemWeight(Weight preferredItemWeight);
        Weight getPreferredMealWeight();
        void setPreferredMealWeight(Weight preferredMealWeight);
};

#endif