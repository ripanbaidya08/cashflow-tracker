package com.android.ripan.expensetrackerapp.views.fragements;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.ripan.expensetrackerapp.R;
import com.android.ripan.expensetrackerapp.adapters.TransactionsAdapter;
import com.android.ripan.expensetrackerapp.databinding.FragmentAddTransactionBinding;
import com.android.ripan.expensetrackerapp.databinding.FragmentTransactionsFragementBinding;
import com.android.ripan.expensetrackerapp.models.Transaction;
import com.android.ripan.expensetrackerapp.utils.Constants;
import com.android.ripan.expensetrackerapp.utils.Helper;
import com.android.ripan.expensetrackerapp.viewmodels.MainViewModel;
import com.android.ripan.expensetrackerapp.views.activity.MainActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;

import io.realm.RealmResults;

public class TransactionsFragement extends Fragment {

    public TransactionsFragement(){ }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentTransactionsFragementBinding binding;

    Calendar calendar ;
    // 0 -> daily, 1 -> Montly, 2 -> Calendar, 3 -> Summary, 4 -> note
    public MainViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTransactionsFragementBinding.inflate(inflater);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        calendar = Calendar.getInstance();
        updateDate();

        binding.nextDateBtn.setOnClickListener(v -> {
            if(Constants.SELECTED_TAB == Constants.DAILY){
                calendar.add(Calendar.DATE, 1);
            } else if (Constants.SELECTED_TAB == Constants.MONTHLY){
                calendar.add(Calendar.MONTH, 1);
            }
            updateDate();
        });

        binding.previousDateBtn.setOnClickListener(v -> {
            if(Constants.SELECTED_TAB == Constants.DAILY){
                calendar.add(Calendar.DATE, -1);
            }else if(Constants.SELECTED_TAB == Constants.MONTHLY){
                calendar.add(Calendar.MONTH, -1);
            }
            updateDate();
        });

        binding.floatingActionButton.setOnClickListener(v -> {
            /* if we press the 'plus' icon on the Main Menu then a new fragment will be opened from the bottom sheet.. */
            new AddTransactionFragment().show(getParentFragmentManager(), null);
        });

        binding.tabLayout2.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getText().equals("Monthly")){
                    Constants.SELECTED_TAB = 1;
                    updateDate();
                } else if(tab.getText().equals("Daily")){
                    Constants.SELECTED_TAB = 0;
                    updateDate();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        binding.transactionsList.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel.transactions.observe(getViewLifecycleOwner(), new Observer<RealmResults<Transaction>>() {
            @Override
            public void onChanged(RealmResults<Transaction> transactions) {
                TransactionsAdapter adapter = new TransactionsAdapter(getActivity(), transactions);
                binding.transactionsList.setAdapter(adapter);
                if(transactions.size() > 0){
                    binding.emptyState.setVisibility(View.GONE);
                } else {
                    binding.emptyState.setVisibility(View.VISIBLE);
                }
            }
        });

        viewModel.totalIncome.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.incomeLbl.setText(String.valueOf(aDouble)); // 2:19
            }
        });

        viewModel.totalExpense.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.expenseLbl.setText(String.valueOf(aDouble));
            }
        });

        viewModel.totalAmount.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.totalLbl.setText(String.valueOf(aDouble));
            }
        });

        return binding.getRoot();
    }

    void updateDate(){
        if(Constants.SELECTED_TAB == Constants.DAILY){
            binding.currentDate.setText(Helper.formatDate(calendar.getTime()));
        } else if(Constants.SELECTED_TAB == Constants.MONTHLY){
            binding.currentDate.setText(Helper.formatDateByMonth(calendar.getTime()));
        }

        viewModel.getTransactions(calendar);
    }
}