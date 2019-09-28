
package com.nade.mywikiapplication.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WikiArticle {

    @SerializedName("batchcomplete")
    @Expose
    private String batchcomplete;
    @SerializedName("continue")
    @Expose
    private Continue _continue;
    @SerializedName("query")
    @Expose
    private Query query;

    public String getBatchcomplete() {
        return batchcomplete;
    }

    public void setBatchcomplete(String batchcomplete) {
        this.batchcomplete = batchcomplete;
    }

    public Continue getContinue() {
        return _continue;
    }

    public void setContinue(Continue _continue) {
        this._continue = _continue;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

}
