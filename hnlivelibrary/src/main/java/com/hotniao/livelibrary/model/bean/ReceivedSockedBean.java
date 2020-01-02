package com.hotniao.livelibrary.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * @创建者 阳石柏
 * @创建时间 2016/7/7 14:52
 * @描述 ${WebSocket返回数据Bean}
 */
public class ReceivedSockedBean implements Serializable {


    /**
     * type : sendpubmsg
     * data : {"fuser":{"id":"10234","nick":"那么骄傲°","icons":"","avatar":"./20160518/14635352579102.jpg","richlvl":"1","anchorlvl":"1","viplvl":"0","mountid":"0","superadmin":"0","logintype":"login","guardlvl":0,"admin":0},"tuser":{"id":"10210","nick":"像风一样的女子","icons":"","avatar":"./20160518/14635352579078.jpg","richlvl":"1","anchorlvl":"1","viplvl":"0","mountid":"0","superadmin":"0","logintype":"login","guardlvl":0,"admin":0},"msg":"252355"}
     */

    private String    type;
    /**
     * fuser : {"id":"10234","nick":"那么骄傲°","icons":"","avatar":"./20160518/14635352579102.jpg","richlvl":"1","anchorlvl":"1","viplvl":"0","mountid":"0","superadmin":"0","logintype":"login","guardlvl":0,"admin":0}
     * tuser : {"id":"10210","nick":"像风一样的女子","icons":"","avatar":"./20160518/14635352579078.jpg","richlvl":"1","anchorlvl":"1","viplvl":"0","mountid":"0","superadmin":"0","logintype":"login","guardlvl":0,"admin":0}
     * msg : 252355
     */

    private PriseBean fuser;

    private DataBean data;

    private String notice;

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public PriseBean getFuser() {
        return fuser;
    }

    public void setFuser(PriseBean fuser) {
        this.fuser = fuser;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class PriseBean implements Serializable {

        private String nick;

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }
    }

    public static class DataBean implements Serializable {



        private  String  uid;

       private   String total_dot;

        private String  bigGiftAddress;

        public String getTotal_dot() {
            return total_dot;
        }

