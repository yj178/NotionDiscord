package NotionDiscordBot.BotApp.Discord;

import NotionDiscordBot.BotApp.Configuration.BotConfiguration;
import NotionDiscordBot.BotApp.Interface.EventListener;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class MyBot {

    private BotConfiguration botConfiguration;

    public MyBot(BotConfiguration botConfiguration) {
        this.botConfiguration = botConfiguration;
    }

    @Bean
    public <T extends Event> GatewayDiscordClient gatewayDiscordClient(List<EventListener<T>> eventListeners) {
        GatewayDiscordClient client = DiscordClientBuilder.create(botConfiguration.getToken())
                .build()
                .login()
                .block();

        for(EventListener<T> listener : eventListeners) {
            client.on(listener.getEventType())
                    .flatMap(listener::execute)
                    .onErrorResume(listener::handleError)
                    .subscribe();
        }

        return client;
    }
}
