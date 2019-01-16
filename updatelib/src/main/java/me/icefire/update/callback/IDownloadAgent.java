package me.icefire.update.callback;

import me.icefire.update.error.UpdateError;
import me.icefire.update.model.UpdateModel;

/**
 * 下载
 * @author yangchj
 * email yangchj@icefire.me
 * date 2019/1/15
 */
public interface IDownloadAgent extends OnDownloadListener{

    UpdateModel getUpdateInfo();

    void setError(UpdateError updateError);
}
