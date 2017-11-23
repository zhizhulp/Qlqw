package com.ascba.rebate.bean;

import java.util.List;

/**
 * Created by 李平 on 2017/11/7 16:35
 * Describe:邀请奖励页实体类
 */

public class InviteAll {

    /**
     * allInvite : [{"details":"5141邀请好友尾号0823成功加入,奖励10000积分"},{"details":"5141邀请好友尾号8704成功加入,奖励10000积分"},{"details":"5141邀请好友尾号9835成功加入,奖励10000积分"}]
     * personInvite : 3
     * sumInvite : 30000
     * memInvite : [{"nickname":"小灰灰3","avatar":"http://www.qlqw.comhttp://","create_time":"2017.11.07","white_score":10000},{"nickname":"小灰灰2","avatar":"http://www.qlqw.comhttp://","create_time":"2017.11.07","white_score":10000},{"nickname":"小灰灰1","avatar":"http://www.qlqw.comhttp://","create_time":"2017.11.07","white_score":10000}]
     */
    private String invitePic;
    private int personInvite;
    private int sumInvite;
    private List<Attention> allInvite;
    private List<RecAward> memInvite;
    private String inviteRules;
    /**
     * memberFriends : {"image":"http://www.qlqw.com/public/uploads/indexPic/courtesy.jpg","title":"哇！花多少赚多少，京东都怕了。","subtitle":"商品多，配送快，品质好，天天省钱，消费能赚钱，快快体验吧！","courtesy_img":"http://www.qlqw.com/public/uploads/indexPic/zhuan.jpg","courtesy_url":"http://www.qlqw.com/index/Reg/getVerify?p_referee=84&from_type=1"}
     * memberCircle : {"image":"http://www.qlqw.com/public/uploads/indexPic/courtesy.jpg","title":"哇！花多少赚多少，京东都怕了。","subtitle":"商品多，配送快，品质好，天天省钱，消费能赚钱，快快体验吧！","courtesy_img":"http://www.qlqw.com/public/uploads/indexPic/zhuan.jpg","courtesy_url":"http://www.qlqw.com/index/Reg/getVerify?p_referee=84&from_type=2"}
     */

    private MemberFriends memberFriends;
    private MemberCircle memberCircle;

    public String getInviteRules() {
        return inviteRules;
    }

    public void setInviteRules(String inviteRules) {
        this.inviteRules = inviteRules;
    }

    public String getInvitePic() {
        return invitePic;
    }

    public void setInvitePic(String invitePic) {
        this.invitePic = invitePic;
    }

    public int getPersonInvite() {
        return personInvite;
    }

    public void setPersonInvite(int personInvite) {
        this.personInvite = personInvite;
    }

    public int getSumInvite() {
        return sumInvite;
    }

    public void setSumInvite(int sumInvite) {
        this.sumInvite = sumInvite;
    }

    public List<Attention> getAllInvite() {
        return allInvite;
    }

    public void setAllInvite(List<Attention> allInvite) {
        this.allInvite = allInvite;
    }

    public List<RecAward> getMemInvite() {
        return memInvite;
    }

    public void setMemInvite(List<RecAward> memInvite) {
        this.memInvite = memInvite;
    }

    public MemberFriends getMemberFriends() {
        return memberFriends;
    }

    public void setMemberFriends(MemberFriends memberFriends) {
        this.memberFriends = memberFriends;
    }

    public MemberCircle getMemberCircle() {
        return memberCircle;
    }

    public void setMemberCircle(MemberCircle memberCircle) {
        this.memberCircle = memberCircle;
    }

    public static class Attention {
        /**
         * details : 5141邀请好友尾号0823成功加入,奖励10000积分
         */

        private String details;

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }
    }

    public static class RecAward {
        /**
         * nickname : 小灰灰3
         * avatar : http://www.qlqw.comhttp://
         * create_time : 2017.11.07
         * white_score : 10000
         */

        private String nickname;
        private String avatar;
        private String create_time;
        private String white_score;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getWhite_score() {
            return white_score;
        }

        public void setWhite_score(String white_score) {
            this.white_score = white_score;
        }
    }

    public static class MemberFriends {
        /**
         * image : http://www.qlqw.com/public/uploads/indexPic/courtesy.jpg
         * title : 哇！花多少赚多少，京东都怕了。
         * subtitle : 商品多，配送快，品质好，天天省钱，消费能赚钱，快快体验吧！
         * courtesy_img : http://www.qlqw.com/public/uploads/indexPic/zhuan.jpg
         * courtesy_url : http://www.qlqw.com/index/Reg/getVerify?p_referee=84&from_type=1
         */

        private String image;
        private String title;
        private String subtitle;
        private String courtesy_img;
        private String courtesy_url;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getCourtesy_img() {
            return courtesy_img;
        }

        public void setCourtesy_img(String courtesy_img) {
            this.courtesy_img = courtesy_img;
        }

        public String getCourtesy_url() {
            return courtesy_url;
        }

        public void setCourtesy_url(String courtesy_url) {
            this.courtesy_url = courtesy_url;
        }
    }

    public static class MemberCircle {
        /**
         * image : http://www.qlqw.com/public/uploads/indexPic/courtesy.jpg
         * title : 哇！花多少赚多少，京东都怕了。
         * subtitle : 商品多，配送快，品质好，天天省钱，消费能赚钱，快快体验吧！
         * courtesy_img : http://www.qlqw.com/public/uploads/indexPic/zhuan.jpg
         * courtesy_url : http://www.qlqw.com/index/Reg/getVerify?p_referee=84&from_type=2
         */

        private String image;
        private String title;
        private String subtitle;
        private String courtesy_img;
        private String courtesy_url;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getCourtesy_img() {
            return courtesy_img;
        }

        public void setCourtesy_img(String courtesy_img) {
            this.courtesy_img = courtesy_img;
        }

        public String getCourtesy_url() {
            return courtesy_url;
        }

        public void setCourtesy_url(String courtesy_url) {
            this.courtesy_url = courtesy_url;
        }
    }
}
