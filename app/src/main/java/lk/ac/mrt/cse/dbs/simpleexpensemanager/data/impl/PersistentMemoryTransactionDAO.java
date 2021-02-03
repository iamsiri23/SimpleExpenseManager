package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;



/**
 * Created by Thirumagal on 1/2/2021.
 */



public class PersistentMemoryTransactionDAO implements TransactionDAO {

    //transaction table
    private static final String KEY_ACCOUNT_NUMBER = "accountNo";
    private static final String KEY_TRANSACTION_EXPENSE_TYPE = "expenseType";
    private static final String KEY_TRANSACTION_AMOUNT = "amount";
    private static final String KEY_TRANSACTION_DATE = "date";


    SQLiteDatabase sqLiteDatabase;

    public PersistentMemoryTransactionDAO(SQLiteDatabase db){

      this.sqLiteDatabase =db;
    }


    @Override
    //insert values into transaction table
    public void logTransaction(Date date_, String accountNo, ExpenseType expenseType_, double amount_){



            String insertQuery = "INSERT INTO Account_Transaction (accountNo,expenseType,amount,date) VALUES (?,?,?,?)";
            SQLiteStatement statement = sqLiteDatabase.compileStatement(insertQuery);

            statement.bindString(1,accountNo);
            statement.bindLong(2,(expenseType_ == ExpenseType.EXPENSE) ? 0 : 1);
            statement.bindDouble(3,amount_);
            statement.bindLong(4,date_.getTime());

            statement.executeInsert();



    }

    @Override
    //get all transactions
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactions = new ArrayList<>();

        String transactionDetailSelectQuery = "SELECT * FROM Account_Transaction";
        Cursor rawQuery = sqLiteDatabase.rawQuery(transactionDetailSelectQuery, null);

        try {
            if (rawQuery.moveToFirst()) {
                do {
                    Transaction trans=new Transaction(
                            new Date(rawQuery.getLong(rawQuery.getColumnIndex(KEY_TRANSACTION_DATE))),
                            rawQuery.getString(rawQuery.getColumnIndex(KEY_ACCOUNT_NUMBER)),
                            (rawQuery.getInt(rawQuery.getColumnIndex(KEY_TRANSACTION_EXPENSE_TYPE)) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                   rawQuery.getDouble(rawQuery.getColumnIndex(KEY_TRANSACTION_AMOUNT)));




                    transactions.add(trans);

                } while (rawQuery.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rawQuery != null && !rawQuery.isClosed()) {
                rawQuery.close();
            }
        }


        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {


        List<Transaction> transactionArrayList = new ArrayList<>();

        String transDetailSelectQuery = "SELECT * FROM Account_Transaction LIMIT"+limit;


        Cursor rawQuery = sqLiteDatabase.rawQuery(transDetailSelectQuery, null);


            if (rawQuery.moveToFirst()) {
                do {
                    Transaction trans=new Transaction(
                            new Date(rawQuery.getLong(rawQuery.getColumnIndex(KEY_TRANSACTION_DATE))),
                            rawQuery.getString(rawQuery.getColumnIndex(KEY_ACCOUNT_NUMBER)),
                            (rawQuery.getInt(rawQuery.getColumnIndex(KEY_TRANSACTION_EXPENSE_TYPE)) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                            rawQuery.getDouble(rawQuery.getColumnIndex(KEY_TRANSACTION_AMOUNT)));



                    transactionArrayList.add(trans);

                } while (rawQuery.moveToNext());
            }

        return  transactionArrayList;
    }





}

