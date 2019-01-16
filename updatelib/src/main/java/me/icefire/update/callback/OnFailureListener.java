package me.icefire.update.callback;

import me.icefire.update.error.UpdateError;

/**
 * 更新失败监听
 *
 * @author yangchj
 * email yangchj@icefire.me
 * date 2019/1/15
 */
public interface OnFailureListener {

    void onFailure(UpdateError updateError);
}
