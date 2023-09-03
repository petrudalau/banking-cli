# banking-cli
## How to run
Run main() in the `org.intexsoft.main.BankConsoleApp`
## commands to know before start
* `exit` - exits the app
* `help` - prints the format of all the commands
##  DB required
App connects with hardcoded(TODO) connection url `jdbc:mysql://localhost:3306/banking` with user `rood:admin`
Tables required can be created with the following script
```
create table account
(
    id       varchar(250)   not null
        primary key,
    clientId varchar(250)   null,
    bankId   varchar(250)   null,
    currency varchar(10)    null,
    amount   double(20, 10) null
);

create table bank
(
    id                            varchar(250)  not null
        primary key,
    name                          varchar(250)  null,
    external_transfer_fee         double(10, 4) null,
    external_transfer_fee_company double(10, 4) null
);

create table client
(
    id      varchar(250) not null
        primary key,
    name    varchar(250) null,
    company tinyint(1)   null
);

create table transaction
(
    id              varchar(250)   not null
        primary key,
    fromAccount     varchar(250)   null,
    toAccount       varchar(250)   null,
    transactionFee  double(20, 10) null,
    transactionTime datetime       null,
    currency        varchar(10)    null,
    amount          double(20, 10) null
);
```

##TODO
 * reuse DB connections
 * DB constraints
 * hard or soft delete?
 * clarify fee - is it amount of percentage?
 * scale decimal amounts everywhere
 * transactions
 * configure logging to the separate file instead of console
 * configurable currency rates
 * review exceptions and error propagation
 * improve commands documentation, add examples, improve printing of collections
 * java docs
 * review logs
 * review DB column types
 * check dates and timezones
 * improve test coverage
 * code clean-up
