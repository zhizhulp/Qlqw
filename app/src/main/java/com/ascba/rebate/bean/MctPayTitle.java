package com.ascba.rebate.bean;

public class MctPayTitle extends MctBasePay {
    private String title;
    private boolean showLine;

    @Override
    public int getItemType() {
        return ITEM_TYPE_TITLE;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MctPayTitle(String title) {
        this.title = title;
    }

    public MctPayTitle(String title, boolean showLine) {
        this.title = title;
        this.showLine = showLine;
    }

    public boolean isShowLine() {
        return showLine;
    }

    public void setShowLine(boolean showLine) {
        this.showLine = showLine;
    }
}
