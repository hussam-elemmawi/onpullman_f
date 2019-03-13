package io.hussam.westarmy.onpullman.data.model;

import androidx.annotation.NonNull;

public class DayInfo {
    private final String date;
    private final int pullmanId;

    public DayInfo(String d, int id) {
        date = d;
        pullmanId = id;
    }

    public String getDate() {
        return date;
    }

    public int getPullmanId() {
        return pullmanId;
    }

    @NonNull
    @Override
    public String toString() {
        return "[pullmanId: " + pullmanId + ", date: " + date + "]";
    }
}
