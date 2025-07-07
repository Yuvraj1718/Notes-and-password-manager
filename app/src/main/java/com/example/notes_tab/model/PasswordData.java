/*
 * Copyright (c) 2023 Navjot Singh Rakhra. All rights reserved.
 */

package com.example.notes_tab.model;

import java.io.Serializable;

public class PasswordData implements Serializable {
    private final String website;
    private final char[] password;

    public String getWebsite() {
        return website;
    }

    public char[] getPassword() {
        return password;
    }

    public PasswordData(String website, char[] password) {
        this.website = website;
        this.password = password;
    }

}
