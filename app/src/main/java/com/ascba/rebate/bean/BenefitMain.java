package com.ascba.rebate.bean;

import java.util.List;

/**
 * Created by 李平 on 2017/10/30 11:27
 * Describe:寄卖收益
 */

public class BenefitMain {

    /**
     * member : {"ranking":"第6名","on_sale":1,"today_money":"25.32","need_money":"80.00"}
     * ranking_list : [{"avatar":"/public/base/images/default.jpg?1497844072?1497844075?1498256843","realname":"史思晨","red_score":"470.38元"},{"avatar":"/public/uploads/avatar/33/331505788439887.png","realname":"王文超","red_score":"445.85元"},{"avatar":"","realname":"赵林生","red_score":"432.00元"},{"avatar":"/public/uploads/avatar/36/36.png","realname":"郭春娥","red_score":"55.97元"},{"avatar":"/public/uploads/avatar/94/941505817081235.png","realname":"赵苗苗","red_score":"38.73元"},{"avatar":"/public/uploads/avatar/5022/50221504236020.png","realname":"汪炳旭","red_score":"25.32元"},{"avatar":"","realname":"梁清虎","red_score":"17.17元"},{"avatar":"/public/uploads/avatar/98/98.png?1498923047","realname":"邓艳伟","red_score":"11.40元"},{"avatar":"/public/uploads/avatar/84/841506655316378.png","realname":"聂国富","red_score":"11.34元"},{"avatar":"/public/uploads/avatar/89/89.png","realname":"刘明阳","red_score":"11.34元"}]
     */

    private ScoreHome.Member member;
    private List<Benefit> ranking_list;

    public ScoreHome.Member getMember() {
        return member;
    }

    public void setMember(ScoreHome.Member member) {
        this.member = member;
    }

    public List<Benefit> getRanking_list() {
        return ranking_list;
    }

    public void setRanking_list(List<Benefit> ranking_list) {
        this.ranking_list = ranking_list;
    }

    public static class Benefit {
        /**
         * avatar : /public/base/images/default.jpg?1497844072?1497844075?1498256843
         * realname : 史思晨
         * red_score : 470.38元
         */

        private String avatar;
        private String realname;
        private String red_score;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getRed_score() {
            return red_score;
        }

        public void setRed_score(String red_score) {
            this.red_score = red_score;
        }
    }
}
