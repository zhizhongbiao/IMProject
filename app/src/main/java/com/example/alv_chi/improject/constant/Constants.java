package com.example.alv_chi.improject.constant;

/**
 * Created by Alv_chi on 2017/2/16.
 */

public class Constants {
    public static class URLConstants {

    }

    public static class FileNameConstants {

    }

    public static class KeyConstants {
        //        Bundle or Intent key
        public static final String PARCELABLE_BASE_ITEM_KEY = "BaseItem";
        public static final String USER_MESSAGES_RECORD = "UserMessagesRecord";
        //        public static final String PARCELABLE_MESSAGE_ITEM_KEY ="MessageItem";
        public static final String IS_THIS_INTEN_FROM_PENDING_INTENT = "isThisIntenFromPendingIntent";

        //        login password and userName
        public static final String MASTER_USER_LOGIN_NAME = "UserName";
        public static final String MASTER_USER_LOGIN_PASSWORD = "Password";

        //        key for is the user you are chatting to onLine
        public static final String IS_ONLINE = "I m Online";


        //        student account parameters/arguements
        public static final String STUDENT_EMAIL = "email";
        public static final String STUDENT_ID = "Id";
        public static final String STUDENT_REGISTER_DATE = "Id";
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
        public static boolean isNeedToLogin = false;
    }

    public static class HandlerMessageType {
        public static final int SUCCESS = 0;
        public static final int FAILURE = -1;

    }
}
