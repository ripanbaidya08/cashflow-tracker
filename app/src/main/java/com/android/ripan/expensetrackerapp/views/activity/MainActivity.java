package com.android.ripan.expensetrackerapp.views.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.ripan.expensetrackerapp.adapters.TransactionsAdapter;
import com.android.ripan.expensetrackerapp.models.Transaction;
import com.android.ripan.expensetrackerapp.utils.Constants;
import com.android.ripan.expensetrackerapp.utils.Helper;
import com.android.ripan.expensetrackerapp.viewmodels.MainViewModel;
import com.android.ripan.expensetrackerapp.views.fragements.AddTransactionFragment;
import com.android.ripan.expensetrackerapp.R;
import com.android.ripan.expensetrackerapp.databinding.ActivityMainBinding;
import com.android.ripan.expensetrackerapp.views.fragements.StatsFragment;
import com.android.ripan.expensetrackerapp.views.fragements.TransactionsFragement;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    Calendar calendar ;
    // 0 -> daily, 1 -> Montly, 2 -> Calendar, 3 -> Summary, 4 -> note
    public MainViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setSupportActionBar(binding.toolBar); // show the top menu, Named "transactions"
        getSupportActionBar().setTitle("Transactions");

        Constants.setCategories(); // Calling Methods

        calendar = Calendar.getInstance();

        viewModel.getTransactions(calendar);

        // Replacing the transactionFragement to the MainActivity
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, new TransactionsFragement());
        transaction.commit();

        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if(item.getItemId() == R.id.transactions){
                    getSupportFragmentManager().popBackStack();
                }else if(item.getItemId() == R.id.stats){
                    transaction.replace(R.id.content, new StatsFragment());
                    transaction.addToBackStack(null);
                }
                transaction.commit();
                return true;
            }
        });
    }

    public void getTransactions(){ // 2:27 , till now everything is working fine
        viewModel.getTransactions(calendar);
    }

    // For the Top Menu, 'Search and Favourite" menu is in top_menu.xml
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

}