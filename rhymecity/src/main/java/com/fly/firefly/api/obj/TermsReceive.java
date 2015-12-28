package com.fly.firefly.api.obj;

/*
 * Created by Metech USer
 */

 /* Response From API */

public class TermsReceive {

    private final TermsReceive termObj;
    private String status;
    private String message;
    private term_info term_info;

    public TermsReceive(TermsReceive param_termObj) {
        this.termObj = param_termObj;
    }

    public TermsReceive getTermObj() {
        return termObj;
    }

    public class term_info {

        private String id;
        private String title;
        private String body;
        private String created_at;
        private String updated_at;
        private String content;
        private String display_order;
        private String parent_id;



        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getDisplay_order() {
            return display_order;
        }

        public void setDisplay_order(String display_order) {
            this.display_order = display_order;
        }

        public String getParent_id() {
            return parent_id;
        }

        public void setParent_id(String parent_id) {
            this.parent_id = parent_id;
        }



    }


    public term_info getTermInfo() {
        return term_info;
    }

    public void setTermInfo(term_info termInfo) {
        this.term_info = termInfo;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {return message;}

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
