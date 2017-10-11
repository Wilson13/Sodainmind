package com.wilson.sodainmind.DBFlow;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = AppDatabase.class)
public class CountryDBModel extends BaseModel{

    @Column
    @PrimaryKey
    int number;

    @Column
    int order;

    @Column
    String name;

    @Column
    String alpha2;

    @Column
    String alpha3;

    @Column
    boolean selected;

    public void insertRow(int number, String name, String alpha2, String alpha3, boolean selected) {
        this.number = number;
        this.name = name;
        this.alpha2 = alpha2;
        this.alpha3 = alpha3;
        this.selected = selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getName() {
        return this.name;
    }

    public String alpha2() {
        return this.alpha2;
    }

    public String getAlpha3() {
        return this.alpha3;
    }

    public boolean isSelected() {
        return selected;
    }
}
