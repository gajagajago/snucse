#include "StoreInventory.h"
#include <vector>
#include <iostream>
#include <cstring>
#include <string>
#include <algorithm>
#include <fstream>
#include <sstream>

using namespace std;

StoreInventory::StoreInventory()
{
    ReadFromFile("available_items.txt");
}

ItemType StoreInventory::strToItemType(char *it)
{
    if (strcmp(it, "SLEEPING_BAG") == 0)
    {
        return SLEEPING_BAG;
    }
    else if (strcmp(it, "TENT") == 0)
    {
        return TENT;
    }
    else if (strcmp(it, "LURE") == 0)
    {
        return LURE;
    }
    else if (strcmp(it, "CLOTHING") == 0)
    {
        return CLOTHING;
    }
    else if (strcmp(it, "FISHING_ROD") == 0)
    {
        return FISHING_ROD;
    }
    else if (strcmp(it, "COOKING") == 0)
    {
        return COOKING;
    }
    else if (strcmp(it, "WATER") == 0)
    {
        return WATER;
    }

    return SLEEPING_BAG;
}

Weight StoreInventory::strToWeight(char *w)
{
    if (strcmp(w, "LOW") == 0)
    {
        return LOW;
    }
    else if (strcmp(w, "MEDIUM") == 0)
    {
        return MEDIUM;
    }
    else if (strcmp(w, "HIGH") == 0)
    {
        return HIGH;
    }

    return LOW;
}

void StoreInventory::ReadFromFile(string filename)
{
    ifstream ifs(filename);

    if (ifs.is_open())
    {
        vector<Item> item_vector;
        vector<char *> parsed_line;
        char *pch;
        const char *delimiter = " ";
        while (!ifs.eof())
        {
            char buf[100];
            ifs.getline(buf, 100);
            pch = strtok(buf, delimiter);
            while (pch != NULL)
            {
                parsed_line.push_back(pch);
                pch = strtok(NULL, delimiter);
            }

            if (!strncmp(parsed_line.at(0), "item", sizeof("item")))
            {
                char *one = parsed_line.at(1);
                char *two = parsed_line.at(2);
                ItemType it = strToItemType(one);
                Weight w = strToWeight(two);
                Item item(it, w);
                item_vector.push_back(item);
            }
            parsed_line.clear(); 
        }
        random_shuffle(item_vector.begin(), item_vector.end());
        int size = item_vector.size();
        item_list = new Item[size];
        copy(item_vector.begin(), item_vector.end(), item_list);
        ifs.close();
    }
    else
    {
        cout << "file not open :(" << endl;
    }
}
