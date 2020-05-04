#include <iostream>
#include <malloc.h>
#include <algorithm>
#include <vector>
using namespace std;
#include "enum.h"
#include "Item.h"
#include "Meal.h"
#include "StoreInventory.h"
#include "CustomerRequirement.h"
#include "Backpack.h"

void testNoError() {
    try { //test no error
        Backpack b;
        b.assignMeals(CustomerRequirement(ONE, LOW, MEDIUM));
        b.assignItem(CustomerRequirement(ONE, LOW, MEDIUM));
        b.packBackpack();
        b.addItem(Item(COOKING, HIGH));
        b.removeItem(Item(COOKING, HIGH));
        b.removeItem(1);
        cout << "Test NoError: Passed" << endl;
    } catch (...) {
        cout << "Test NoError: Failed" << endl;
    }
}

void testConstructor() {
    try { //test constructor
        Backpack b;
        StoreInventory st;
        Item* bStore = b.getStoreInventory();
        Item* origStore = st.item_list;

        int len1 = _msize(bStore) / sizeof(Item);
        int len2 = _msize(origStore) / sizeof(Item);
        
        if (len1 != len2) {
            throw -1;
        }

        cout << "Test Constructor: Passed" << endl;
    } catch (...) {
        cout << "Test Constructor: Failed" << endl;
    }
}

void testAddItem() {
    try { //test addItem
        Backpack b;
        Item it1(SLEEPING_BAG, HIGH);
        Item it2(CLOTHING, HIGH);
        b.addItem(it1);
        if (!(b.getItems()[0].equals(it1) && b.getItemLength() == 1)) {
            throw -1;
        } 
        b.addItem(it1);
        if (!(b.getItems()[0].equals(it1) && b.getItems()[1].equals(it1)
            && b.getItemLength() == 2)) {
            throw -1;
        }

        b.addItem(it2);
        if (!(b.getItems()[0].equals(it1) 
            && b.getItems()[1].equals(it1)
            && b.getItems()[2].equals(it2)
            && b.getItemLength() == 3)) {
            throw -1;
        }

        cout << "Test addItem: Passed" << endl;
    } catch (...) {
        cout << "Test addItem: Failed" << endl;       
    }
}

void testRemoveItemInt() {
    Backpack b;
    Item it1(SLEEPING_BAG, HIGH);
    Item it2(LURE, HIGH);
    Item it3(CLOTHING, HIGH);
    Item it4(TENT, HIGH);
    Item it5(FISHING_ROD, HIGH);
    Item it6(WATER, HIGH);
    Item it7(COOKING, HIGH);

    try {
        b.removeItem(2);
    } catch (...) {
        cout << "Test removeItem int: Failed" << endl;
        cout << "\tFailed handling null array" << endl;
        return;
    }

    b.addItem(it1);
    b.addItem(it2);
    b.removeItem(0);

    try {
        if (!(b.getItemLength() == 1 && b.getItems()[0].equals(it2))) {
            throw -1;
        }
    } catch (...) {
        cout << "Test removeItem int: Failed" << endl;
        cout << "\tFailed removing correct Item from items array (1st)" << endl;
        return;
    }

    b.addItem(it1);
    b.addItem(it3);
    b.addItem(it4);
    b.addItem(it5);
    b.addItem(it6);
    b.addItem(it7);
    b.removeItem(4);
    Item itemArr[7] = {it2, it1, it3, it4, it6, it7};

    try {
        for(int i = 0; i < b.getItemLength(); i++) {
            if (!b.getItems()[i].equals(itemArr[i])) {
                throw -1;
            }
        }
    } catch (...) {
        cout << "Test removeItem int: Failed" << endl;
        cout << "\tFailed removing correct Item from items array (2nd)" << endl;
        return;
    }

    cout << "Test removeItem int: Passed" << endl;
}

