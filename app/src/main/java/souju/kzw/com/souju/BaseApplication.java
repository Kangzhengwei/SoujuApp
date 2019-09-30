package souju.kzw.com.souju;

import android.app.Application;
import android.util.Log;

import com.tencent.smtt.sdk.QbSdk;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Android on 2019/2/26.
 */

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initX5();
        initDataBase();
    }

    /**
     * 初始化x5内核
     */
    private void initX5() {
        //腾讯x5内核
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                Log.e("app", " onViewInitFinished is " + arg0);
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }

    /**
     * 初始化数据库
     */
    private void initDataBase() {
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("myrealm.realm")
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }

}
