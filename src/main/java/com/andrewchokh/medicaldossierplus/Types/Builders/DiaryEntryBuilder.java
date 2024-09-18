package com.andrewchokh.medicaldossierplus.Types.Builders;

import com.andrewchokh.medicaldossierplus.Types.DiaryEntry;
import java.util.Date;

public class DiaryEntryBuilder {
    private int id;
    private int userId;
    private String content;
    private Date creationDate;

    public DiaryEntryBuilder id(int id) {
        this.id = id;
        return this;
    }

    public DiaryEntryBuilder userId(int userId) {
        this.userId = userId;
        return this;
    }

    public DiaryEntryBuilder content(String content) {
        this.content = content;
        return this;
    }

    public DiaryEntryBuilder creationDate(Date creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public DiaryEntry build() {
        return new DiaryEntry(id, userId, content, creationDate);
    }
}
