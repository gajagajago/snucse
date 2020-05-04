#ifndef CHECKING_H
#define CHECKING_H

#include "BankAccount.h"

class Checking : public BankAccount{
    public:
        Checking(int num=0,float bal=0,float min=1000,float chg=2);
        int withdraw(float amount);
        virtual void print();
    protected:
        //minimum account balance to keep(유지되야 하는 최소한의 잔고)
        float minimum; 
        // amount of money charged when balance is below minimum(minimum이 유지안되었을 때 발생하는 벌금)
        float charge;
};

#endif