package com.android.ripan.expensetrackerapp.views.fragements;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.ripan.expensetrackerapp.R;
import com.android.ripan.expensetrackerapp.adapters.AccountsAdapter;
import com.android.ripan.expensetrackerapp.adapters.CategoryAdapter;
import com.android.ripan.expensetrackerapp.databinding.FragmentAddTransactionBinding;
import com.android.ripan.expensetrackerapp.databinding.ListDialogBinding;
import com.android.ripan.expensetrackerapp.models.Account;
import com.android.ripan.expensetrackerapp.models.Category;
import com.android.ripan.expensetrackerapp.models.Transaction;
import com.android.ripan.expensetrackerapp.utils.Constants;
import com.android.ripan.expensetrackerapp.utils.Helper;
import com.android.ripan.expensetrackerapp.views.activity.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

// we import the BottomSheetDialogFragment, bcz we want to show the bottom sheet .

public class AddTransactionFragment extends BottomSheetDialogFragment {

    public AddTransactionFragment() { }

    FragmentAddTransactionBinding binding;
    Transaction transaction ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddTransactionBinding.inflate(inflater);

        transaction = new Transaction();

        binding.incomeBtn.setOnClickListener(v -> {
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.income_selector));
            binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.textColor));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.green));

            transaction.setType(Constants.INCOME);

        });

        binding.expenseBtn.setOnClickListener(v -> {
            binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.expense_selector));
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.textColor));
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.red));

            transaction.setType(Constants.EXPENSE);
        });

        binding.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
                // Saving the Date to the 'input' area.
                datePickerDialog.setOnDateSetListener((datePicker, i, i1, i2) -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                    calendar.set(Calendar.MONTH, datePicker.getMonth());
                    calendar.set(Calendar.YEAR, datePicker.getYear());

                    // SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM, yyyy");
                    String dateToShow = Helper.formatDate(calendar.getTime());

                    binding.date.setText(dateToShow);

                    transaction.setDate(calendar.getTime());
                    transaction.setId(calendar.getTime().getTime());
                });
                datePickerDialog.show();
            }
        });


        binding.category.setOnClickListener(v -> {
            ListDialogBinding dialogBuilding = ListDialogBinding.inflate(inflater);
            AlertDialog categoryDialog = new AlertDialog.Builder(getContext()).create();
            categoryDialog.setView(dialogBuilding.getRoot());

            CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), Constants.categories, new CategoryAdapter.CategoryClickListener() {
                @Override
                public void onCategoryClicked(Category category) {
                    binding.category.setText(category.getCategoryName());
                    transaction.setCategory(category.getCategoryName());
                    categoryDialog.dismiss();
                }
            });
            dialogBuilding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
            dialogBuilding.recyclerView.setAdapter(categoryAdapter);

            categoryDialog.show();
        });


        binding.account.setOnClickListener(v -> {
            ListDialogBinding dialogBuilding = ListDialogBinding.inflate(inflater);
            AlertDialog accountsDialog = new AlertDialog.Builder(getContext()).create();
            accountsDialog.setView(dialogBuilding.getRoot());

            ArrayList<Account> accounts = new ArrayList<>();

            accounts.add(new Account(0, "Cash"));
            accounts.add(new Account(0, "Bank"));
            accounts.add(new Account(0, "Paytm"));
            accounts.add(new Account(0, "EasyPaisa"));
            accounts.add(new Account(0, "Other"));

            AccountsAdapter adapter = new AccountsAdapter(getContext(), accounts, new AccountsAdapter.AccountsClickListener() {
                @Override
                public void onAccountSelected(Account account) {
                    binding.account.setText(account.getAccountName());
                    transaction.setAccount(account.getAccountName());
                    accountsDialog.dismiss();
                }
            });
            dialogBuilding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            dialogBuilding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL)); // this is for divided line
            dialogBuilding.recyclerView.setAdapter(adapter);

            accountsDialog.show();
        });

        binding.saveTransactionBtn.setOnClickListener(v -> {
            double amount = Double.parseDouble(binding.amount.getText().toString());
            String note = binding.note.getText().toString();

            if(transaction.getType().equals(Constants.EXPENSE)){
                transaction.setAmount(amount*-1);
            } else {
                transaction.setAmount(amount);
            }

            transaction.setNote(note);

            ((MainActivity)getActivity()).viewModel.addTransaction(transaction);
            ((MainActivity)getActivity()).getTransactions();
            dismiss();
        });
        return binding.getRoot();
    }
}