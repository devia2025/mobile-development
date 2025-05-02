
package com.official.tmrnty.Extra.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Recommended {

    @SerializedName("CONVERT(ID, CHAR)")
    @Expose
    private String id;

    @SerializedName("Lvl")
    @Expose
    private String lvl;

    @SerializedName("Subject")
    @Expose
    private String subject;

    @SerializedName("Description")
    @Expose
    private String description;

    @SerializedName("Url")
    @Expose
    private String url;

    @SerializedName("Image")
    @Expose
    private String image;

    @SerializedName("Rate")
    @Expose
    private String rate;

    @SerializedName("Views")
    @Expose
    private String views;

    @SerializedName("Likes")
    @Expose
    private String likes;

    @SerializedName("Type")
    @Expose
    private String type;

    public String getid() {return id;}
    public void setid(String id) {
        this.id = id;
    }

//    public String getid2() {return id_str;}
//    public void setid2(String id) {
//        this.id_str = id;
//    }

    public String getlvl() {
        return lvl;
    }
    public void setlvl(String lvl) {
        this.lvl = lvl;
    }

    public String getsubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getdescription() {
        return description;
    }
    public void setdescription(String description) {
        this.description = description;
    }

    public String geturl() {
        return url;
    }
    public void seturl(String url) {
        this.url = url;
    }

    public String getimage() {
        if(image.matches("img")){
            return image = "https://raw.githubusercontent.com/DevIA3kl/other/master/more/img.png";
        }else {
            return image;
        }
    }
    public void setimage(String image) {
        this.image = image;
    }

    public String getrate() {
        return rate;
    }
    public void setrate(String rate) {
        this.rate = rate;
    }

    public String getviews() {
        return views;
    }
    public void setviews(String views) {
        this.views = views;
    }

    public String getlikes() {
        int count = this.likes.length() - this.likes.replace(",", "").length();
        return likes = Integer.toString(count);
      //  return likes;
    }
    public void setlikes(String likes) {
        this.likes = likes;
    }

    public String gettype() {
        return type;
    }
    public void settype(String type) {
        this.type = type;
    }

}
