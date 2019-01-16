package me.icefire.update;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import me.icefire.update.callback.IDownloadAgent;
import me.icefire.update.error.UpdateError;
import me.icefire.update.log.ULog;
import me.icefire.update.utils.Util;

/**
 * @author yangchj
 * email yangchj@icefire.me
 * date 2019/1/15
 */
public class UpdateDownloader extends AsyncTask<Void,Integer,Long> {

    private static final int TIME_OUT = 30000;
    private static final int BUFFER_SIZE = 1024 * 100;

    private final static int EVENT_START=1;
    private final static int EVENT_PROGRESS=2;
    private final static int EVENT_COMPLETE=3;

    private Context mContext;
    private IDownloadAgent mAgent;
    private String mUrl;
    private File mTemp;

    private long mBytesLoaded = 0;
    private long mBytesTotal = 0;
    private long mBytesTemp = 0;
    private long mTimeBegin = 0;
    private long mTimeUsed = 1;
    private long mTimeLast = 0;
    private long mSpeed = 0;

    private HttpURLConnection mConnection;

    public UpdateDownloader(IDownloadAgent agent,Context context,String url,File file){
        mContext=context;
        mAgent=agent;
        mUrl=url;
        mTemp=file;
        if (mTemp.exists()){
            mBytesTemp=mTemp.length();
        }
    }

    public long getBytesLoaded(){
        return mBytesLoaded+mBytesTemp;
    }


    @Override
    protected Long doInBackground(Void... voids) {
        mTimeBegin=System.currentTimeMillis();
        try {
            long result=download();
            if (isCancelled()){
                mAgent.setError(new UpdateError(UpdateError.DOWNLOAD_CANCELLED));
            }else if(result==-1){
                mAgent.setError(new UpdateError(UpdateError.DOWNLOAD_UNKNOWN));
            }else if (!Util.verify(mTemp, mTemp.getName())) {
                mAgent.setError(new UpdateError(UpdateError.DOWNLOAD_VERIFY));
            }

        }catch (UpdateError error){
            mAgent.setError(error);
        }catch (FileNotFoundException e) {
            ULog.logE(e.getMessage());
            mAgent.setError(new UpdateError(UpdateError.DOWNLOAD_DISK_IO));
        } catch (IOException e) {
            ULog.logE(e.getMessage());
            mAgent.setError(new UpdateError(UpdateError.DOWNLOAD_NETWORK_IO));
        }finally {
            if (mConnection!=null){
                mConnection.disconnect();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
       switch (values[0]){
           case EVENT_START:
               mAgent.onStart();
               break;
           case EVENT_PROGRESS:
               long now=System.currentTimeMillis();
               if (now-mTimeLast<900){
                   break;
               }
               mTimeLast=now;
               mTimeUsed=now-mTimeBegin;
               mSpeed=mBytesLoaded*1000/mTimeUsed;
               mAgent.onProgress((int) (getBytesLoaded()*100/mBytesTotal));
               break;
       }
    }

    @Override
    protected void onPostExecute(Long aLong) {
        mAgent.onFinish();
    }

    /**
     * 检查网络连接
     * @throws UpdateError
     */
    void checkNetWork() throws UpdateError{
        if (!Util.checkNetWork(mContext)){
            throw new UpdateError(UpdateError.DOWNLOAD_NETWORK_BLOCKED);
        }
    }

    /**
     * 检查连接状态
     * @throws IOException
     * @throws UpdateError
     */
    void checkStatus() throws IOException,UpdateError{
        int statusCode=mConnection.getResponseCode();
        if (statusCode!=200&&statusCode!=206){
            throw new UpdateError(statusCode+"",UpdateError.DOWNLOAD_HTTP_STATUS);
        }
    }

    /**
     * 检查空间
     * @param loaded
     * @param total
     * @throws UpdateError
     */
    void checkSpace(long loaded,long total) throws UpdateError{
        long storage=getAvailableStorage();
        ULog.logI("need = " + (total - loaded) + " = " + total + " - " + loaded + "\nspace = " + storage);
        if (total-loaded>storage){
            throw new UpdateError(UpdateError.DOWNLOAD_DISK_NO_SPACE);
        }
    }

    /**
     * 创建网路连接
     * @param url
     * @return
     * @throws IOException
     */
    private HttpURLConnection create(URL url) throws IOException{
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Accept", "application/*");
        connection.setConnectTimeout(10000);
        return connection;
    }

    private long download() throws IOException,UpdateError {
        checkNetWork();
        mConnection=create(new URL(mUrl));
        mConnection.connect();
        checkStatus();
        mBytesTotal=mConnection.getContentLength();
        checkSpace(mBytesTemp,mBytesTotal);

        if (mBytesTemp==mBytesTotal){
            publishProgress(EVENT_START);
            return 0;
        }

        if (mBytesTemp>0){
            mConnection.disconnect();
            mConnection=create(mConnection.getURL());
            mConnection.addRequestProperty("Range","bytes="+mBytesTemp+"-");
            mConnection.connect();
            checkStatus();
        }
        publishProgress(EVENT_START);

        int bytesCopied=copy(mConnection.getInputStream(),new LoadingRandomAccessFile(mTemp));
        if (isCancelled()){
        }else if((mBytesTemp + bytesCopied) != mBytesTotal && mBytesTotal != -1){
            ULog.logI("download incomplete(" + mBytesTemp + " + " + bytesCopied + " != " + mBytesTotal + ")");
            throw new UpdateError(UpdateError.DOWNLOAD_INCOMPLETE);
        }
        return bytesCopied;
    }

    private int copy(InputStream in, RandomAccessFile out)throws IOException,UpdateError{
        byte[] buffer = new byte[BUFFER_SIZE];
        BufferedInputStream bis = new BufferedInputStream(in, BUFFER_SIZE);
        try{
            out.seek(out.length());
            int bytes = 0;
            long previousBlockTime = -1;
            while (!isCancelled()) {
                int n = bis.read(buffer, 0, BUFFER_SIZE);
                if (n == -1) {
                    break;
                }
                out.write(buffer, 0, n);
                bytes += n;
                checkNetWork();
                if (mSpeed != 0) {
                    previousBlockTime = -1;
                } else if (previousBlockTime == -1) {
                    previousBlockTime = System.currentTimeMillis();
                } else if ((System.currentTimeMillis() - previousBlockTime) > TIME_OUT) {
                    throw new UpdateError(UpdateError.DOWNLOAD_NETWORK_TIMEOUT);
                }
            }
            return bytes;
        }finally {
            out.close();
            bis.close();
            in.close();
        }
    }

    private final class LoadingRandomAccessFile extends RandomAccessFile{
        public LoadingRandomAccessFile(File file) throws FileNotFoundException {
            super(file, "rw");
        }
        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            super.write(b, off, len);
            mBytesLoaded+=len;
            publishProgress(EVENT_PROGRESS);
        }
    }
    /**
     * 获取可用空间
     * @return
     */
    private static long getAvailableStorage() {
        try {
            StatFs stat = new StatFs(Environment.getExternalStorageDirectory().toString());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                return stat.getAvailableBlocksLong() * stat.getBlockSizeLong();
            } else {
                return (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();
            }
        } catch (RuntimeException ex) {
            return 0;
        }
    }
}