void testRemoveItemItem() {
    Backpack b;
    Item it1(SLEEPING_BAG, HIGH);
    Item it2(LURE, HIGH);
    Item it3(CLOTHING, HIGH);
    Item it4(TENT, HIGH);
    Item it5(FISHING_ROD, HIGH);
    Item it6(WATER, HIGH);
    Item it7(COOKING, HIGH);

    try {
        b.removeItem(it1);
    } catch (...) {
        cout << "Test removeItem Item: Failed" << endl;
        cout << "\tFailed handling null array" << endl;
        return;
    }

    b.addItem(it1);
    b.addItem(it2);
    b.removeItem(it1);

    try {
        if (!(b.getItemLength() == 1 && b.getItems()[0].equals(it2))) {
            throw -1;
        }
    } catch (...) {
        cout << "Test removeItem Item: Failed" << endl;
        cout << "\tFailed removing correct Item from items array (1st)" << endl;
        return;
    }

    b.addItem(it1);
    b.addItem(it3);
    b.addItem(it4);
    b.addItem(it5);
    b.addItem(it6);
    b.addItem(it7);
    b.removeItem(it5);
    Item itemArr[7] = {it2, it1, it3, it4, it6, it7};

    try {
        for(int i = 0; i < b.getItemLength(); i++) {
            if (!b.getItems()[i].equals(itemArr[i])) {
                throw -1;
            }
        }
    } catch (...) {
        cout << "Test removeItem Item: Failed" << endl;
        cout << "\tFailed removing correct Item from items array (2nd)" << endl;
        return;
    }

    cout << "Test removeItem Item: Passed" << endl;

}

void testPackBackPack() {
    try { 
        Backpack b;
        Item itemArr[] = {
            Item(TENT, HIGH), 
            Item(CLOTHING, HIGH), 
            Item(COOKING, HIGH),
            Item(LURE, HIGH),
            Item(SLEEPING_BAG, HIGH),
            Item(FISHING_ROD, HIGH),
            Item(WATER, HIGH)
        };

        b.addItem(itemArr[0]);
        b.addItem(itemArr[1]);
        b.addItem(itemArr[2]);
        b.addItem(itemArr[3]);
        b.addItem(itemArr[4]);
        b.addItem(itemArr[5]);
        b.addItem(itemArr[6]);
        b.packBackpack();

        Item** testZone = b.getZones();
        
        if (!testZone[0][0].equals(itemArr[3])) {
            throw -1;
        } else if (!testZone[1][0].equals(itemArr[5])) {
            throw -1;
        } else if (!testZone[2][0].equals(itemArr[1])) {
            throw -1;
        } else if (!testZone[3][0].equals(itemArr[2])) {
            throw -1;
        } else if (!testZone[3][1].equals(itemArr[6])) {
            throw -1;
        } else if (!testZone[4][0].equals(itemArr[4])) {
            throw -1;
        } else if (!testZone[4][1].equals(itemArr[0])) {
            throw -1;
        }
        cout << "Test packBackpack: Passed" << endl;
    } catch (...) {
        cout << "Test packBackpack: Failed" << endl;
    }

}

bool sortMeal(Meal& a, Meal&b){
    if(a.getMealType()<b.getMealType()) return true;
    if(a.getMealType()>b.getMealType()) return false;
    if(a.getWeight()<b.getWeight()) return true;
    if(a.getWeight()>b.getWeight()) return false;

    return false;
}

