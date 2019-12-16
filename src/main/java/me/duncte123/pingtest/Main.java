package me.duncte123.pingtest;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;

public class Main implements EventListener {

    private Main(String token) throws LoginException {
        new JDABuilder()
                .setToken(token)
                .addEventListeners(this)
                .build();
    }

    @Override
    public void onEvent(@Nonnull GenericEvent event) {
        if (event instanceof GuildMessageReceivedEvent) {
            this.onMessage((GuildMessageReceivedEvent) event);
        }
    }

    private void onMessage(@Nonnull GuildMessageReceivedEvent event) {
        final String raw = event.getMessage().getContentRaw();
        final TextChannel channel = event.getChannel();
        final JDA jda = event.getJDA();

        if ("--ping".equals(raw)) {
            channel.sendMessage("Ws: " + jda.getGatewayPing()).queue();

            jda.getRestPing().queue(
                    (ping) -> channel.sendMessage("Rest: " + ping).queue()
            );
        }
    }

    public static void main(String[] args) throws LoginException {
        if (args.length == 0) {
            throw new IllegalArgumentException("Must provide bot token as argument");
        }

        new Main(args[0]);
    }
}
