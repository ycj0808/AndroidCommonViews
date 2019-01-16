package me.icefire.update.callback;

import me.icefire.update.model.UpdateModel;

/**
 * @author yangchj
 * email yangchj@icefire.me
 * date 2019/1/15
 */
public interface IUpdateAgent {

    UpdateModel getUpdateInfo();

    void update();

    void ignore();
}
