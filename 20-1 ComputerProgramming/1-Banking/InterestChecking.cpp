#include <iostream>
using namespace std;
#include "InterestChecking.h"

InterestChecking::InterestChecking(int num, float bal, float cmin, float imin, float chg, float rate, float monchg) {
    acctnum = num;
    balance = bal;
    minimum = cmin;
    minint = imin;
    charge = chg;
    intrate = rate;
    moncharge = monchg;
}

void InterestChecking::interest() {
    if(balance < minint) {
        balance -= moncharge;
    }else {
        float monthly_int = (intrate / 100) / 12;
        balance += balance * monthly_int;
    }
}

void InterestChecking::print() {
    cout << "Interest Checking Account: " << acctnum << endl;
    cout << "         Balance: " << balance << endl;
    cout << "         Minimum to Avoid Charges: " << minimum << endl;
    cout << "         Charge per Check: " << charge << endl;
    cout << "         Minimum balance for interest and No Monthly Fee: " << minint << endl;
    cout << "         Interest: " << intrate << "%" << endl;
    cout << "         Monthly Fee: " << moncharge << endl;
}