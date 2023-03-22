package NotionDiscordBot.BotApp.Configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "discord")
@Component
@Getter
@Setter
public class BotConfiguration {

    private String token;

}
