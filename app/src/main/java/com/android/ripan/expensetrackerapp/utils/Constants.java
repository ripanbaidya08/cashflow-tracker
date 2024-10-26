package com.android.ripan.expensetrackerapp.utils;

import com.android.ripan.expensetrackerapp.R;
import com.android.ripan.expensetrackerapp.models.Category;

import java.util.ArrayList;

public class Constants {

    public static String INCOME = "INCOME";
    public static String EXPENSE = "EXPENSE";

    public static int DAILY = 0 ;
    public static int MONTHLY = 1 ;
    public static int CALENDER = 2 ;
    public static int SUMMARY = 3 ;
    public static int NOTE = 4 ;

    public static int SELECTED_TAB = 0 ;
    public static int SELECTED_TAB_STATS = 0 ;

    public static String SELECTED_STATS_TYPE = Constants.INCOME;
    public static ArrayList<Category> categories;
    public static void setCategories(){
        categories = new ArrayList<>();
        categories.add(new Category("Salary", R.drawable.ic_salary, R.color.category1));
        categories.add(new Category("Business", R.drawable.ic_business, R.color.category2));
        categories.add(new Category("Investment", R.drawable.ic_investment, R.color.category3));
        categories.add(new Category("Loan", R.drawable.ic_loan, R.color.category4));
        categories.add(new Category("Rent", R.drawable.ic_rent, R.color.category5));
        categories.add(new Category("Other", R.drawable.ic_other, R.color.category6));
    }

    // category will be select based on the name.
    public static Category getCategoryDetails(String categoryName){
        for(Category category : categories){
            if(category.getCategoryName().equals(categoryName)){
                return category;
            }
        }
        return null;
    }

    public static int getAccountColour(String accountName){
        switch (accountName){
            case "Bank":
                return R.color.bank_color;
            case "Cash":
                return R.color.cash_color;
            case "Card":
                return R.color.card_color;
            default:
                return R.color.default_color;
        }
    }

}
