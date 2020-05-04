#include <iostream>
using namespace std;
#include "Savings.h"

Savings::Savings(int num, float bal, float rate) {
    acctnum = num;
    balance = bal;
    intrate = rate;
}

void Savings::interest() {
    float monthly_interest = (intrate / 100) / 12;
    balance += balance * monthly_interest;
}

int Savings::withdraw(float amount) {
    if(balance <= amount) {
        cout << "Cannot withdraw $" << amount << " on account #" << acctnum << " because the balance is low." << endl;
        return 0;
    }else {
        balance -= amount;
        return 1;
    }
}

void Savings::print() {
    cout << "Savings Account: " << acctnum << endl;
    cout << "        Balance: " << balance << endl;
    cout << "        Interest: " << intrate << "%" << endl;
}