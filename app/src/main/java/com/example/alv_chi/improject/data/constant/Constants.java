package com.example.alv_chi.improject.data.constant;

/**
 * Created by Alv_chi on 2017/2/16.
 */

public class Constants {
    public static class DatabaseConstants {
        public static final long LOGIN_INFO_DB_ID = 1L;
    }

    public static class FileNameConstants {
        public static final String RECEIVED_IMAGES_PATH = "IMProjectReceivedImages";
    }

    public static class KeyConstants {
        //        Bundle or Intent key
        public static final String PARCELABLE_BASE_ITEM_KEY = "BaseItem";
        public static final String USER_MESSAGES_RECORD = "UserMessagesRecord";
        public static final String RECEIVE_IMAGE_MSG_FAILED = "receiveImageMsgFailed";
        public static final String SEND_IMAGE_MSG_FAILED = "sendImageMsgFailed";

        //        public static final String PARCELABLE_MESSAGE_ITEM_KEY ="MessageItem";
        public static final String IS_THIS_INTEN_FROM_PENDING_INTENT = "isThisIntenFromPendingIntent";
        public static final String CURRENT_ACCOUNT_IS_LOGINED_BY_OTHERS_EXCEPTION = "currentIsLoginedInOtherDevice";

        //        login password and userName
        public static final String MASTER_USER_LOGIN_NAME = "UserName";
        public static final String OPENFIRE_SERVER_IP = "ServerIP";
        public static final String MASTER_USER_LOGIN_PASSWORD = "Password";

        //        key for is the user you are chatting to onLine
        public static final String IS_ONLINE = "I m Online";


        //        student account parameters/arguements
        public static final String STUDENT_EMAIL = "email";
        public static final String STUDENT_ID = "Id";
        public static final String STUDENT_REGISTER_DATE = "Id";


        //        startActivityForResult RequestCode:
        public static final int LOAD_SYSTEM_IMAGE_REQUEST_CODE =123;

    }

    public static class AppConfigConstants {
        //                public static final String OPEN_FIRE_SERVER_IP = "172.27.156.177";
        public static final String OPEN_FIRE_SERVER_IP = "192.168.23.1";

        public static final String OPEN_FIRE_SERVER_HOST_NAME = "windows10.microdone.cn";
        public static final String OPEN_FIRE_SERVER_DOMAIN_NAME = "windows10.microdone.cn";
        public static final int OPEN_FIRE_SERVER_LOGIN_PORT = 5222;

//        public static final String CLIENT_USER_NAME = "植钟标";
//        public static final String CLIENT_PASSWORD = "zhizhongbiao";
//        public static final String CLIENT_EMAIL = "zhizhongbiao@test.c/om";

        public static final String CLIENT_USER_NAME = "b";
        public static final String CLIENT_PASSWORD = "zhizhongbiao";
        public static final String CLIENT_EMAIL = "B@test.com";

        //        public static final String OTHER_EMAIL = "Test@test.com";
        public static boolean ifNeedToLoginManually = true;



        public static String CURRENT_ACCOUNT_IS_LOGINED_IN_OTHER_DEVICE_EXCEPTION_MESSAGE =
                "conflict You can read more about the meaning";

//      request system permissions
        public static final int MY_PERMISSIONS_REQUEST_LOAD_IMAGES = 7;

    }

    public static class HandlerMessageType {
        public static final int SUCCESS = 0;
        public static final int BIND_SERVICE_SUCCESS = 1;
        public static final int FAILURE = -1;
        public static final int HUMAN_FAILURE = -2;
        public static final int SERVER_FAILURE = -3;

    }
}
