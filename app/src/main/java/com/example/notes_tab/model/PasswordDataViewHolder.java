/*
 * Copyright (c) 2023 Navjot Singh Rakhra. All rights reserved.
 */
package com.example.notes_tab.model;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes_tab.R;


public class PasswordDataViewHolder extends RecyclerView.ViewHolder {
    public TextView getWebsiteView() {
        return websiteView;
    }

    public EditText getPasswordView() {
        return passwordView;
    }

    public PasswordDataViewHolder(@NonNull View itemView) {
        super(itemView);

        websiteView = itemView.findViewById(R.id.website);
        passwordView = itemView.findViewById(R.id.password);
    }

    private final TextView websiteView;
    private final EditText passwordView;

}