void testAssignMeals() {
    CustomerRequirement cr(ONE, LOW, LOW);
    CustomerRequirement cr2(ONE, LOW, HIGH);
    CustomerRequirement cr3(TWO, LOW, MEDIUM);
    CustomerRequirement cr4(THREE, LOW, HIGH);
    Backpack b;
    vector<Meal> mv;
    Meal* meals = new Meal[2];

    b.assignMeals(cr);

    try {
        meals[0] = Meal(LUNCH, LOW);
        meals[1] = Meal(SNACK, LOW);

        for(int i = 0; i < b.getMealLength(); i++) {
            mv.push_back(b.getMeals()[i]);
        }

        sort(mv.begin(), mv.end(),sortMeal);

        for(int i = 0; i < mv.size(); i++) {
            if (!mv.at(i).equals(meals[i])) {
                throw -1;
            }
        }

        mv.clear();
    } catch (...) {
        cout << "Test assignMeals: Failed" << endl;
        cout << "\tFailed assigning correct Meal objects (1st)" << endl;
        return;
    }

    b.assignMeals(cr2);

    try {
        meals[0] = Meal(LUNCH, HIGH);
        meals[1] = Meal(SNACK, HIGH);

        for(int i = 0; i < b.getMealLength(); i++) {
            mv.push_back(b.getMeals()[i]);
        }

        sort(mv.begin(), mv.end(),sortMeal);

        for(int i = 0; i < mv.size(); i++) {
            if (!mv.at(i).equals(meals[i])) {
                throw -1;
            }
        }

        mv.clear();
    } catch (...) {
        cout << "Test assignMeals: Failed" << endl;
        cout << "\tFailed assigning correct Meal objects (2nd)" << endl;
        return;
    }

    b.assignMeals(cr3);

    try {
        meals = new Meal[6];
        meals[0] = Meal(BREAKFAST, MEDIUM);
        meals[1] = Meal(LUNCH, MEDIUM);
        meals[2] = Meal(LUNCH, MEDIUM);
        meals[3] = Meal(DINNER, MEDIUM);
        meals[4] = Meal(SNACK, MEDIUM);
        meals[5] = Meal(SNACK, MEDIUM);

        for(int i = 0; i < b.getMealLength(); i++) {
            mv.push_back(b.getMeals()[i]);
        }

        sort(mv.begin(), mv.end(),sortMeal);

        for(int i = 0; i < mv.size(); i++) {
            if (!mv.at(i).equals(meals[i])) {
                throw -1;
            }
        }

        mv.clear();
    } catch (...) {
        cout << "Test assignMeals: Failed" << endl;
        cout << "\tFailed assigning correct Meal objects (3rd)" << endl;
        return;
    }

    b.assignMeals(cr4);

    try {
        meals = new Meal[10];
        meals[0] = Meal(BREAKFAST, HIGH);
        meals[1] = Meal(BREAKFAST, HIGH);
        meals[2] = Meal(LUNCH, HIGH);
        meals[3] = Meal(LUNCH, HIGH);
        meals[4] = Meal(LUNCH, HIGH);
        meals[5] = Meal(DINNER, HIGH);
        meals[6] = Meal(DINNER, HIGH);
        meals[7] = Meal(SNACK, HIGH);
        meals[8] = Meal(SNACK, HIGH);
        meals[9] = Meal(SNACK, HIGH);

        for(int i = 0; i < b.getMealLength(); i++) {
            mv.push_back(b.getMeals()[i]);
        }

        sort(mv.begin(), mv.end(),sortMeal);

        for(int i = 0; i < mv.size(); i++) {
            if (!mv.at(i).equals(meals[i])) {
                throw -1;
            }
        }

        mv.clear();
    } catch (...) {
        cout << "Test assignMeals: Failed" << endl;
        cout << "\tFailed assigning correct Meal objects (4th)" << endl;
        return;
    }
    
    cout << "Test assignMeals: Passed" << endl;

}

