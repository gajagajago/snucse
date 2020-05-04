#ifndef STOREINVENTORY_H
#define STOREINVENTORY_H

#include <string>
#include <cstring>
using namespace std;

#include "Item.h"
#include "enum.h"

class StoreInventory {
    public:
        Item* item_list;
        StoreInventory();
        ItemType strToItemType(char* it);
        Weight strToWeight(char* w);
        void ReadFromFile(string filename);
};

#endif
