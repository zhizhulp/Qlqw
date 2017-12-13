package com.ascba.rebate.bean;

public class MctPayTitle extends MctBasePay {
    private String title;
    private boolean showLine;
    private boolean showSpace = true;

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

    public MctPayTitle(String title, boolean showLine, boolean showSpace) {
        this.title = title;
        this.showLine = showLine;
        this.showSpace = showSpace;
    }

    public boolean isShowLine() {
        return showLine;
    }

    public void setShowLine(boolean showLine) {
        this.showLine = showLine;
    }

    public boolean isShowSpace() {
        return showSpace;
    }

    public void setShowSpace(boolean showSpace) {
        this.showSpace = showSpace;
    }
}
