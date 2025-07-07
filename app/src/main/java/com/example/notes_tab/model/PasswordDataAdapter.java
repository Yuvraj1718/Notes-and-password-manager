/*
 * Copyright (c) 2023 Navjot Singh Rakhra. All rights reserved.
 */
package com.example.notes_tab.model;

import android.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes_tab.PasswordActivity;
import com.example.notes_tab.R;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PasswordDataAdapter extends RecyclerView.Adapter<PasswordDataViewHolder> implements Serializable {
    private final List<PasswordData> passwordDataList;
    private final PasswordActivity passwordActivity;

    public PasswordDataAdapter(List<PasswordData> passwordDataList, PasswordActivity passwordActivity) {
        this.passwordDataList = passwordDataList;
        this.passwordActivity = passwordActivity;
    }

    @NonNull
    @Override
    public PasswordDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pwd_view_model, parent, false);
        return new PasswordDataViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PasswordDataViewHolder holder, int position) {
        holder.getWebsiteView().setText(passwordDataList.get(position).getWebsite());
        holder.getPasswordView().setText(passwordDataList.get(position).getPassword(), 0, passwordDataList.get(position).getPassword().length);
        holder.getPasswordView().setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        holder.getPasswordView().getRootView().setOnLongClickListener(view -> {
            view.startAnimation(passwordActivity.getScaleUp());
            view.startAnimation(passwordActivity.getScaleDown());
            new AlertDialog.Builder(view.getContext())
                    .setTitle("Delete this password?")
                    .setPositiveButton("Yes", (a, b) -> {
                        passwordDataList.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                        passwordActivity.executorService.execute(passwordActivity::updateData);
                    })
                    .setNegativeButton("No", null)
                    .show();
            return false;
        });
    }


    @Override
    public int getItemCount() {
        return passwordDataList.size();
    }
}
