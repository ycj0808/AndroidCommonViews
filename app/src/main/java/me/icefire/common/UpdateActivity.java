package me.icefire.common;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.json.JSONObject;

import me.icefire.update.UpdateManager;
import me.icefire.update.callback.IUpdateParser;
import me.icefire.update.model.UpdateModel;

/**
 * @author yangchj
 * @email yangchj@icefire.me
 * @date 2019/1/16
 */
public class UpdateActivity extends AppCompatActivity {

    String mCheckUrl = "https://raw.githubusercontent.com/yjfnypeu/UpdatePlugin/master/update.json";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check(false,true,true,false,true,10086);
            }
        });
    }

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
}
