//package com.example.alv_chi.improject.activity;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import com.example.alv_chi.improject.R;
//
//import org.jivesoftware.smack.SmackException;
//import org.jivesoftware.smack.XMPPConnection;
//import org.jivesoftware.smack.XMPPException;
//import org.jivesoftware.smackx.filetransfer.FileTransfer;
//import org.jivesoftware.smackx.filetransfer.FileTransferListener;
//import org.jivesoftware.smackx.filetransfer.FileTransferManager;
//import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
//import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
//import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
//
//import java.io.File;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import static org.jivesoftware.smackx.filetransfer.FileTransfer.Error.connection;
//
///**
// * Created by Alv_chi on 2017/5/5.
// */
//
//public class DynamicRequestPermissionDemo
//        extends AppCompatActivity {
//    private ImageView imageView;
//    private static final int CROP_PHOTO = 2;
//    private static final int REQUEST_CODE_PICK_IMAGE = 3;
//    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 6;
//    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 7;
//    private File output;
//    private Uri imageUri;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        initView();
//    }
//
//    void initView() {
//        imageView = (ImageView) findViewById(R.id.image);
//    }
//
//    public void takePhone(View view) {
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    MY_PERMISSIONS_REQUEST_CALL_PHONE2);
//
//        } else {
//            takePhoto();
//        }
//
//    }
//
//    public void choosePhone(View view) {
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    MY_PERMISSIONS_REQUEST_CALL_PHONE2);
//
//        } else {
//            choosePhoto();
//        }
//    }
//
//    /**
//     * 拍照
//     */
//    void takePhoto() {
//        /**
//         * 最后一个参数是文件夹的名称，可以随便起
//         */
//        File file = new File(Environment.getExternalStorageDirectory(), "拍照");
//        if (!file.exists()) {
//            file.mkdir();
//        }
//        /**
//         * 这里将时间作为不同照片的名称
//         */
//        output = new File(file, System.currentTimeMillis() + ".jpg");
//
//        /**
//         * 如果该文件夹已经存在，则删除它，否则创建一个
//         */
//        try {
//            if (output.exists()) {
//                output.delete();
//            }
//            output.createNewFile();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        /**
//         * 隐式打开拍照的Activity，并且传入CROP_PHOTO常量作为拍照结束后回调的标志
//         */
//        imageUri = Uri.fromFile(output);
//        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        startActivityForResult(intent, CROP_PHOTO);
//
//    }
//
//    /**
//     * 从相册选取图片
//     */
//    void choosePhoto() {
//        /**
//         * 打开选择图片的界面
//         */
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");//相片类型
//        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
//
//    }
//
//    public void onActivityResult(int req, int res, Intent data) {
//        switch (req) {
//            /**
//             * 拍照的请求标志
//             */
//            case CROP_PHOTO:
//                if (res == RESULT_OK) {
//                    try {
//                        /**
//                         * 该uri就是照片文件夹对应的uri
//                         */
//                        Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
//                        imageView.setImageBitmap(bit);
//                    } catch (Exception e) {
//                        Toast.makeText(this, "程序崩溃", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Log.i("tag", "失败");
//                }
//
//                break;
//            /**
//             * 从相册中选取图片的请求标志
//             */
//
//            case REQUEST_CODE_PICK_IMAGE:
//                if (res == RESULT_OK) {
//                    try {
//                        /**
//                         * 该uri是上一个Activity返回的
//                         */
//                        Uri uri = data.getData();
//                        Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
//                        imageView.setImageBitmap(bit);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Log.d("tag", e.getMessage());
//                        Toast.makeText(this, "程序崩溃", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Log.i("liang", "失败");
//                }
//
//                break;
//
//            default:
//                break;
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//
//        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                takePhoto();
//            } else {
//                // Permission Denied
//                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
//            }
//        }
//
//
//        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE2) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                choosePhoto();
//            } else {
//                // Permission Denied
//                Toast.makeText(DynamicRequestPermissionDemo.this, "Permission Denied", Toast.LENGTH_SHORT).show();
//            }
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
//
//
//    public void sendFire(XMPPConnection conn, String toUser, String filePath) throws XMPPException {
//        //conn必须是已经登录了的
//        //注册toUser只在一个地方登录过，多处登录有可能会出现接收主收不到请求的问题
//        FileTransferManager ftm = new FileTransferManager(conn);
//    /*
//     * 检查传递的用户是否正确
//     * toUser="user2@192.168.1.100"
//     * 192.168.1.100 为openfire服务器地址
//    */
//        Presence p = connection.getRoster().getPresence(toUser);
//        if (p == null) {
//            System.out.println("用户不存在");
//            return;
//        }
//        toUser = p.getFrom();//提取完整的用户名称
//        OutgoingFileTransfer oft = ftm.createOutgoingFileTransfer(toUser);
//
//        //"get my file"可以是随意字符串，就是一个携带信息
//        oft.sendFile(new File(filePath), "get my file");
//        System.out.println("sending file status=" + oft.getStatus());
//        long startTime = -1;
//        while (!oft.isDone()) {
//            if (oft.getStatus().equals(Status.error)) {
//                System.out.println("error!!!" + oft.getError());
//            } else {
//                double progress = oft.getProgress();
//                if (progress > 0.0 && startTime == -1) {
//                    startTime = System.currentTimeMillis();
//                }
//                progress *= 100;
//                System.out.println("status=" + oft.getStatus());
//                System.out.println("progress=" + nf.format(progress) + "%");
//            }
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        System.out.println("used " + ((System.currentTimeMillis() - startTime) / 1000) + " seconds  ");
//    }
//
//
//    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//    public void startRecvFileListen(XMPPConnection conn) {
//        //conn必须是已经登录了的
//        FileTransferManager manager = new FileTransferManager(conn);
//        //添加文件接收监听器
//        manager.addFileTransferListener(new FileTransferListener() {
//            //每次有文件发送过来都会调用些方法
//            public void fileTransferRequest(FileTransferRequest request) {
//                //调用request的accetp表示接收文件，也可以调用reject方法拒绝接收
//                final IncomingFileTransfer inTransfer = request.accept();
//                try {
//                    System.out.println("接收到文件发送请求，文件名称：" + request.getFileName());
//                    //接收到的文件要放在哪里
//                    String filePath = "D:\\datas\\smackclient\\" + request.getFileName();
//                    inTransfer.recieveFile(new File(filePath));
//                    //如果要时时获取文件接收的状态必须在线程中监听，如果在当前线程监听文件状态会导致一下接收为0
//                    new Thread() {
//                        @Override
//                        public void run() {
//                            long startTime = System.currentTimeMillis();
//                            while (!inTransfer.isDone()) {
//                                if (inTransfer.getStatus().equals(FileTransfer.Status.error)) {
//                                    System.out.println(sdf.format(new Date()) + "error!!!" + inTransfer.getError());
//                                } else {
//                                    double progress = inTransfer.getProgress();
//                                    progress *= 100;
//                                    System.out.println(sdf.format(new Date()) + "status=" + inTransfer.getStatus());
//                                    System.out.println(sdf.format(new Date()) + "progress=" + nf.format(progress) + "%");
//                                }
//                                try {
//                                    Thread.sleep(1000);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                            System.out.println("used " + ((System.currentTimeMillis() - startTime) / 1000) + " seconds  ");
//                        }
//                    }.start();
//                } catch (XMPPException e) {
//
//                    e.printStackTrace();
//                } catch (SmackException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        System.out.println(connection.getUser() + "--" + connection.getServiceName() + "开始监听文件传输");
//    }
//
//
//}