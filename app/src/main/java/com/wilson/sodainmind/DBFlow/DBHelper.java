package com.wilson.sodainmind.DBFlow;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

public class DBHelper {

    public static List<CountryDBModel> getAllCountries() {
        return SQLite.select().from(CountryDBModel.class).orderBy(CountryDBModel_Table.name, true).queryList();
    }

    public static List<CountryDBModel> getSelectedCountries() {
        return SQLite.select().from(CountryDBModel.class).where(CountryDBModel_Table.selected.is(true)).queryList();
    }

    public static long getAllCountriesCount() {
        return SQLite.selectCountOf().from(CountryDBModel.class).count();
    }

    public static long getSelectedCountriesCount() {
        return SQLite.selectCountOf().from(CountryDBModel.class).where(CountryDBModel_Table.selected.is(true)).count();
    }

}
