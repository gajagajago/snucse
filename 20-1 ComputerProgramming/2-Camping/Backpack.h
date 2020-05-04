#ifndef BACKPACK_H
#define BACKPACK_H

#include "Meal.h"
#include "Item.h"
#include "StoreInventory.h"
#include "CustomerRequirement.h"

class Backpack {
    private:
        Meal* meals;
        int meal_length;
        Item* items;
        int item_length;
        Item** zones;
        Item* storeInventory;

    public:
        Backpack();
        void assignMeals(CustomerRequirement customerRequirement);
        void assignItem(CustomerRequirement customerRequirement);
        void packBackpack();
        void addItem(Item item);
        void removeItem(int i);
        void removeItem(Item item);
        void print();
        Meal* getMeals();
        void setMeals(Meal* m);
        int getMealLength();
        Item* getItems();
        void setItems(Item* it);
        int getItemLength();
        Item** getZones();
        void setZones(Item** z);
        Item* getStoreInventory();
        void setStoreInventory(Item* s);
};

#endif
