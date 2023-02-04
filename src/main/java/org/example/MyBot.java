package org.example;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;

public class MyBot {

    public static void main(String[] args) {
        GatewayDiscordClient client = DiscordClientBuilder.create("TOKEN HERE")
                .build()
                .login()
                .block();


        client.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(event -> {
                    User self = event.getSelf();
                    System.out.println(String.format("Logged in as %s#%s", self.getUsername(), self.getDiscriminator()));
                });


        client.getEventDispatcher().on(MessageCreateEvent.class) // listens for all MessageCreateEvents
                .map(MessageCreateEvent::getMessage) // turns the events into messages that were sent
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false)) //filters out bots
                .filter(message -> message.getContent().equalsIgnoreCase("!ping")) // filters out messages that are not "!ping"
                .flatMap(Message::getChannel) //turns it into the channel the message originally came from
                .flatMap(channel -> channel.createMessage("Pong!")) // creates message "Pong!"
                .subscribe(); //executes
        client.onDisconnect().block();
    }
}
