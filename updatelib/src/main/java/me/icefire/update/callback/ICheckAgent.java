package me.icefire.update.callback;

import me.icefire.update.error.UpdateError;

/**
 * 版本检测监听
 * @author yangchj
 * email yangchj@icefire.me
 * date 2019/1/15
 */
public interface ICheckAgent {

    void setInfo(String info);

    void setError(UpdateError updateError);
}
