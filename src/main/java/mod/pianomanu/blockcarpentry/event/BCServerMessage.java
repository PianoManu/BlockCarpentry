package mod.pianomanu.blockcarpentry.event;

import mod.pianomanu.blockcarpentry.BlockCarpentryMain;
import mod.pianomanu.blockcarpentry.setup.config.BCModConfig;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
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
        if (event.getPlayer() != null && BCModConfig.SHOW_DISCORD_INVITATION.get()) {
            Style s = Style.EMPTY.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("§6Click to visit our server!"))).setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.com/invite/cy7xwVzRzK"));
            event.getPlayer().sendStatusMessage(new TranslationTextComponent("message.blockcarpentry.discord1").setStyle(s), false);
            event.getPlayer().sendStatusMessage(new TranslationTextComponent("message.blockcarpentry.discord2").setStyle(s), false);
            event.getPlayer().sendStatusMessage(new TranslationTextComponent("message.blockcarpentry.discord3").setStyle(s), false);
            Path p = FMLPaths.CONFIGDIR.get();

            s = Style.EMPTY.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("§6Click here to open the config file!"))).setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, p.toFile().getAbsolutePath() + "/blockcarpentry-common.toml"));
            event.getPlayer().sendStatusMessage(new TranslationTextComponent("message.blockcarpentry.discord_disable1").setStyle(s), false);
            event.getPlayer().sendStatusMessage(new TranslationTextComponent("message.blockcarpentry.discord_disable2").setStyle(s), false);
            if (BCModConfig.ONLY_SHOW_INVITATION_ONCE.get())
                BCModConfig.SHOW_DISCORD_INVITATION.set(false);
        }
    }
}
//========SOLI DEO GLORIA========//