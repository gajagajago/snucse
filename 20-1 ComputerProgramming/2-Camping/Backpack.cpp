#include "Backpack.h"
#include <vector>
#include <iostream>
using namespace std;

Backpack::Backpack() {
    //initialize storeInventory with item_list
    StoreInventory store;
    storeInventory = store.item_list;

    //create two dimensional(5*2) zones
    int sizeX = 5;
    int sizeY = 2;
    zones = new Item*[sizeX];
    for(int i = 0; i < sizeX; ++i) {        
        zones[i] = new Item[sizeY];
    }

    meals, items = NULL;    
    meal_length, item_length = 0;           
}

void Backpack::assignMeals(CustomerRequirement customerRequirement) {
    DaysOnCamp days = customerRequirement.getDaysOnCamp();
    Weight preferred_m_weight = customerRequirement.getPreferredMealWeight();

    Meal lunch(LUNCH, preferred_m_weight);
    Meal snack(SNACK, preferred_m_weight);
    Meal dinner(DINNER, preferred_m_weight);
    Meal breakfast(BREAKFAST, preferred_m_weight);

    switch (days) {
        case ONE:
            meals = new Meal[2];
            meal_length = 2;

            meals[0] = lunch;
            meals[1] = snack;
            break;
        
        case TWO:
            meals = new Meal[6];
            meal_length = 6;

            meals[0] = lunch;
            meals[1] = snack;
            meals[2] = dinner;
            meals[3] = breakfast;
            meals[4] = lunch;
            meals[5] = snack;
            break;
        
        case THREE:
            meals = new Meal[10];
            meal_length = 10;

            meals[0] = lunch;
            meals[1] = snack;
            meals[2] = dinner;
            meals[3] = breakfast;
            meals[4] = lunch;
            meals[5] = snack;
            meals[6] = dinner;
            meals[7] = breakfast;
            meals[8] = lunch;
            meals[9] = snack;
            break;
    } 
}

void Backpack::assignItem(CustomerRequirement customerRequirement) {
    DaysOnCamp days = customerRequirement.getDaysOnCamp();
    Weight preferred_i_weight = customerRequirement.getPreferredItemWeight();
    Weight preferred_m_weight = customerRequirement.getPreferredMealWeight();

    Item water(WATER, HIGH);
    Item clothing(CLOTHING, preferred_i_weight);
    Item fishing_rod(FISHING_ROD, preferred_i_weight);
    Item lure(LURE, preferred_i_weight);
    Item sleeping_bag(SLEEPING_BAG, MEDIUM);
    Item tent(TENT, preferred_i_weight);
    Item cooking(COOKING, preferred_i_weight);

    if(days == ONE) {
        item_length = 4;
        items = new Item[item_length];
    }
    else {
        if(preferred_m_weight == HIGH){
            item_length = 7;
            items = new Item[item_length];
            items[6] = cooking;
        }
        else {
            item_length = 6;
            items = new Item[item_length];
        }
        items[4] = sleeping_bag;
        items[5] = tent;
    }
    items[0] = clothing;
    items[1] = fishing_rod;
    items[2] = lure;
    items[3] = water;

    //assign and check
    for(int i = 0; i < item_length; ++i) {
        bool isStored;
        for(int j = 0; j < 42; ++j) {   //hardcoding-42 for number of items in available_items.txt
            if(items[i].equals(storeInventory[j])) {
                isStored = true;
                break;
            }
        }
        if(!isStored) {
            printf("Such item is not stored in store inventory.");
        }
    } 
}   

void Backpack::packBackpack() {
    for(int i = 0; i < item_length; ++i) {
        ItemType it = items[i].getItemType();

        switch (it) {
            case SLEEPING_BAG:
                zones[4][0] = items[i];
                break;
            case TENT:
                zones[4][1] = items[i];
                break;
            case LURE:
                zones[0][0] = items[i];
                break;
            case CLOTHING:
                zones[2][0] = items[i];
                break;
            case FISHING_ROD:
                zones[1][0] = items[i];
                break;
            case COOKING:
                zones[3][0] = items[i];
                break;
            case WATER:
                zones[3][1] = items[i];
                break;
        }
    }
}

void Backpack::addItem(Item item) { 
    if(items == NULL) {
        items = new Item[1];
        items[0] = item;
    }
    else {
        Item* temp_items = new Item[item_length];
        for(int i = 0; i < item_length ; ++i) {
            temp_items[i] = items[i];
        }
        
        items = new Item[item_length+1];
        for(int i = 0; i < item_length; ++i) {
            items[i] = temp_items[i];
        }
        items[item_length] = item;
    }
    ++item_length;
}

void Backpack::removeItem(int i) {
    if(i >= item_length) {return;}

    if(item_length == 1) {
        items = NULL;
        item_length = 0;
        return;
    }

    Item* temp_items = new Item[item_length];

    for (int j = 0; j < item_length; ++j) {
        temp_items[j] = items[j];
    }

    items = new Item[item_length - 1];

    for (int j = 0; j < i; ++j) {
        items[j] = temp_items[j];
    }

    for (int j = i; j < item_length - 1; ++j) {
        items[j] = temp_items[j + 1];
    }

    --item_length;
}

void Backpack::removeItem(Item item) {
    if (item_length == 0) {return;}

    vector<Item> temp_item;

    for(int i = 0; i < item_length; ++i) {
        temp_item.push_back(items[i]);
    }

    for(vector<Item>::iterator iter = temp_item.begin(); iter != temp_item.end(); ++iter) {
        if(iter->equals(item)) {
            temp_item.erase(iter);

            items = new Item[item_length - 1];
            copy(temp_item.begin(), temp_item.end(), items);

            --item_length;
            break;
        }
    }
}

void Backpack::print() {
    Item skip_case(SLEEPING_BAG, LOW);

    for(int i = 0; i < 5; ++i) {
        cout << "Zone " << i << ":\n";
        for (int j = 0; j < 2; ++j)
        {
            if(zones[i][j].equals(skip_case)) {
                continue;
            }
            cout << "\t";
            zones[i][j].print();
        }
    }
}

Meal* Backpack::getMeals() {
    return meals;
}

void Backpack::setMeals(Meal* m) {
    meals = m;
}

int Backpack::getMealLength() {
    return meal_length;
}

Item* Backpack::getItems() {
    return items;
}

void Backpack::setItems(Item* it) {
    items = it;
}

int Backpack::getItemLength() {
    return item_length;
}

Item** Backpack::getZones() {
    return zones;
}

void Backpack::setZones(Item** z) {
    zones = z;
}

Item* Backpack::getStoreInventory() {
    return storeInventory;
}

void Backpack::setStoreInventory(Item* s) {
    storeInventory = s;
}
