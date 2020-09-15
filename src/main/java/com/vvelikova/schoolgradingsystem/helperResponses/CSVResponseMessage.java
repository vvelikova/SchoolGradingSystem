package com.vvelikova.schoolgradingsystem.helperResponses;

/** The ResponseMessage is for message to client that we’re
 * gonna use in Rest Controller and Exception Handler. */
public class CSVResponseMessage {

    private String message;

    public CSVResponseMessage(String msg) {
        this.message = msg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
