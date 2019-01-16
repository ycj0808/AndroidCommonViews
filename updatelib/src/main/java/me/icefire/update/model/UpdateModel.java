package me.icefire.update.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 版本信息
 *
 * @author yangchj
 * email yangchj@icefire.me
 * date 2019/1/15
 */
public class UpdateModel {

    private boolean hasUpdate = false;//是否有版本更新
    private boolean isSilent = false;//是否静默下载：有新版本不提示直接下载
    private boolean isForce = false;//是否强制安装：不按照无法使用APP

    private boolean isAutoInstall = true;//是否下载后自动安装
    private boolean isIgnorable = true;//是否可忽略该版本
    private int maxTimes = 0;//一天内最大提示次数，<1时不限

    private int versionCode;//版本号
    private String versionName;//版本号
    private String updateContent;//更新内容
    private String apkUrl;//下载地址
    private long size;//文件大小

    public boolean isHasUpdate() {
        return hasUpdate;
    }

    public UpdateModel setHasUpdate(boolean hasUpdate) {
        this.hasUpdate = hasUpdate;
        return this;
    }

    public boolean isSilent() {
        return isSilent;
    }

    public UpdateModel setSilent(boolean silent) {
        isSilent = silent;
        return this;
    }

    public boolean isForce() {
        return isForce;
    }

    public UpdateModel setForce(boolean force) {
        isForce = force;
        return this;
    }

    public boolean isAutoInstall() {
        return isAutoInstall;
    }

    public UpdateModel setAutoInstall(boolean autoInstall) {
        isAutoInstall = autoInstall;
        return this;
    }

    public boolean isIgnorable() {
        return isIgnorable;
    }

    public UpdateModel setIgnorable(boolean ignorable) {
        isIgnorable = ignorable;
        return this;
    }

    public int getMaxTimes() {
        return maxTimes;
    }

    public UpdateModel setMaxTimes(int maxTimes) {
        this.maxTimes = maxTimes;
        return this;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public UpdateModel setVersionCode(int versionCode) {
        this.versionCode = versionCode;
        return this;
    }

    public String getVersionName() {
        return versionName;
    }

    public UpdateModel setVersionName(String versionName) {
        this.versionName = versionName;
        return this;
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public UpdateModel setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
        return this;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public UpdateModel setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
        return this;
    }

    public long getSize() {
        return size;
    }

    public UpdateModel setSize(long size) {
        this.size = size;
        return this;
    }

    public static UpdateModel parse(String s) throws JSONException {
        JSONObject o = new JSONObject(s);
        return parse(o);
    }

    private static UpdateModel parse(JSONObject obj) {
        UpdateModel updateModel = new UpdateModel();
        if (obj == null) {
            return updateModel;
        }
        updateModel.setHasUpdate(obj.optBoolean("hasUpdate", false));
        if (!updateModel.hasUpdate) {
            return updateModel;
        }
        updateModel.setSilent(obj.optBoolean("isSilent", false));
        updateModel.setForce(obj.optBoolean("isForce", false));
        updateModel.setAutoInstall(obj.optBoolean("isAutoInstall", !updateModel.isSilent));
        updateModel.setIgnorable(obj.optBoolean("isIgnorable", true));
        updateModel.setVersionCode(obj.optInt("versionCode", 0));
        updateModel.setVersionName(obj.optString("versionName"));
        updateModel.setUpdateContent(obj.optString("updateContent"));
        updateModel.setApkUrl(obj.optString("url"));
        return updateModel;
    }

}
