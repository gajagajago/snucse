#include <iostream>
using namespace std;
#include "BankAccount.h"

BankAccount::BankAccount(){};

BankAccount::BankAccount(int num, float bal) {
    acctnum = num;
    balance = bal;
}

void BankAccount::deposit(float amount) {
    balance += amount;
}

int BankAccount::withdraw(float amount) {
    balance -= amount;
    return 0;
}

int BankAccount::getAcctnum() {
    return acctnum;
}

float BankAccount::getBalance() {
    return balance;
}
