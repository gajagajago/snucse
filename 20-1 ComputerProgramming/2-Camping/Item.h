#ifndef ITEMITEM_H
#define ITEMITEM_H

#include "enum.h"

class Item {
    private:
       ItemType item_type;
       Weight weight;

    public:
        Item();
        Item(ItemType itemType, Weight weight);
        ItemType getItemType();
        void setItemType(ItemType itemType);
        Weight getWeight();
        void setWeight(Weight weight);
        int compareTo(Item item);
        void print();
        bool equals(Item item);
};

#endif