#include <iostream>

#include "Savings.h"
#include "InterestChecking.h"
#include "Checking.h"
#include "BankAccount.h"
#include "BankSystem.h"
using namespace std;

int main() {

  BankAccount* accounts[4];
  BankSystem bank1;

  Checking James(1103458181,950);
  InterestChecking Thompson(1203217789,2800);
  Savings Mathew(1308563516,4000);
  Checking Amy(1401945501,670);

  accounts[0] = &James;
  accounts[1] = &Thompson;
  accounts[2] = &Mathew;
  accounts[3] = &Amy;


  //  all operations
  bank1.transaction(&James, &Thompson, 200);
  bank1.withdraw(&James, 100);
  bank1.withdraw(&Mathew, 1000);
  bank1.transaction(&Thompson, &Amy, 600);
  bank1.withdraw(&Amy, 1300);
  bank1.withdraw(&Thompson, 200);
  bank1.transaction(&Mathew, &James, 540);
  bank1.transaction(&James, &Amy, 1200);
  bank1.deposit(&James, 300);

  //  For the month, Interest checking and savings getting interest.
  Thompson.interest();
  Mathew.interest();

  //  report on account balances
  cout << endl << endl << "\t\tAccount Balances" << endl << endl;
  cout << endl << "Name: James    Account Number:  #" << James.getAcctnum();
  cout << "      Balance:  $ " << James.getBalance() << endl;
  cout << endl << "Name: Thompson    Account Number:  #" << Thompson.getAcctnum();
  cout << "      Balance:  $ " << Thompson.getBalance() << endl;
  cout << endl << "Name: Mathew    Account Number:  #" << Mathew.getAcctnum();
  cout << "      Balance:  $ " << Mathew.getBalance() << endl;
  cout << endl << "Name: Amy    Account Number:  #" << Amy.getAcctnum();
  cout << "      Balance:  $ " << Amy.getBalance() << endl;
  cout << endl;

  //print every account info
  for (int i = 0 ; i < 4 ; i++) {
    accounts[i]->print();
  }

  return 0;
}