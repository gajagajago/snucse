#include <iostream>
using namespace std;

#include "Item.h"

Item::Item() {
    item_type = SLEEPING_BAG;
    weight = LOW;
}

Item::Item(ItemType itemType, Weight weight) {
    item_type = itemType;
    this->weight = weight;  
}

ItemType Item::getItemType() {
    return item_type;
}

void Item::setItemType(ItemType itemType) {
    item_type = itemType;
}

Weight Item::getWeight() {
    return weight;
}

void Item::setWeight(Weight weight) {
    this->weight = weight;
}

int Item::compareTo(Item item) {
    if (item_type == item.getItemType()) return 0;
    if (item_type == SLEEPING_BAG) return 1;
    if (item.getItemType() == SLEEPING_BAG) return -1;
    if (item_type == TENT) return 1;
    if (item.getItemType() == TENT) return -1;
    if (item_type == LURE) return 1;
    if (item.getItemType() == LURE) return -1;
    if (item_type == CLOTHING) return 1;
    if (item.getItemType() == CLOTHING) return -1;
    if (item_type == FISHING_ROD) return 1;
    if (item.getItemType() == FISHING_ROD) return -1;
    if (item_type == COOKING) return 1;
    if (item.getItemType() == COOKING) return -1;
    if (item_type == WATER) return 1;
    if (item.getItemType() == WATER) return -1;
    return -1;
}

void Item::print() {
    const char* it;
    const char* w;
    if(item_type == SLEEPING_BAG) {
        it = "SLEEPING_BAG";
    } else if(item_type == TENT) {
        it = "TENT";
    } else if(item_type == LURE) {
        it = "LURE";
    } else if(item_type == CLOTHING) {
        it = "CLOTHING";
    } else if(item_type == FISHING_ROD) {
        it = "FISHING_ROD";
    } else if(item_type == COOKING) {
        it = "COOKING";
    } else if(item_type == WATER) {
        it = "WATER";
    }

    if(weight == LOW) {
        w = "LOW";
    } else if(weight == MEDIUM) {
        w = "MEDIUM";
    } else if(weight == HIGH) {
        w = "HIGH";
    }

    cout << "Item{" <<
        "Item type: " << it << ", Weight: " << w << "}" << endl;
}

bool Item::equals(Item item) {
    return getItemType() == item.getItemType() && getWeight() == item.getWeight();
}
