package com.example.alv_chi.improject.greendao;

import android.util.Log;

import com.example.alv_chi.improject.dao.MessageRecordDao;

import java.util.List;


/**
 * Created by Alv_chi on 2017/4/24.
 */

public class DataBaseUtil {

    private static final String TAG = "DataBaseUtil";
    private static DataBaseUtil dataBaseUtilInstance;
    private static MessageRecordDao messageRecordDao;

    private DataBaseUtil() {
    }

    ;

    public static DataBaseUtil getDataBaseInstance() {
        if (dataBaseUtilInstance == null) {
            dataBaseUtilInstance = new DataBaseUtil();
            messageRecordDao = GreenDaoUtil.getInstance().getDaoSession().getMessageRecordDao();
        }

        return dataBaseUtilInstance;
    }


//    create;
    public void create(MessageRecord messageRecord)
    {
        long rowNum = messageRecordDao.insert(messageRecord);
        if (rowNum>0)
        {
            Log.e(TAG, "create: 消息插入成功！rowNum="+rowNum );
        }else
        {
            Log.e(TAG, "create: 消息插入失败！rowNum="+rowNum );
        }
    }


    //    delete;
    public void delete(MessageRecord messageRecord)
    {
        messageRecordDao.delete(messageRecord);
        Log.e(TAG, "delete: 消息删除成功！" );

    }


    //    update;
    public void update(MessageRecord messageRecord)
    {
       messageRecordDao.update(messageRecord);
        Log.e(TAG, "update: 消息更新成功！" );
    }

    //    retrive;
    public List<MessageRecord> retrive(int numOfMsgRecords,MessageRecord messageRecord)
    {
        List<MessageRecord> messageRecords = messageRecordDao.queryBuilder()
                .where(MessageRecordDao.Properties.MasterUserName.eq(messageRecord.getMasterUserName()))
                .where(MessageRecordDao.Properties.UserName.eq(messageRecord.getUserName()))
                .orderDesc(MessageRecordDao.Properties.Id)
                .limit(numOfMsgRecords)
                .build()
                .list();
        Log.e(TAG, "retrive: 消息查询成功！messageRecords.size()="+messageRecords.size() );
        return messageRecords;



    }



}
