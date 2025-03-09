package github.Mtn16.bookGUI;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public final class BookGUI extends JavaPlugin {

    @Override
    public void onEnable() {

        createDefaultStructure();

        Objects.requireNonNull(getCommand("bookgui")).setExecutor(new BookCommand(this));
        Objects.requireNonNull(getCommand("bookgui")).setTabCompleter(new BookCommandTabCompleter(this));
    }

    private void createDefaultStructure() {
        File booksFolder = new File(getDataFolder(), "books");
        if (!booksFolder.exists()) {
            if(!booksFolder.mkdirs()) {
                getLogger().warning("Cannot create BookGUI plugin folder");
                return;
            }
        }

        File exampleFile = new File(booksFolder, "example.yml");
        if (!exampleFile.exists()) {
            try {
                YamlConfiguration config = new YamlConfiguration();
                config.set("title", "Example book");
                config.set("author", "Admin");
                config.set("pages", List.of(
                        "<green>Welcome to BookGUI!</green> You can use <rainbow>MiniMessage</rainbow> formatting here!",
                        "This is <bold>the second page</bold>."
                ));
                config.save(exampleFile);
                getLogger().info("Generated example book: " + exampleFile.getPath());
            } catch (IOException e) {
                getLogger().severe("Cannot generate example book: " + e.getMessage());
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