bool sortItem(Item&a, Item&b){
    if(a.getItemType()<b.getItemType()) return true;
    if(a.getItemType()>b.getItemType()) return false;
    if(a.getWeight()<b.getWeight()) return true;
    if(a.getWeight()>b.getWeight()) return false;

    return false;
}
void testAssignItem() {
    CustomerRequirement cr(ONE, MEDIUM, LOW);
    CustomerRequirement cr2(TWO, HIGH, MEDIUM);
    CustomerRequirement cr3(THREE, HIGH, HIGH);
    Backpack b;
    vector<Item> mv;
    Item* items = new Item[4];

    b.assignItem(cr);

    try {
        items[0] = Item(LURE, MEDIUM);
        items[1] = Item(CLOTHING, MEDIUM);
        items[2] = Item(FISHING_ROD, MEDIUM);
        items[3] = Item(WATER, HIGH);

        for(int i = 0; i < b.getItemLength(); i++) {
            mv.push_back(b.getItems()[i]);
        }

        sort(mv.begin(), mv.end(), sortItem);

        for(int i = 0; i < mv.size(); i++) {
            if (!mv.at(i).equals(items[i])) {
                throw -1;
            }
        }

        mv.clear();
    } catch (...) {
        cout << "Test assignItem: Failed" << endl;
        cout << "\tFailed assigning correct Item objects (1st)" << endl;
        return;
    }

    b.assignItem(cr2);

    try {
        items = new Item[6];
        items[0] = Item(SLEEPING_BAG, MEDIUM);
        items[1] = Item(TENT, HIGH);
        items[2] = Item(LURE, HIGH);
        items[3] = Item(CLOTHING, HIGH);
        items[4] = Item(FISHING_ROD, HIGH);
        items[5] = Item(WATER, HIGH);

        for(int i = 0; i < b.getItemLength(); i++) {
            mv.push_back(b.getItems()[i]);
        }

        sort(mv.begin(), mv.end(),sortItem);

        for(int i = 0; i < mv.size(); i++) {
            if (!mv.at(i).equals(items[i])) {
                throw -1;
            }
        }

        mv.clear();
    } catch (...) {
        cout << "Test assignItem: Failed" << endl;
        cout << "\tFailed assigning correct Item objects (2nd)" << endl;
        return;
    }

    b.assignItem(cr3);

    try {
        items = new Item[7];
        items[0] = Item(SLEEPING_BAG, MEDIUM);
        items[1] = Item(TENT, HIGH);
        items[2] = Item(LURE, HIGH);
        items[3] = Item(CLOTHING, HIGH);
        items[4] = Item(FISHING_ROD, HIGH);
        items[5] = Item(COOKING, HIGH);
        items[6] = Item(WATER, HIGH);

        for(int i = 0; i < b.getItemLength(); i++) {
            mv.push_back(b.getItems()[i]);
        }

        sort(mv.begin(), mv.end(),sortItem);
        
        for(int i = 0; i < mv.size(); i++) {
            if (!mv.at(i).equals(items[i])) {
                throw -1;
            }
        }

        mv.clear();
    } catch (...) {
        cout << "Test assignItem: Failed" << endl;
        cout << "\tFailed assigning correct Item objects (3rd)" << endl;
        return;
    }

    cout << "Test assignMeals: Passed" << endl;
}

void testPrint() {
    CustomerRequirement cr(ONE, HIGH, LOW);
    CustomerRequirement cr2(TWO, HIGH, HIGH);
    CustomerRequirement cr3(THREE, HIGH, LOW);
    Backpack b;
    Backpack b2;
    Backpack b3;

    cout << "Four items must be printed" << endl;
    b.assignItem(cr);
    b.packBackpack();
    b.print();
    cout << endl;

    cout << "Seven items must be printed" << endl;
    b2.assignItem(cr2);
    b2.packBackpack();
    b2.print();
    cout << endl;

    cout << "Six items must be printed" << endl;
    b3.assignItem(cr3);
    b3.packBackpack();
    b3.print();
    cout << endl;
}

int main() {
    cout << "******************************" << endl;
    cout << "Starting Test :) \n" << endl;
    testNoError();
    testConstructor();
    testAddItem();
    testRemoveItemInt();
    testRemoveItemItem();
    testPackBackPack();
    testAssignMeals();
    testAssignItem();
    cout << "\nTest print (check if correct)" << endl;
    testPrint();

    cout << "\nEnd of Test" << endl;
}
