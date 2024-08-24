db = db.getSiblingDB('transactions');

db.accounts.insertMany([
    { code: "FOOD", balance: 1000.00 },
    { code: "MEAL", balance: 1500.00 },
    { code: "CASH", balance: 2000.00 }
]);