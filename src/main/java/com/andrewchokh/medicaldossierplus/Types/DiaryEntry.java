package com.andrewchokh.medicaldossierplus.Types;


import java.util.Date;

public class DiaryEntry {
    private final int id;
    private final int userId;
    private final String content;
    private final Date creationDate;

    public DiaryEntry(int id, int userId, String content, Date creationDate) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.creationDate = creationDate;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public Date getCreationDate() {
        return creationDate;
    }
}
