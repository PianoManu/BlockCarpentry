package mod.pianomanu.blockcarpentry.event;

import mod.pianomanu.blockcarpentry.BlockCarpentryMain;
import mod.pianomanu.blockcarpentry.setup.config.BCModConfig;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

/**
 * Class for sending a discord invitation message to the player, which can be
 * disabled in /config/blockcarpentry-common.toml
 *
 * @author PianoManu
 * @version 1.0 10/19/23
 */
@Mod.EventBusSubscriber(modid = BlockCarpentryMain.MOD_ID, value = Dist.CLIENT)
public class BCServerMessage {

    @SubscribeEvent
    public static void onWorldStart(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() != null && BCModConfig.SHOW_DISCORD_INVITATION.get()) {
            Style s = Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("§6Click to visit our server!"))).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.com/invite/cy7xwVzRzK"));
            event.getEntity().displayClientMessage(Component.translatable("message.blockcarpentry.discord1").setStyle(s), false);
            event.getEntity().displayClientMessage(Component.translatable("message.blockcarpentry.discord2").setStyle(s), false);
            event.getEntity().displayClientMessage(Component.translatable("message.blockcarpentry.discord3").setStyle(s), false);
            Path p = FMLPaths.CONFIGDIR.get();

            s = Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("§6Click here to open the config file!"))).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, p.toFile().getAbsolutePath() + "/blockcarpentry-common.toml"));
            event.getEntity().displayClientMessage(Component.translatable("message.blockcarpentry.discord_disable1").setStyle(s), false);
            event.getEntity().displayClientMessage(Component.translatable("message.blockcarpentry.discord_disable2").setStyle(s), false);
            if (BCModConfig.ONLY_SHOW_INVITATION_ONCE.get())
                BCModConfig.SHOW_DISCORD_INVITATION.set(false);
        }
    }
}
//========SOLI DEO GLORIA========//