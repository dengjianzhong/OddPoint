package com.zhong.oddpoint.main.bean;

import java.util.List;

public class car_info {

    /**
     * data : {"flag":"1","map":{"CQW":"重庆","CUW":"重庆北","WYW":"万州","WZE":"万州北"},"result":车次结果
     * httpstatus : 200
     * messages :
     * status : true
     */

    private DataBean data;
    private int httpstatus;
    private String messages;
    private boolean status;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getHttpstatus() {
        return httpstatus;
    }

    public void setHttpstatus(int httpstatus) {
        this.httpstatus = httpstatus;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public static class DataBean {
        /**
         * flag : 1
         * map : {"CQW":"重庆","CUW":"重庆北","WYW":"万州","WZE":"万州北"}
         * result : []
         */

        private String flag;
        private MapBean map;
        private List<String> result;

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public MapBean getMap() {
            return map;
        }

        public void setMap(MapBean map) {
            this.map = map;
        }

        public List<String> getResult() {
            return result;
        }

        public void setResult(List<String> result) {
            this.result = result;
        }


        public static class MapBean {
            /**
             * CQW : 重庆
             * CUW : 重庆北
             * WYW : 万州
             * WZE : 万州北
             */

            private String CQW;
            private String CUW;
            private String WYW;
            private String WZE;

            public String getCQW() {
                return CQW;
            }

            public void setCQW(String CQW) {
                this.CQW = CQW;
            }

            public String getCUW() {
                return CUW;
            }

            public void setCUW(String CUW) {
                this.CUW = CUW;
            }

            public String getWYW() {
                return WYW;
            }

            public void setWYW(String WYW) {
                this.WYW = WYW;
            }

            public String getWZE() {
                return WZE;
            }

            public void setWZE(String WZE) {
                this.WZE = WZE;
            }
        }


    }
}
