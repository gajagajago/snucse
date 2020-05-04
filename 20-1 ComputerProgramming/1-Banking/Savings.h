#ifndef SAVINGS_H
#define SAVINGS_H

#include "BankAccount.h"

class Savings : public BankAccount {
    public:
        Savings(int num=0,float bal=0,float rate=3.5);
        void interest(); // add monthly interest to current balance
        int withdraw(float amount);
        virtual void print();
    protected:
        float intrate; //interest rate(이자율)
};

#endif