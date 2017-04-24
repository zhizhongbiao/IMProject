package com.example.alv_chi.improject.greendao;

/**
 * Created by Alv_chi on 2017/4/24.
 */

public class GreenDaoUtil {

    private static GreenDaoUtil greenDaoUtilInstance;

    private GreenDaoUtil(){};

    public static GreenDaoUtil getInstance()
    {
        if (greenDaoUtilInstance==null)
        {
            greenDaoUtilInstance=new GreenDaoUtil();
        }
        return greenDaoUtilInstance;
    }

    private void initialGreeenDao()
    {
//        new DaoMaster.DevOpenHelper()
    }

}
