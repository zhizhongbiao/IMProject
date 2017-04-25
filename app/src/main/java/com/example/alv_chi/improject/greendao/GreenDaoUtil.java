package com.example.alv_chi.improject.greendao;

import android.database.sqlite.SQLiteDatabase;

import com.example.alv_chi.improject.application.MyApp;
import com.example.alv_chi.improject.dao.DaoMaster;
import com.example.alv_chi.improject.dao.DaoSession;

/**
 * Created by Alv_chi on 2017/4/24.
 */

public class GreenDaoUtil {

    private static GreenDaoUtil greenDaoUtilInstance;
    private DaoMaster.DevOpenHelper devOpenHelper;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    private GreenDaoUtil() {
    }

    ;

    public static GreenDaoUtil getInstance() {
        if (greenDaoUtilInstance == null) {
            greenDaoUtilInstance = new GreenDaoUtil();
        }
        return greenDaoUtilInstance;
    }

    private void initialGreeenDao() {
        devOpenHelper = new DaoMaster.DevOpenHelper(MyApp.getMyAppContext(), "IMProject_MessageRecord_db", null);
        db = devOpenHelper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        if (daoSession == null) {
            initialGreeenDao();
        }
        return daoSession;
    }

    public SQLiteDatabase getDb() {
        if (db == null) {
            initialGreeenDao();
        }
        return db;
    }

}
