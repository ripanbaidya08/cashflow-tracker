package com.android.ripan.expensetrackerapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.ripan.expensetrackerapp.models.Transaction;
import com.android.ripan.expensetrackerapp.utils.Constants;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainViewModel extends AndroidViewModel {

    public MutableLiveData<RealmResults<Transaction>> transactions = new MutableLiveData<>();
    public MutableLiveData<RealmResults<Transaction>> categoriesTransactions = new MutableLiveData<>();

    public MutableLiveData<Double> totalIncome = new MutableLiveData<>();
    public MutableLiveData<Double> totalExpense = new MutableLiveData<>();
    public MutableLiveData<Double> totalAmount = new MutableLiveData<>();


    Realm realm ;
    Calendar calendar;

    public MainViewModel(@NonNull Application application) {
        super(application);
        Realm.init(application);
        setupDatabase();
    }

    public void getTransactions(Calendar calendar, String type) {
        this.calendar = calendar;

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        RealmResults<Transaction> newTransactions = null ;

        if (Constants.SELECTED_TAB_STATS == Constants.DAILY) {

            newTransactions = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", calendar.getTime())
                    .lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000)))
                    .equalTo("type", type) // based on income or expense
                    .findAll();

        } else if(Constants.SELECTED_TAB_STATS == Constants.MONTHLY){
            calendar.set(Calendar.DAY_OF_MONTH, 0);

            Date startTime = calendar.getTime();

            calendar.add(Calendar.MONTH, 1);

            Date endTime = calendar.getTime();

            newTransactions = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", endTime)
                    .equalTo("type", type)
                    .findAll();

        }
        categoriesTransactions.setValue(newTransactions);
    }

    public void getTransactions(Calendar calendar) {
        this.calendar = calendar;

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        double income = 0;
        double expense = 0 ;
        double total = 0;

        RealmResults<Transaction> newTransactions = null ;

        if (Constants.SELECTED_TAB == Constants.DAILY) {

        newTransactions = realm.where(Transaction.class)
                .greaterThanOrEqualTo("date", calendar.getTime())
                .lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000)))
                .findAll();

        income = realm.where(Transaction.class)
                .greaterThanOrEqualTo("date", calendar.getTime())
                .lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000)))
                .equalTo("type", Constants.INCOME)
                .sum("amount")
                .doubleValue();

        expense = realm.where(Transaction.class)
                .greaterThanOrEqualTo("date", calendar.getTime())
                .lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000)))
                .equalTo("type", Constants.EXPENSE)
                .sum("amount")
                .doubleValue();

        total = realm.where(Transaction.class)
                .greaterThanOrEqualTo("date", calendar.getTime())
                .lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000)))
                .sum("amount")
                .doubleValue();

        } else if(Constants.SELECTED_TAB == Constants.MONTHLY){
            calendar.set(Calendar.DAY_OF_MONTH, 0);

            Date startTime = calendar.getTime();

            calendar.add(Calendar.MONTH, 1);

            Date endTime = calendar.getTime();

            newTransactions = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", endTime)
                    .findAll();

            income = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", endTime)
                    .equalTo("type", Constants.INCOME)
                    .sum("amount")
                    .doubleValue();

            expense = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", endTime)
                    .equalTo("type", Constants.EXPENSE)
                    .sum("amount")
                    .doubleValue();

            total = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", endTime)
                    .sum("amount")
                    .doubleValue();
        }
        totalIncome.setValue(income);
        totalExpense.setValue(expense);
        totalAmount.setValue(total);
        transactions.setValue(newTransactions);
        // select * from dbName; | select * from dbName where id = ? ;
//        RealmResults<Transaction> newTransactions = realm.where(Transaction.class)
//                .equalTo("date", calendar.getTime())
//                .findAll();
    }

    public void addTransaction(Transaction transaction){
        realm.beginTransaction(); // Realm database related code will start from here.
        realm.copyToRealmOrUpdate(transaction); // 2:24
        realm.commitTransaction(); // Realm database related code will enclose within this.
    }

    public void deleteTransaction(Transaction transaction){
        realm.beginTransaction();
        transaction.deleteFromRealm();
        realm.commitTransaction();
        getTransactions(calendar);
    }
    public void addTransactions(){
        realm.beginTransaction(); // Realm database related code will start from here.

        realm.copyToRealmOrUpdate(new Transaction(Constants.INCOME, "Business", "Cash", "some note here", new Date(), 1000, new Date().getTime()));
        realm.copyToRealmOrUpdate(new Transaction(Constants.EXPENSE, "Investment", "Card", "some note here", new Date(), -500, new Date().getTime()));
        realm.copyToRealmOrUpdate(new Transaction(Constants.INCOME, "Rent", "Cash", "some note here", new Date(), 1000, new Date().getTime()));
        realm.copyToRealmOrUpdate(new Transaction(Constants.INCOME, "Business", "Other", "some note here", new Date(), 1000, new Date().getTime()));

        realm.commitTransaction(); // Realm database related code will enclose within this.
    }


    void setupDatabase(){ // 2:04
        realm = Realm.getDefaultInstance();
    }
}
