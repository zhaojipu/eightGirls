package com.yunbao.phonelive.bean;

import java.util.List;

/**
 * Created by Administrator on 2019/6/10 0010.
 */

public class HotBean {
    private List<Slide> slide;

    public List<Slide> getSlide() {
        return slide;
    }

    public void setSlide(List<Slide> slide) {
        this.slide = slide;
    }

    public class Slide{
        private String slide_pic;
        private String slide_url;

        public String getSlide_pic() {
            return slide_pic;
        }

        public void setSlide_pic(String slide_pic) {
            this.slide_pic = slide_pic;
        }

        public String getSlide_url() {
            return slide_url;
        }

        public void setSlide_url(String slide_url) {
            this.slide_url = slide_url;
        }
    }
}
