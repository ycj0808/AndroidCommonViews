package me.icefire.update;

import java.net.HttpURLConnection;
import java.net.URL;

import me.icefire.update.callback.ICheckAgent;
import me.icefire.update.callback.IUpdateChecker;
import me.icefire.update.error.UpdateError;
import me.icefire.update.log.ULog;
import me.icefire.update.utils.Util;

/**
 * 更新检测默认实现
 * @author yangchj
 * email yangchj@icefire.me
 * date 2019/1/15
 */
public class UpdateChecker implements IUpdateChecker {

    final byte[] mPostData;

    public UpdateChecker(){
        mPostData=null;
    }

    public UpdateChecker(byte[] mData) {
        this.mPostData = mData;
    }

    @Override
    public void check(ICheckAgent agent, String url) {
        HttpURLConnection connection = null;
        try{
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestProperty("Accept", "application/json");

            if (mPostData==null){
                connection.setRequestMethod("GET");
                connection.connect();
            }else{
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setInstanceFollowRedirects(false);
                connection.setUseCaches(false);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("Content-Length",Integer.toString(mPostData.length));
                connection.getOutputStream().write(mPostData);
            }

            if (connection.getResponseCode()==HttpURLConnection.HTTP_OK){
                agent.setInfo(Util.readString(connection.getInputStream()));
            }else{
                agent.setError(new UpdateError(connection.getResponseCode()+"",UpdateError.CHECK_HTTP_STATUS));
            }
        }catch (Exception e){
            ULog.logE(e.getMessage());
        }
    }
}
