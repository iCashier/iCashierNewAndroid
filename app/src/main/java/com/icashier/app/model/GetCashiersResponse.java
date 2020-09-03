package com.icashier.app.model;

import java.util.List;

public class GetCashiersResponse {

    /**
     * code : 200
     * message : Success.
     * cashiers : [{"name":"wer","id":12,"image":"http://52.27.53.102/imenu/uploads/users/4OBE2LNCKZW71FOM2S6VH49AR/d9bd52ad4fae9b0a585882cfa47f8d76.png","signup":"2018-11-15T08:39:54.000Z","login":"2018-11-15T08:39:54.000Z"},{"name":"asdf2","id":13,"image":"http://52.27.53.102/imenu/uploads/users/98/9c3fe4bb4ce43637b26303dd23999e78.png","signup":"2018-11-15T08:41:37.000Z","login":"2018-11-15T08:41:37.000Z"},{"name":"ertyuinbm","id":14,"image":"http://52.27.53.102/imenu/uploads/users/4OBE2LNCKZW71FOM2S6VH49AR/3a27522d86e3f6665fb0cac7ace07848.png","signup":"2018-11-15T09:39:57.000Z","login":"2018-11-15T09:39:57.000Z"},{"name":"Sanjeet","id":16,"image":"http://52.27.53.102/imenu/uploads/users/98/8c00548124aaad5bddf5948816d5520c.png","signup":"0000-00-00 00:00:00","login":"0000-00-00 00:00:00"},{"name":"Sanjeet3","id":17,"image":"http://52.27.53.102/imenu/uploads/users/98/b958207bc1f7ea1d068f3b511c8ff24c.png","signup":"2018-11-15T09:39:54.000Z","login":"2018-11-15T09:39:54.000Z"},{"name":"werwer","id":18,"image":"http://52.27.53.102/imenu/uploads/users/4OBE2LNCKZW71FOM2S6VH49AR/b73d1aa164540f12e0cb4e96f1aa525b.png","signup":"2018-11-15T10:32:06.000Z","login":"2018-11-15T10:32:06.000Z"},{"name":"ertyuiasdf","id":33,"image":"http://52.27.53.102/imenu/uploads/users/4OBE2LNCKZW71FOM2S6VH49AR/1f8acecff21cb44e3307dc2269f6cae7.png","signup":"2018-11-15T10:44:45.000Z","login":"2018-11-15T10:44:45.000Z"},{"name":"wero","id":41,"image":"http://52.27.53.102/imenu/uploads/users/4OBE2LNCKZW71FOM2S6VH49AR/862c961bd7c554047936088f0bc7d599.png","signup":"2018-11-15T10:57:15.000Z","login":"2018-11-15T10:57:15.000Z"}]
     */

    private int code;
    private String message;
    private List<CashiersBean> cashiers;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CashiersBean> getCashiers() {
        return cashiers;
    }

    public void setCashiers(List<CashiersBean> cashiers) {
        this.cashiers = cashiers;
    }

    public static class CashiersBean {
        /**
         * name : wer
         * id : 12
         * image : http://52.27.53.102/imenu/uploads/users/4OBE2LNCKZW71FOM2S6VH49AR/d9bd52ad4fae9b0a585882cfa47f8d76.png
         * signup : 2018-11-15T08:39:54.000Z
         * login : 2018-11-15T08:39:54.000Z
         */

        private String name;
        private int id;
        private String image;
        private String signup;
        private String login;
        private boolean isSelected;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getSignup() {
            return signup;
        }

        public void setSignup(String signup) {
            this.signup = signup;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }
}
