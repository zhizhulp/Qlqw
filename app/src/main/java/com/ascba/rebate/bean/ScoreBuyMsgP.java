package com.ascba.rebate.bean;

import java.util.List;

public class ScoreBuyMsgP extends ScoreBuyBase {
    public List<String> getMsgs() {
        return msgs;
    }

    public void setMsgs(List<String> msgs) {
        this.msgs = msgs;
    }

    private List<String> msgs;
    @Override
    public int getItemType() {
        return 4;
    }
}
