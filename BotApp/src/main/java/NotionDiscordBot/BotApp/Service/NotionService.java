package NotionDiscordBot.BotApp.Service;

import NotionDiscordBot.BotApp.DTO.property.NotionProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class NotionService {

    private final NotionProperty notionProperty;

    public NotionService(final NotionProperty notionProperty) {
        this.notionProperty = notionProperty;
    }

    public List<String> getTodayProblemList(String today) throws URISyntaxException {
        String endpoint = "https://api.notion.com/v1/databases/" + notionProperty.getDbID() + "/query";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Notion-Version", notionProperty.getVersion());
        headers.set("Authorization", "Bearer " + notionProperty.getApiKey());

        Map<String, Object> requestBody = Map.of("filter", Map.of("property", "날짜", "date", Map.of("equals", today)));

        RequestEntity<Map<String, Object>> requestEntity = RequestEntity.post(new URI(endpoint))
                .headers(headers)
                .body(requestBody);

        ResponseEntity<Map> responseEntity = restTemplate.exchange(requestEntity, Map.class);

        List<String> notionData = new ArrayList<>();

        Map<String, Object> response = responseEntity.getBody();
        List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");

        for (Map<String, Object> result : results) {
            Map<String, Object> properties = (Map<String, Object>) result.get("properties");
            Map<String, Object> problemName = (Map<String, Object>) properties.get("문제명");
            for (String s : problemName.keySet()) {
                System.out.println(s);
            }
            List<Map<String, Object>> title = (List<Map<String, Object>>) problemName.get("title");

            Map<String, String> text = (Map<String, String>) title.get(0).get("text");
            notionData.add((String) text.get("content"));
        }

        return notionData;

    }
}
