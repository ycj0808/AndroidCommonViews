package me.icefire.update.callback;

import me.icefire.update.model.UpdateModel;

/**
 * @author yangchj
 * email yangchj@icefire.me
 * date 2019/1/16
 */
public interface IUpdateParser {
    UpdateModel parse(String source) throws Exception;
}
