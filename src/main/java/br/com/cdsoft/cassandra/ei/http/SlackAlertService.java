package br.com.cdsoft.cassandra.ei.http;

import br.com.cdsoft.cassandra.ei.alert.ObjectMapperFormatter;
import br.com.cdsoft.cassandra.ei.dto.PropertyDTO;
import br.com.cdsoft.cassandra.ei.dto.SlackAlertDTO;
import br.com.cdsoft.cassandra.ei.dto.SlackMessageDTO;
import br.com.cdsoft.cassandra.ei.dto.TypeProperty;
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

    public static final String REGRA_NÚMERO = "Regra Número";
    public static final String REGRA_CREDITO = "37";
    @Autowired
    private ObjectMapperFormatter objectMapperFormatter;

    @Value("${alert.url}")
    private String url;
    @Value("${alert.imagem}")
    private String imagem;
    @Value("${realtime-stream.message}")
    private String message;
    @Value("${realtime-stream.author}")
    private String author;

    public boolean send(final PropertyDTO propertyDTO) throws Exception {

        if (TypeProperty.CONTACORRENTE.equals(propertyDTO.getType())) {

            var post = template(propertyDTO);
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
        return false;


    }

    private String template(final PropertyDTO propertyDTO) throws Exception {

        final SlackAlertDTO slackAlertDTO = new SlackAlertDTO();
        slackAlertDTO.setText(message);
        addField(slackAlertDTO, propertyDTO);
        return slackAlertDTO.getPayload(objectMapperFormatter);

    }

    private void addField(final SlackAlertDTO slackAlertDTO, final PropertyDTO propertyDTO) {
        if (TypeProperty.CONTACORRENTE.equals(propertyDTO.getType()) | TypeProperty.CREDITO.equals(propertyDTO.getType())) {
            final List<SlackMessageDTO> attachments = new ArrayList<>();
            final SlackMessageDTO messageDTO = new SlackMessageDTO();
            messageDTO.setAuthor(author);
            messageDTO.setTitle("Propriedade do Tipo: " + propertyDTO.getType().getDescricao() + " de nome " + propertyDTO.getKey() + " foi criada com o valor de " + propertyDTO.getValue());
            messageDTO.setAuthorIcon(imagem);
            messageDTO.addField(REGRA_NÚMERO, REGRA_CREDITO);
            messageDTO.setImageUrl(imagem);
            attachments.add(messageDTO);
            slackAlertDTO.setAttachments(attachments);
        }
    }
}
