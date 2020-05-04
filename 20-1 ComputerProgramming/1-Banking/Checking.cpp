#include <iostream>
using namespace std;
#include "Checking.h"

Checking::Checking(int num, float bal, float min, float chg) {
    acctnum = num;
    balance = bal;
    minimum = min;
    charge = chg;
}

int Checking::withdraw(float amount) {
    if(balance <= amount) {
        cout << "Cannot withdraw $" << amount << " on account #" << acctnum << " because the balance is low." << endl;
        return 0;
    }else if(balance < minimum) {
        balance -= (amount+charge);
        return 1;
    }else {
        balance -= amount;
        return 1;
    }
}

void Checking::print() {
    cout << "Checking Account: " << acctnum << endl;
    cout << "         Balance: " << balance << endl;
    cout << "         Minimum to Avoid Charges: " << minimum << endl;
    cout << "         Charge per Check: " << charge << endl;
}