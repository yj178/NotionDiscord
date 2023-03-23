package NotionDiscordBot.BotApp.Configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "notion")
@Component
@Getter
@Setter
public class NotionConfiguration {
    private String dbID;
    private String version;
    private String apiKey;
}
