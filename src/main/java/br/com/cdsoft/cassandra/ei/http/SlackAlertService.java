package br.com.cdsoft.cassandra.ei.http;

import br.com.cdsoft.cassandra.ei.alert.ObjectMapperFormatter;
import br.com.cdsoft.cassandra.ei.dto.SlackAlertDTO;
import br.com.cdsoft.cassandra.ei.dto.SlackMessageDTO;
import br.com.cdsoft.cassandra.ei.dto.PropertyDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SlackAlertService {

    @Autowired
    private ObjectMapperFormatter objectMapperFormatter;

    @Value("${alert.url}")
    private String url;
    @Value("${alert.imagem}")
    private String imagem;

    public boolean send(final PropertyDTO propertyDTO) throws Exception {


        var post =   template(propertyDTO);
        var requestBody =
                RequestBody.create(MediaType.parse(org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE), post);
        var client = new OkHttpClient();
        var request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        var response = client.newCall(request).execute();
        return HttpStatus.OK.value() == response.code();


    }

    private String template(final PropertyDTO propertyDTO) throws Exception{

            final SlackAlertDTO alertDTO = new SlackAlertDTO();
            alertDTO.setText("Real Time Stream");
            final List<SlackMessageDTO> attachments = new ArrayList<>();
            final SlackMessageDTO messageDTO = new SlackMessageDTO();
            messageDTO.setAuthor("Instituição");
            messageDTO.setTitle("Propriedade.: " + propertyDTO.getKey() + " foi criada com o valor .: " + propertyDTO.getValue());
            messageDTO.addField("Regra número", "37");
            messageDTO.setAuthorIcon(imagem);
            messageDTO.setImageUrl(imagem);
            attachments.add(messageDTO);
            alertDTO.setAttachments(attachments);
            return alertDTO.getPayload(objectMapperFormatter);

    }
}
