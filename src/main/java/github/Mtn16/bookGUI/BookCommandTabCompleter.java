package github.Mtn16.bookGUI;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BookCommandTabCompleter implements TabCompleter {
    private final BookGUI plugin;

    public BookCommandTabCompleter(BookGUI plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            File booksFolder = new File(plugin.getDataFolder(), "books");
            List<String> suggestions = new ArrayList<>();

            if (booksFolder.exists() && booksFolder.isDirectory()) {
                for (File file : Objects.requireNonNull(booksFolder.listFiles())) {
                    if (file.isFile() && file.getName().endsWith(".yml")) {
                        suggestions.add(file.getName().replace(".yml", ""));
                    }
                }
            }
            return suggestions;
        } else if (args.length == 2) {
            List<String> suggestions = new ArrayList<>();

            for (Player player : plugin.getServer().getOnlinePlayers()) {
                suggestions.add(player.getName());
            }

            return  suggestions;
        }
        return null;
    }
}
