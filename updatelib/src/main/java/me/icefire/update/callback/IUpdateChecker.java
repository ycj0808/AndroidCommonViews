package me.icefire.update.callback;

/**
 * 更新检测
 * @author yangchj
 * email yangchj@icefire.me
 * date 2019/1/15
 */
public interface IUpdateChecker {
    void check(ICheckAgent agent,String url);
}
