package NotionDiscordBot.BotApp.Service;

import NotionDiscordBot.BotApp.DTO.ProblemInfo;
import NotionDiscordBot.BotApp.Interface.EventListener;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateFields;
import discord4j.core.spec.EmbedCreateSpec;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;

@Service
public class MessageCreateListener implements EventListener<MessageCreateEvent> {

    private NotionService notionService;

    public MessageCreateListener(NotionService notionService) {
        this.notionService = notionService;
    }

    @Override
    public Class<MessageCreateEvent> getEventType() {
        return MessageCreateEvent.class;
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        return solve(event.getMessage());
    }


    public Mono<Void> solve(Message eventMessage) {
        return Mono.just(eventMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().equalsIgnoreCase("!solve"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> {
                    try {
                        return channel.createMessage(createNotionInfoListMessage(LocalDate.now().toString()));
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                })
                .then();
    }

    public String createNotionInfoListMessage(String today) throws URISyntaxException {
        List<ProblemInfo> problemList = notionService.getTodayProblemInfoList(today);

        StringBuilder sb = new StringBuilder();
        for (ProblemInfo p : problemList) {
//            sb.append(p.getAuthor() + "추천 // 문제이름 : " + p.getName() + "(" + p.getLink() + ")" + "\n");
            sb.append("문제이름 : " + p.getName() + "(" + p.getLink() + ")" + "\n");
        }

        return sb.toString();
    }

    public EmbedCreateSpec createNotionInfoEmbed(String today) throws URISyntaxException {
        List<ProblemInfo> problemList = notionService.getTodayProblemInfoList(today);


        EmbedCreateSpec embed = EmbedCreateSpec.builder()
                .title(today + "추천 문제 리스트")
                .build();

        return embed;
    }

    public String createNotionInfoMessage(String today) throws URISyntaxException {
        List<String> problemList = notionService.getTodayProblemNameList(today);

        StringBuilder sb = new StringBuilder();
        for (String problem : problemList) {
            sb.append(problem + "\n");
        }

        return sb.toString();
    }
}