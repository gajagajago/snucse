#include <iostream>
using namespace std;
#include "BankSystem.h"

void BankSystem::transaction(BankAccount *from, BankAccount *to, float amount) {
    float transaction_fee = 10;
    float prev_withdraw_bal = from -> getBalance();

    from -> withdraw(amount + transaction_fee);
    if(prev_withdraw_bal >= (amount+transaction_fee)) {
        to->deposit(amount);
    }
}

void BankSystem::deposit(BankAccount *b, float amount) {
    b -> deposit(amount);
}

void BankSystem::withdraw(BankAccount *b, float amount) {
    b -> withdraw(amount);
}