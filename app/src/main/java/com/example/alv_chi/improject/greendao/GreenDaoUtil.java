package com.example.alv_chi.improject.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.alv_chi.improject.greendao.dao.DaoMaster;
import com.example.alv_chi.improject.greendao.dao.DaoSession;

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

    private void initialGreeenDao(Context appContext) {
        devOpenHelper = new DaoMaster.DevOpenHelper(appContext, "IMProject_MessageRecord_db", null);
        db = devOpenHelper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession(Context appContext) {
        if (daoSession == null) {
            initialGreeenDao(appContext);
        }
        return daoSession;
    }

    public SQLiteDatabase getDb(Context appContext) {
        if (db == null) {
            initialGreeenDao(appContext);
        }
        return db;
    }

}
