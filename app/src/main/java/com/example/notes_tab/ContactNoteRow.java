package com.example.notes_tab;

import java.io.Serializable;

public class ContactNoteRow implements Serializable {
    String title, content;

    public ContactNoteRow(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
