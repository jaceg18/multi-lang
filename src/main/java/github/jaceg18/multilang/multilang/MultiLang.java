package github.jaceg18.multilang.multilang;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("all") // only suppresses deprecated warnings.
public final class MultiLang extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }
    private static final Pattern LANG_PATTERN = Pattern.compile("<([a-z]{2})>");

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        Matcher matcher = LANG_PATTERN.matcher(message);

        if (matcher.find()) {
            handleLangTag(event, matcher, message);
        }
    }

    private void handleLangTag(AsyncPlayerChatEvent event, Matcher matcher, String message) {
        Player sender = event.getPlayer();
        event.setCancelled(true);

        String sourceLang = matcher.group(1);
        if (matcher.find()) {
            String targetLang = matcher.group(1);
            String cleanedMessage = message.replaceAll("<[a-z]{2}>", "");

            for (Player recipient : event.getRecipients()) {
                try {
                    String translatedMessage = translate(cleanedMessage, targetLang, sourceLang);
                    recipient.sendMessage(sender.getDisplayName() + ": " + translatedMessage);
                } catch (Exception e) {
                    // Handle failed translation
                    recipient.sendMessage(cleanedMessage);
                    sender.sendMessage(ChatColor.RED + "Your translation failed." +
                            ChatColor.GRAY + " Format: <sourceLang> message <targetLang>");
                }
            }
        }
    }

    private String translate(String textToTranslate, String targetLang, String sourceLang) throws Exception {
        String translatedText;
        String langPair = sourceLang + "|" + targetLang;
        String encodedText = URLEncoder.encode(textToTranslate, "UTF-8");
        URL url = new URL("https://api.mymemory.translated.net/get?q=" + encodedText + "&langpair=" + langPair);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            StringBuilder content = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            JSONObject json = new JSONObject(content.toString());
            translatedText = json.getJSONObject("responseData").getString("translatedText");

            if (translatedText.equalsIgnoreCase("PLEASE SELECT TWO DISTINCT LANGUAGES")){
                translatedText = textToTranslate;
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Translation failed");
        }

        return translatedText;
    }

}