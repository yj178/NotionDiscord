package NotionDiscordBot.BotApp.Service;

import NotionDiscordBot.BotApp.Configuration.NotionConfiguration;
import NotionDiscordBot.BotApp.DTO.ProblemInfo;
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

    private final NotionConfiguration notionConfiguration;

    public NotionService(final NotionConfiguration notionConfiguration) {
        this.notionConfiguration = notionConfiguration;
    }

    public List<ProblemInfo> getTodayProblemInfoList(String today) throws URISyntaxException {
        String endpoint = "https://api.notion.com/v1/databases/" + notionConfiguration.getDbID() + "/query";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Notion-Version", notionConfiguration.getVersion());
        headers.set("Authorization", "Bearer " + notionConfiguration.getApiKey());

        Map<String, Object> requestBody = Map.of("filter", Map.of("property", "날짜", "date", Map.of("equals", today)));

        RequestEntity<Map<String, Object>> requestEntity = RequestEntity.post(new URI(endpoint))
                .headers(headers)
                .body(requestBody);

        ResponseEntity<Map> responseEntity = restTemplate.exchange(requestEntity, Map.class);

        List<ProblemInfo> notionData = new ArrayList<>();

        Map<String, Object> response = responseEntity.getBody();
        List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");

        for (Map<String, Object> result : results) {
            Map<String, Object> properties = (Map<String, Object>) result.get("properties");
            Map<String, Object> problemName = (Map<String, Object>) properties.get("문제명");
            List<Map<String, Object>> title = (List<Map<String, Object>>) problemName.get("title");
            Map<String, String> text = (Map<String, String>) title.get(0).get("text");

            Map<String, Object> problemLink = (Map<String, Object>) properties.get("링크");
            String link = (String) problemLink.get("url");

            Map<String, Object> problemAuthor = (Map<String, Object>) properties.get("추천자");
            List<Map<String, Object>> author = (List<Map<String, Object>>) problemAuthor.get("rich_text");
            String authorName = (String) author.get(0).get("plain_text");
            notionData.add(new ProblemInfo(text.get("content"), link, authorName));
        }

        return notionData;

    }

    public List<String> getTodayProblemNameList(String today) throws URISyntaxException {
        String endpoint = "https://api.notion.com/v1/databases/" + notionConfiguration.getDbID() + "/query";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Notion-Version", notionConfiguration.getVersion());
        headers.set("Authorization", "Bearer " + notionConfiguration.getApiKey());

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
            List<Map<String, Object>> title = (List<Map<String, Object>>) problemName.get("title");

            Map<String, String> text = (Map<String, String>) title.get(0).get("text");
            notionData.add((String) text.get("content"));
        }

        return notionData;

    }
}