        public void setTotal_dot(String total_dot) {
            this.total_dot = total_dot;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getBigGiftAddress() {
            return bigGiftAddress;
        }

        public void setBigGiftAddress(String bigGiftAddress) {
            this.bigGiftAddress = bigGiftAddress;
        }
        //****************************公聊消息start**************************************************************************************//
        /**
         * content : {"word":"ff","add_time":"1505537794"}
         * user_info : {"uid":"100010","nick":"mj","avatar":"","is_vip":"0","gender":"1","level":"1"}
         */

        private ContentBean content;
        private UserInfoBean user_info;


        public static class ContentBean implements Serializable {
            /**
             * word : ff
             * add_time : 1505537794
             */

            @SerializedName("word")
            private String wordX;
            private String add_time;

            public String getWordX() {
                return wordX;
            }

            public void setWordX(String wordX) {
                this.wordX = wordX;
            }

            public String getAdd_time() {
                return add_time;
            }

            public void setAdd_time(String add_time) {
                this.add_time = add_time;
            }
        }

        public static class UserInfoBean implements  Serializable {
            /**
             * uid : 100010
             * nick : mj
             * avatar :
             * is_vip : 0
             * gender : 1
             * level : 1
             */

            private String uid;
            private String nick;
            private String avatar;
            private String is_vip;
            private String gender;
            private String level;

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getNick() {
                return nick;
            }

            public void setNick(String nick) {
                this.nick = nick;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getIs_vip() {
                return is_vip;
            }

            public void setIs_vip(String is_vip) {
                this.is_vip = is_vip;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public String getLevel() {
                return level;
            }

            public void setLevel(String level) {
                this.level = level;
            }
        }
        //****************************公聊消息end**************************************************************************************//



        //****************************机器人进入/离开 start**************************************************************************************//

        /**
         * robot : {"uid":"100008","nick":"机器人","avatar":"http://ybimg.liveniao.com/14635352579073.jpg","level":"5"}
         * robot_count : 1
         */

        private RoBotInfoBean robot;
        private String robot_count;

        public RoBotInfoBean getRobot() {
            return robot;
        }

        public void setRobot(RoBotInfoBean robot) {
            this.robot = robot;
        }

        public String getRobot_count() {
            return robot_count;
        }

        public void setRobot_count(String robot_count) {
            this.robot_count = robot_count;
        }


        public  static  class RoBotInfoBean  implements  Serializable{


            /**
             * uid : 100007
             * avatar : http://ybimg.liveniao.com/20170908+1679091c5a880faf6fb5e6087eb1b2dc
             * level : 5
             * nick : 机器人1
             */

            private String uid;
            private String avatar;
            private String level;
            private String nick;
            private String is_vip;


            public String getIs_vip() {
                return is_vip;
            }

            public void setIs_vip(String is_vip) {
                this.is_vip = is_vip;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getLevel() {
                return level;
            }

            public void setLevel(String level) {
                this.level = level;
            }

            public String getNick() {
                return nick;
            }

            public void setNick(String nick) {
                this.nick = nick;
            }
        }

        //****************************机器人进入/离开 star**************************************************************************************//




        //****************************用户进入直播间 star**************************************************************************************//
        /**
         * fuser : {"avatar":"","level":"1","nick":"哈哈酱","uid":"100011"}
         * reconnect :
         */

        private UserJoinLeaveBean fuser;

        public UserJoinLeaveBean getFuser() {
            return fuser;
        }

        public void setFuser(UserJoinLeaveBean fuser) {
            this.fuser = fuser;
        }

        private String reconnect;
        private int liveonlines;

        public int getLiveonlines() {
            return liveonlines;
        }

        public void setLiveonlines(int liveonlines) {
            this.liveonlines = liveonlines;
        }
        public String getReconnect() {
            return reconnect;
        }

        public void setReconnect(String reconnect) {
            this.reconnect = reconnect;
        }

        public  static  class  UserJoinLeaveBean{

            /**
             * avatar :
             * level : 1
             * nick : mj
             * uid : 100010
             */

            private String avatar;
            private String level;
            private String nick;
            private String uid;
            private String is_vip;


            public String getAvatar() {
                return avatar;
            }

            public String getIs_vip() {
                return is_vip;
            }

            public void setIs_vip(String is_vip) {
                this.is_vip = is_vip;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getLevel() {
                return level;
            }

            public void setLevel(String level) {
                this.level = level;
            }

            public String getNick() {
                return nick;
            }

            public void setNick(String nick) {
                this.nick = nick;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }
        }




        //****************************用户进入直播间  end**************************************************************************************//


        //****************************礼物  start**************************************************************************************//
       private  GiftIdBean   gift;

        public GiftIdBean getGift() {
            return gift;
        }

        public void setGift(GiftIdBean gift) {
            this.gift = gift;
        }

        public static   class  GiftIdBean{


           /**
            * id : 1
            */

           private String    id;
           private String    get_dot;
           private String    name;
           private String    icon;
           private String    total_dot;


            public String getTotal_dot() {
                return total_dot;
            }

            public void setTotal_dot(String total_dot) {
                this.total_dot = total_dot;
            }

            public String getGet_dot() {
                return get_dot;
            }

            public void setGet_dot(String get_dot) {
                this.get_dot = get_dot;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getId() {
               return id;
           }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public void setId(String id) {
               this.id = id;
           }
       }


        //****************************礼物  end**************************************************************************************//
        //****************************主播获得房间收入  start**************************************************************************************//

        private  String  anchor_uid;
        private  String  get_dot;

        public String getGet_dot() {
            return get_dot;
        }

        public void setGet_dot(String get_dot) {
            this.get_dot = get_dot;
        }

        public String getAnchor_uid() {
            return anchor_uid;
        }

        public void setAnchor_uid(String anchor_uid) {
            this.anchor_uid = anchor_uid;
        }

        //****************************主播获得房间收入  end**************************************************************************************//







        /**
         * id : 10234
         * nick : 那么骄傲°
         * icons :
         * avatar : ./20160518/14635352579102.jpg
         * richlvl : 1
         * anchorlvl : 1
         * viplvl : 0
         * mountid : 0
         * superadmin : 0
         * logintype : login
         * guardlvl : 0
         * admin : 0
         */

        private SocketFuserBean fuserx;
        /**
         * id : 10210
         * nick : 像风一样的女子
         * icons :
         * avatar : ./20160518/14635352579078.jpg
         * richlvl : 1
         * anchorlvl : 1
         * viplvl : 0
         * mountid : 0
         * superadmin : 0
         * logintype : login
         * guardlvl : 0
         * admin : 0
         */

        private TuserBean tuser;
        private String msg;
        private String word;



        private int num;
        private LuckMoenyBean luckMoeny;

        private String lvl;
        private String expire;

        private int followers;
        private List<ResultBean> result;


        public String getWord() {
            return word;
        }

        public int getFollowers() {
            return followers;
        }

        public void setFollowers(int followers) {
            this.followers = followers;
        }

        public String getLvl() {
            return lvl;
        }


        public String getExpire() {
            return expire;
        }

        public void setExpire(String expire) {
            this.expire = expire;
        }

        public SocketFuserBean getFuserX() {
            return fuserx;
        }

        public void setFuserx(SocketFuserBean fuserx) {
            this.fuserx = fuserx;
        }

        public TuserBean getTuser() {
            return tuser;
        }

        public void setTuser(TuserBean tuser) {
            this.tuser = tuser;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }



        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public LuckMoenyBean getLuckMoeny() {
            return luckMoeny;
        }

        public void setLuckMoeny(LuckMoenyBean luckMoeny) {
            this.luckMoeny = luckMoeny;
        }

        public List<ResultBean> getResult() {
            return result;
        }

        public void setResult(List<ResultBean> result) {
            this.result = result;
        }

        public ContentBean getContent() {
            return content;
        }

        public void setContent(ContentBean content) {
            this.content = content;
        }

        public UserInfoBean getUser_info() {
            return user_info;
        }

        public void setUser_info(UserInfoBean user_info) {
            this.user_info = user_info;
        }

        public static class TuserBean implements Serializable {
            private String id;
            private String nick;
            private String icons;
            private String avatar;
            private String richlvl;
            private String anchorlvl;
            private String viplvl;
            private String mountid;
            private String superadmin;
            private String logintype;
            private int guardlvl;
            private int admin;
            private float dot;

            public float getDot() {
                return dot;
            }

            public void setDot(float dot) {
                this.dot = dot;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getNick() {
                return nick;
            }

            public void setNick(String nick) {
                this.nick = nick;
            }

            public String getIcons() {
                return icons;
            }

            public void setIcons(String icons) {
                this.icons = icons;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getRichlvl() {
                return richlvl;
            }

            public void setRichlvl(String richlvl) {
                this.richlvl = richlvl;
            }

            public String getAnchorlvl() {
                return anchorlvl;
            }

            public void setAnchorlvl(String anchorlvl) {
                this.anchorlvl = anchorlvl;
            }

            public String getViplvl() {
                return viplvl;
            }

            public void setViplvl(String viplvl) {
                this.viplvl = viplvl;
            }

            public String getMountid() {
                return mountid;
            }

            public void setMountid(String mountid) {
                this.mountid = mountid;
            }

            public String getSuperadmin() {
                return superadmin;
            }

            public void setSuperadmin(String superadmin) {
                this.superadmin = superadmin;
            }

            public String getLogintype() {
                return logintype;
            }

            public void setLogintype(String logintype) {
                this.logintype = logintype;
            }

            public int getGuardlvl() {
                return guardlvl;
            }

            public void setGuardlvl(int guardlvl) {
                this.guardlvl = guardlvl;
            }

            public int getAdmin() {
                return admin;
            }

            public void setAdmin(int admin) {
                this.admin = admin;
            }
        }



        public static class LuckMoenyBean implements Serializable {

            private String id;
            private String money;
            private String num;
            private String name;
            private String rid;
            private String uid;
            private String ctime;

            public String getName() {
                return name;
            }

            public String getId() {
                return id;
            }

            public String getMoney() {
                return money;
            }

            public String getNum() {
                return num;
            }

            public String getRid() {
                return rid;
            }

            public String getUid() {
                return uid;
            }

            public String getCtime() {
                return ctime;
            }

        }

        public static class ResultBean implements Serializable {
            /**
             * id : 10541
             * avatar : def_user_icon.png
             * nick : 13352970351
             * liveonlines : 0
             */

            private String id;
            private String avatar;
            private String total_dot;

            private String nick;

            public String getNick() {
                return nick;
            }

            public void setNick(String nick) {
                this.nick = nick;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getTotal_dot() {
                return total_dot;
            }

            public void setTotal_dot(String total_dot) {
                this.total_dot = total_dot;
            }


        }

    }

}
