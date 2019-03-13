package io.hussam.westarmy.onpullman.data.model;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class Pullman {

    private final int id;
    private final String title;


    public Pullman(int id, String title) {
        this.id = id;
        this.title = title;
    }


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @NonNull
    @Override
    public String toString() {
        return title;
    }

    public static List<Pullman> getPullmanList() {
        List<Pullman> list = new ArrayList<>(6);
        int index = 0;
        list.add(new Pullman(index++, "براني اسكندريه"));
        list.add(new Pullman(index++, "براني القاهرة"));
        list.add(new Pullman(index++, "المشيفة اسكندريه"));
        list.add(new Pullman(index++, "المشيفة القاهرة"));
        list.add(new Pullman(index++, "السلوم اسكندريه"));
        list.add(new Pullman(index++, "السلوم القاهرة"));
        return list;
    }
}
