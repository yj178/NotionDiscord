package NotionDiscordBot.BotApp.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ProblemInfo {
    private String name;
    private String link;
    private String author;
}
