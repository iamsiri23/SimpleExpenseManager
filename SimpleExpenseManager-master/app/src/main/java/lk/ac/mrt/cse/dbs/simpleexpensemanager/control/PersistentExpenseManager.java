package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentMemoryTransactionDAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


/**
 * Created by Thirumagal on 1/2/2021.
 */




public class PersistentExpenseManager extends ExpenseManager{


    Context context;
    private static final String ACCOUNT_TABLE_NAME = "Account";
    private static final String KEY_ACCOUNT_NUMBER = "accountNo";
    private static final String KEY_BANK_NAME = "bankName";
    private static final String KEY_HOLDER_NAME = "accountHolderName";
    private static final String KEY_BALANCE = "balance";
    private static final String ACCOUNT_TRANSACTION_TABLE_NAME = "Account_Transaction";
    private static final String KEY_TRANSACTION_ID = "Transaction_id";
    private static final String KEY_TRANSACTION_EXPENSE_TYPE = "expenseType";
    private static final String KEY_TRANSACTION_AMOUNT = "amount";
    private static final String KEY_TRANSACTION_DATE = "date";





    public PersistentExpenseManager(Context context1)  {
        context=context1;
        try {

            setup();
        } catch (ExpenseManagerException e) {
            e.printStackTrace();
        }
    }
    @Override

    public void setup() throws ExpenseManagerException {

        SQLiteDatabase db = context.openOrCreateDatabase("180645F", context.MODE_PRIVATE, null);

//         create the databases.
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ACCOUNT_TABLE_NAME+" ( " +
                KEY_ACCOUNT_NUMBER + " VARCHAR PRIMARY KEY, " +
                KEY_BANK_NAME +" VARCHAR, " +
                KEY_HOLDER_NAME +" VARCHAR, " +
                KEY_BALANCE + " REAL" +
                " );");



        db.execSQL("CREATE TABLE IF NOT EXISTS "+ ACCOUNT_TRANSACTION_TABLE_NAME +" (" +
                KEY_TRANSACTION_ID + " INTEGER PRIMARY KEY, " +
                KEY_ACCOUNT_NUMBER + " VARCHAR, " +
                KEY_TRANSACTION_EXPENSE_TYPE + " INT, " +
                KEY_TRANSACTION_AMOUNT + " REAL, " +
                KEY_TRANSACTION_DATE + " DATE, "  +
                "FOREIGN KEY (" + KEY_ACCOUNT_NUMBER +") REFERENCES "+ ACCOUNT_TABLE_NAME +"(" + KEY_ACCOUNT_NUMBER +")" +
                ");");






        PersistentMemoryAccountDAO accountDAO = new PersistentMemoryAccountDAO(db);
        PersistentMemoryTransactionDAO transactionDAO = new PersistentMemoryTransactionDAO(db);
        setAccountsDAO(accountDAO);

        setTransactionsDAO(transactionDAO);

        /*** End ***/
    }
}
