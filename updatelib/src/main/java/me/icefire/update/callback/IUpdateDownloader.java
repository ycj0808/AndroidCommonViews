package me.icefire.update.callback;

import java.io.File;

/**
 * @author yangchj
 * email yangchj@icefire.me
 * date 2019/1/15
 */
public interface IUpdateDownloader {
    void download(IDownloadAgent agent, String url, File temp);
}
