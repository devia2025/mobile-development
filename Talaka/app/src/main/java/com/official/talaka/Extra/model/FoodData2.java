
package com.official.talaka.Extra.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FoodData2 {

    @SerializedName("Article")
    @Expose
    private List<Recommended> allmenu1 = null;
    public List<Recommended> getAllmenu1() {
        return allmenu1;
    }
}