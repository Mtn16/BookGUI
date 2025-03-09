package github.Mtn16.bookGUI;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class BookCommand implements CommandExecutor {
    private final BookGUI plugin;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public BookCommand(BookGUI plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (commandSender instanceof Player) {
            if (args.length == 1) {
                if (!commandSender.hasPermission("bookgui.open.self") || !commandSender.hasPermission("bookgui.open.*"))
                {
                    commandSender.sendMessage(Component.text("Missing permissions").color(NamedTextColor.RED));
                    return true;
                }
                if (!commandSender.hasPermission("bookgui.book." + args[0]))
                {
                    commandSender.sendMessage(Component.text("Missing permissions").color(NamedTextColor.RED));
                    return true;
                }
                Player player = (Player) commandSender;
                openBook(args[0], player, commandSender);

            } else if (args.length == 2) {
                if (!commandSender.hasPermission("bookgui.open.others") || !commandSender.hasPermission("bookgui.open.*"))
                {
                    commandSender.sendMessage(Component.text("Missing permissions").color(NamedTextColor.RED));
                    return true;
                }
                if (!commandSender.hasPermission("bookgui.book." + args[0]))
                {
                    commandSender.sendMessage(Component.text("Missing permissions").color(NamedTextColor.RED));
                    return true;
                }
                Player player = plugin.getServer().getPlayer(args[1]);
                if (player == null) {
                    commandSender.sendMessage(Component.text("Player is not online").color(NamedTextColor.RED));
                    return true;
                } else if(!player.isOnline()) {
                    commandSender.sendMessage(Component.text("Player is not online").color(NamedTextColor.RED));
                    return true;
                }
                openBook(args[0], player, commandSender);

            } else {
                commandSender.sendMessage(Component.text("Usage: /bookgui <book> [player]").color(NamedTextColor.RED));
            }
            return true;
        } else if(commandSender instanceof ConsoleCommandSender) {
            if(args.length == 2) {
                Player player = plugin.getServer().getPlayer(args[1]);
                if (player == null) {
                    commandSender.sendMessage(Component.text("Player is not online").color(NamedTextColor.RED));
                    return true;
                } else if(!player.isOnline()) {
                    commandSender.sendMessage(Component.text("Player is not online").color(NamedTextColor.RED));
                    return true;
                }
                openBook(args[0], player, commandSender);
            } else {
                commandSender.sendMessage(Component.text("Usage: bookgui <book> <player>").color(NamedTextColor.RED));
            }
            return true;
        } else {
            return false;
        }
    }

    private void openBook(String bookName, Player player, CommandSender sender) {
        File bookFile = new File(plugin.getDataFolder() + "/books/" + bookName + ".yml");

        if (!bookFile.exists()) {
            sender.sendMessage(Component.text("Unknown book").color(NamedTextColor.RED));
            return;
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(bookFile);
        String title = config.getString("title", "Unknown title");
        String author = config.getString("author", "Unknown author");
        List<String> pages = config.getStringList("pages");

        if (pages.isEmpty()) {
            sender.sendMessage(Component.text("Book does not have any pages").color(NamedTextColor.RED));
            return;
        }

        ItemStack bookItem = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) bookItem.getItemMeta();
        meta.setTitle(bookName);
        meta.setAuthor(bookName);

        meta.setTitle(title);
        meta.setAuthor(author);

        for (String page : pages) {
            meta.addPages(miniMessage.deserialize(page));
        }

        bookItem.setItemMeta(meta);

        player.openBook(bookItem);
        sender.sendMessage(Component.text("Opened book " + bookName + " for " + player.getName()).color(NamedTextColor.GREEN));

    }
}
