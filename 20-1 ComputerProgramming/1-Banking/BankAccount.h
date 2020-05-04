#ifndef BANKACCOUNT_H
#define BANKACCOUNT_H

class BankAccount{
    public:
        BankAccount();
        BankAccount(int num,float bal);
        void deposit(float amount); //money getting into account
        virtual int withdraw(float amount); // money getting out of account
        int getAcctnum(); // getter function
        float getBalance();// getter function
        virtual void print() = 0;
    protected:
        int acctnum; // account number(계좌번호)
        float balance; //current balance of account(현재 잔고)
};

#endif