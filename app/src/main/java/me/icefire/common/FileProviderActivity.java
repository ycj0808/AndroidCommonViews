package me.icefire.common;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import me.icefire.common.fileprovider.FileProviderCompat;

/**
 * @author yangchj
 * @email yangchj@icefire.me
 * @date 2019/1/18
 */
public class FileProviderActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_TAKE_PHOTO = 0x110;
    private static final int REQ_PERMISSION_CODE_SDCARD = 0X111;
    private static final int REQ_PERMISSION_CODE_TAKE_PHOTO = 0X112;
    private String mCurrentPhotoPath;
    private ImageView mIvPhoto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fileprovider);
        mIvPhoto = findViewById(R.id.id_iv);
    }

    public void installApk(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQ_PERMISSION_CODE_SDCARD);
        }else{
            installApk();//安装apk
        }
    }

    private void installApk(){
        File file=new File(Environment.getExternalStorageDirectory(),"xnxh.apk");
        if (file.exists()){
            Log.d("FILE",file.getAbsolutePath());
            Intent intent=new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            FileProviderCompat.setIntentDataAndType(this,intent,"application/vnd.android.package-archive",file,true);
            startActivity(intent);
        }else{
         Log.d("FILE","文件不存在");
        }
    }

    public void takePhotoNoCompress(View view){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQ_PERMISSION_CODE_TAKE_PHOTO);
        } else {
            takePhotoNoCompress();
        }
    }

    private void takePhotoNoCompress(){
        Intent takePicIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicIntent.resolveActivity(getPackageManager())!=null){
            String filename=System.currentTimeMillis()+".png";
            File file=new File(Environment.getDownloadCacheDirectory(),filename);
            mCurrentPhotoPath=file.getAbsolutePath();
            Uri fileUri=FileProviderCompat.getUriForFile(this,file);
            takePicIntent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);
            startActivityForResult(takePicIntent,REQUEST_CODE_TAKE_PHOTO);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==REQ_PERMISSION_CODE_SDCARD){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                installApk();
            }else{
                Toast.makeText(FileProviderActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }else if(requestCode == REQ_PERMISSION_CODE_TAKE_PHOTO){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhotoNoCompress();
            } else {
                Toast.makeText(FileProviderActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RESULT_OK&&requestCode==REQUEST_CODE_TAKE_PHOTO){
            mIvPhoto.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));
        }
    }
}
