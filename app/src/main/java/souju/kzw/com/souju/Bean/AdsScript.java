package souju.kzw.com.souju.Bean;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by Android on 2019/2/27.
 */

public class AdsScript extends RealmObject implements Serializable {
    private String website;
    private String script;

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

}