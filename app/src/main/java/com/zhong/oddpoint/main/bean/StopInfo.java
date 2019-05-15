package com.zhong.oddpoint.main.bean;

import java.util.List;

public class StopInfo {
    /**
     * validateMessagesShowId : _validatorMessage
     * status : true
     * httpstatus : 200
     * data : {"data":[{"start_station_name":"万州北","arrive_time":"12:13","station_train_code":"D1881","station_name":"万州北","train_class_name":"动车","service_type":"2","start_time":"12:13","stopover_time":"----","end_station_name":"广州南","station_no":"01","isEnabled":false},{"arrive_time":"12:39","station_name":"梁平南","start_time":"12:41","stopover_time":"2分钟","station_no":"02","isEnabled":false},{"arrive_time":"13:01","station_name":"垫江","start_time":"13:03","stopover_time":"2分钟","station_no":"03","isEnabled":false},{"arrive_time":"13:49","station_name":"重庆北","start_time":"13:56","stopover_time":"7分钟","station_no":"04","isEnabled":true},{"arrive_time":"14:20","station_name":"重庆西","start_time":"14:24","stopover_time":"4分钟","station_no":"05","isEnabled":true},{"arrive_time":"14:37","station_name":"珞璜南","start_time":"14:39","stopover_time":"2分钟","station_no":"06","isEnabled":true},{"arrive_time":"14:56","station_name":"綦江东","start_time":"14:59","stopover_time":"3分钟","station_no":"07","isEnabled":true},{"arrive_time":"15:55","station_name":"遵义","start_time":"15:57","stopover_time":"2分钟","station_no":"08","isEnabled":true},{"arrive_time":"16:45","station_name":"贵阳东","start_time":"16:49","stopover_time":"4分钟","station_no":"09","isEnabled":true},{"arrive_time":"17:07","station_name":"龙里北","start_time":"17:15","stopover_time":"8分钟","station_no":"10","isEnabled":true},{"arrive_time":"17:27","station_name":"贵定县","start_time":"17:29","stopover_time":"2分钟","station_no":"11","isEnabled":true},{"arrive_time":"18:30","station_name":"从江","start_time":"18:32","stopover_time":"2分钟","station_no":"12","isEnabled":true},{"arrive_time":"19:19","station_name":"桂林西","start_time":"19:23","stopover_time":"4分钟","station_no":"13","isEnabled":true},{"arrive_time":"20:20","station_name":"贺州","start_time":"20:32","stopover_time":"12分钟","station_no":"14","isEnabled":true},{"arrive_time":"22:00","station_name":"广州南","start_time":"22:00","stopover_time":"----","station_no":"15","isEnabled":true}]}
     * messages : []
     * validateMessages : {}
     */

    private String validateMessagesShowId;
    private boolean status;
    private int httpstatus;
    private DataBeanX data;
    private ValidateMessagesBean validateMessages;
    private List<?> messages;

    public String getValidateMessagesShowId() {
        return validateMessagesShowId;
    }

    public void setValidateMessagesShowId(String validateMessagesShowId) {
        this.validateMessagesShowId = validateMessagesShowId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getHttpstatus() {
        return httpstatus;
    }

    public void setHttpstatus(int httpstatus) {
        this.httpstatus = httpstatus;
    }

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public ValidateMessagesBean getValidateMessages() {
        return validateMessages;
    }

    public void setValidateMessages(ValidateMessagesBean validateMessages) {
        this.validateMessages = validateMessages;
    }

    public List<?> getMessages() {
        return messages;
    }

    public void setMessages(List<?> messages) {
        this.messages = messages;
    }

    public static class DataBeanX {
        private List<DataBean> data;

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * start_station_name : 万州北
             * arrive_time : 12:13
             * station_train_code : D1881
             * station_name : 万州北
             * train_class_name : 动车
             * service_type : 2
             * start_time : 12:13
             * stopover_time : ----
             * end_station_name : 广州南
             * station_no : 01
             * isEnabled : false
             */

            private String start_station_name;
            private String arrive_time;
            private String station_train_code;
            private String station_name;
            private String train_class_name;
            private String service_type;
            private String start_time;
            private String stopover_time;
            private String end_station_name;
            private String station_no;
            private boolean isEnabled;

            public String getStart_station_name() {
                return start_station_name;
            }

            public void setStart_station_name(String start_station_name) {
                this.start_station_name = start_station_name;
            }

            public String getArrive_time() {
                return arrive_time;
            }

            public void setArrive_time(String arrive_time) {
                this.arrive_time = arrive_time;
            }

            public String getStation_train_code() {
                return station_train_code;
            }

            public void setStation_train_code(String station_train_code) {
                this.station_train_code = station_train_code;
            }

            public String getStation_name() {
                return station_name;
            }

            public void setStation_name(String station_name) {
                this.station_name = station_name;
            }

            public String getTrain_class_name() {
                return train_class_name;
            }

            public void setTrain_class_name(String train_class_name) {
                this.train_class_name = train_class_name;
            }

            public String getService_type() {
                return service_type;
            }

            public void setService_type(String service_type) {
                this.service_type = service_type;
            }

            public String getStart_time() {
                return start_time;
            }

            public void setStart_time(String start_time) {
                this.start_time = start_time;
            }

            public String getStopover_time() {
                return stopover_time;
            }

            public void setStopover_time(String stopover_time) {
                this.stopover_time = stopover_time;
            }

            public String getEnd_station_name() {
                return end_station_name;
            }

            public void setEnd_station_name(String end_station_name) {
                this.end_station_name = end_station_name;
            }

            public String getStation_no() {
                return station_no;
            }

            public void setStation_no(String station_no) {
                this.station_no = station_no;
            }

            public boolean isIsEnabled() {
                return isEnabled;
            }

            public void setIsEnabled(boolean isEnabled) {
                this.isEnabled = isEnabled;
            }
        }
    }

    public static class ValidateMessagesBean {
    }
}
