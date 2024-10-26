package com.android.ripan.expensetrackerapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.ripan.expensetrackerapp.R;
import com.android.ripan.expensetrackerapp.databinding.RowTransactionBinding;
import com.android.ripan.expensetrackerapp.models.Category;
import com.android.ripan.expensetrackerapp.models.Transaction;
import com.android.ripan.expensetrackerapp.utils.Constants;
import com.android.ripan.expensetrackerapp.utils.Helper;
import com.android.ripan.expensetrackerapp.views.activity.MainActivity;

import java.util.ArrayList;

import io.realm.RealmResults;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder>{

    Context context ;
    RealmResults<Transaction> transactions;



    public TransactionsAdapter(Context context, RealmResults<Transaction> transactions){
            this.context = context;
            this.transactions = transactions;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionViewHolder(LayoutInflater.from(context).inflate(R.layout.row_transaction, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);

        holder.binding.transactionAmount.setText(String.valueOf(transaction.getAmount())); // set the Amount
        holder.binding.accountLbl.setText(transaction.getAccount()); // set the Account

        holder.binding.transactionDate.setText(Helper.formatDate(transaction.getDate())); // set date
        holder.binding.transactionCategory.setText(transaction.getCategory()); // set category

        Category transactionCategory = Constants.getCategoryDetails(transaction.getCategory()); // give the category details
        holder.binding.categoryIcon.setImageResource(transactionCategory.getCategoryImage());
        holder.binding.categoryIcon.setBackgroundTintList(context.getColorStateList(transactionCategory.getCategoryColor()));

        holder.binding.accountLbl.setBackgroundTintList(context.getColorStateList(Constants.getAccountColour(transaction.getAccount())));


        if(transaction.getType().equals(Constants.INCOME)){
            holder.binding.transactionAmount.setTextColor(R.color.green);
        } else {
            holder.binding.transactionAmount.setTextColor(R.color.red);
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog deleteDialog = new AlertDialog.Builder(context).create();
                deleteDialog.setTitle("Delete Transaction !");
                deleteDialog.setMessage("Are you Sure to delete this transaction ?");
                deleteDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", (dialogInterface, i) -> {
                    ((MainActivity)context).viewModel.deleteTransaction(transaction);
                });
                deleteDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", (dialogInterface, i) ->{
                    deleteDialog.dismiss();
                });
                deleteDialog.show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder {
        RowTransactionBinding binding ;
        public TransactionViewHolder(@NonNull View itemView){
            super(itemView);
            binding = RowTransactionBinding.bind(itemView);
        }
    }
}
