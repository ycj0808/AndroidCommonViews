## 简介
该库是应用内更新库：
* 提供了默认检测更新，默认下载，默认提示对话框，默认下载进度对话框

* 同时也提供了自定义接口，可以实现检测更新自定义，提示对话框自定义，下载进度对话框自定义

* 适配了android7.0及以上FileProvider


## 引用
```

implementation 'me.icefire:updatelib:1.0.1'
```

## 使用
```

/**
     * 检测更新
     * @param isManual
     * @param hasUpdate
     * @param isForce
     * @param isSilent
     * @param isIgnore
     * @param notifyId
     */
    void check(boolean isManual, final boolean hasUpdate, final boolean isForce, final boolean isSilent, final boolean isIgnore, final int notifyId){
        UpdateManager.create(this)
                .setUrl(mCheckUrl)
                .setManual(isManual)
                .setNotifyId(notifyId)
                .setParser(new IUpdateParser() {
                    @Override
                    public UpdateModel parse(String source) throws Exception {
                        Log.d("UPDATE",source);
                        UpdateModel updateModel=new UpdateModel();
                        try{
                            JSONObject jsonObject=new JSONObject(source);
                            updateModel.setHasUpdate(hasUpdate);
                            updateModel.setForce(isForce);
                            updateModel.setSilent(isSilent);
                            updateModel.setIgnorable(isIgnore);
                            updateModel.setVersionName(jsonObject.optString("update_ver_name","1.0.1"));
                            updateModel.setVersionCode(jsonObject.optInt("update_ver_code",101));
                            updateModel.setApkUrl(jsonObject.optString("update_url"))
                                    .setUpdateContent(jsonObject.optString("update_content"));
                        }catch (Exception e){
                        }
                        return updateModel;
                    }
                }).check();
    }
```

* 自定义设置
```
1.定制查询
UpdateManager.create(this).setUrl(mCheckUrl).setChecker(new IUpdateChecker() {
    @Override
    public void check(ICheckAgent agent, String url) {
        //自定义检查
        agent.setInfo(UpdateModel updateModel);
        or
        agent.setError(new UpdateError(UpdateError.CHECK_HTTP_STATUS);//错误反馈
    }
}).check();

2.定制下载

UpdateManager.create(this).setUrl(mCheckUrl).setDownloader(new IUpdateDownloader() {
    @Override
    public void download(IDownloadAgent agent, String url, File temp) {
        //自定义下载
    }
}).check();

3.更新对话框
UpdateManager.create(this).setPrompter(new IUpdatePrompter() {
    @Override
    public void prompt(IUpdateAgent agent) {
        // todo : 根据 agent.getInfo() 显示更新版本对话框，具体可参考 UpdateAgent.DefaultUpdatePrompter
    }
}).check();

4.没有新版本或出错
UpdateManager.create(this).setOnFailure(new OnFailureListener() {
    @Override
    public void onFailure(UpdateError error) {
        Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
    }
}).check();

5.显示下载进度

如果是静默安装，默认是通知栏显示进度

定制下载进度的对话框
UpdateManager.create(this).setOnDownloadListener(new OnDownloadListener() {
    @Override
    public void onStart() {
        // todo: start
    }

    @Override
    public void onProgress(int progress) {
        // todo: progress
    }

    @Override
    public void onFinish() {
        // todo: finish
    }
}).check();
```

