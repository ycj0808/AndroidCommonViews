## android7.0适配
在TargetSdkVersion升级到24时，file:// 在应用间传递将不再被允许，会抛出FileUriExposedException异常。

## 解决方法
应用间共享文件，通过发送content://URI， 并授予临时访问权限，进行此授权最简单的方式，使用FileProvider类。


在此，抽取了一个工具类，一行代码完成Android7以上FileProvider的适配

## 使用
```
implemention ‘me.icefire:fileprovider:1.0.1’
```

工具类 FileProviderCompat：
* FileProviderCompat.getUriForFile 获取文件存储路径

* FileProviderCompat.setIntentDataAndType 设置打开文件类型

## 示例

安装应用
```
private void installApk(){
        File file=new File(Environment.getExternalStorageDirectory(),"abc.apk");
        if (file.exists()){
            Log.d("FILE",file.getAbsolutePath());
            Intent intent=new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //在此处做了适配
            FileProviderCompat.setIntentDataAndType(this,intent,"application/vnd.android.package-archive",file,true);
            startActivity(intent);
        }else{
         Log.d("FILE","文件不存在");
        }
    }
```

拍照
```
    private void takePhotoNoCompress(){
        Intent takePicIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicIntent.resolveActivity(getPackageManager())!=null){
            String filename=System.currentTimeMillis()+".png";
            File file=new File(Environment.getDownloadCacheDirectory(),filename);
            mCurrentPhotoPath=file.getAbsolutePath();
            //此处做了Android7的适配
            Uri fileUri=FileProviderCompat.getUriForFile(this,file);
            takePicIntent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);
            startActivityForResult(takePicIntent,REQUEST_CODE_TAKE_PHOTO);
        }
    }
```