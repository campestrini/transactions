db = db.getSiblingDB('transactions');

db.createUser({
  user: "username",
  pwd: "password",
  roles: [
    { role: "readWrite", db: "transactions" }
  ]
});

db.accounts.insertMany([
    { code: "FOOD", balance: 1000.00 },
    { code: "MEAL", balance: 1500.00 },
    { code: "CASH", balance: 2000.00 }
]);

db.merchants.insertMany([
    { name: "UBER TRIP                   SAO PAULO BR", account: "CASH" },
    { name: "UBER EATS                   SAO PAULO BR", account: "FOOD" },
    { name: "PAG*JoseDaSilva             RIO DE JANEI BR", account: "CASH" },
    { name: "PICPAY*BILHETEUNICO         GOIANIA BR", account: "CASH" }
]);

db.mccs.insertMany([
    { code: "5411", account: "FOOD" },
    { code: "5412", account: "FOOD" },
    { code: "5811", account: "MEAL" },
    { code: "5812", account: "MEAL" }
]);