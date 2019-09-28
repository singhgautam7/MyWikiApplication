
package com.nade.mywikiapplication.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Continue {

    @SerializedName("rncontinue")
    @Expose
    private String rncontinue;
    @SerializedName("continue")
    @Expose
    private String _continue;

    public String getRncontinue() {
        return rncontinue;
    }

    public void setRncontinue(String rncontinue) {
        this.rncontinue = rncontinue;
    }

    public String getContinue() {
        return _continue;
    }

    public void setContinue(String _continue) {
        this._continue = _continue;
    }

}
