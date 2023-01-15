package com.kasp.rankedbot.commands.server;

import com.kasp.rankedbot.CommandSubsystem;
import com.kasp.rankedbot.EmbedType;
import com.kasp.rankedbot.commands.Command;
import com.kasp.rankedbot.instance.Clan;
import com.kasp.rankedbot.instance.Game;
import com.kasp.rankedbot.instance.Player;
import com.kasp.rankedbot.instance.ServerStats;
import com.kasp.rankedbot.instance.cache.ClanCache;
import com.kasp.rankedbot.instance.cache.GameCache;
import com.kasp.rankedbot.instance.cache.PlayerCache;
import com.kasp.rankedbot.instance.embed.Embed;
import com.kasp.rankedbot.messages.Msg;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class SaveDataCmd extends Command {
    public SaveDataCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
        super(command, usage, aliases, description, subsystem);
    }

    @Override
    public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
        if (args.length != 1) {
            Embed reply = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
            msg.replyEmbeds(reply.build()).queue();
            return;
        }

        for (Player p : PlayerCache.getPlayers().values()) {
            try {
                Player.writeFile(p.getID(), null);
                System.out.println("Saved player " + p.getIgn() + " data");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (Game g : GameCache.getGames().values()) {
            try {
                Game.writeFile(g);
                System.out.println("Saved game " + g.getNumber() + " data");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (Clan c : ClanCache.getClans().values()) {
            try {
                c.writeFile();
                System.out.println("Saved clan " + c.getName() + " data");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ServerStats.save();

        Embed reply = new Embed(EmbedType.ERROR, "Data saved", "All players, clans and games data has been saved", 1);
        msg.replyEmbeds(reply.build()).queue();
    }
}
