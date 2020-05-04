#ifndef BANKSYSTEM_H
#define BANKSYSTEM_H

#include "BankAccount.h"

class BankSystem {
    public:
        void transaction(BankAccount* from, BankAccount* to, float amount); //electronic money transaction; there are sender and receiver 
        void deposit(BankAccount* b, float amount); //person puts cash into his or her account
        void withdraw(BankAccount* b, float amount); //person takes cash out of his or her account
};

#endif