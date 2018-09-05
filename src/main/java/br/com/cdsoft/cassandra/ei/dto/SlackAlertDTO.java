package br.com.cdsoft.cassandra.ei.dto;

import br.com.cdsoft.cassandra.ei.alert.Alerta;
import br.com.cdsoft.cassandra.ei.alert.Formmater;

import java.util.ArrayList;
import java.util.List;

public class SlackAlertDTO implements Alerta {


    public SlackAlertDTO() {

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    private String text;
    private List<SlackMessageDTO> attachments = new ArrayList<>();

    public List<SlackMessageDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<SlackMessageDTO> attachments) {
        this.attachments = attachments;
    }


    @Override
    public String getPayload(Formmater formatter) throws Exception {
        return formatter.format(this);
    }
}
