package de.zappler2k.api.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import de.zappler2k.config.MessageManager;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

@Getter
public class HttpAPI {

    private HttpServer server;
    private MessageManager messageManager;

    @SneakyThrows
    public HttpAPI(MessageManager messageManager) {
        server = HttpServer.create(new InetSocketAddress(1234), 0);
        server.setExecutor(null);
        server.start();
        this.messageManager = messageManager;
    }

    @SneakyThrows
    public void createContent(String name, String content, Player player) {
        StringUploadHandler httpHandler = new StringUploadHandler();
        httpHandler.setContent(content);
        server.createContext("/" + name, httpHandler);

        TextComponent textComponent = new TextComponent("Website: http://" + messageManager.getMessageConfig().getAdress() + ":1234/" + name);
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://" + messageManager.getMessageConfig().getAdress() + ":1234/" + name));
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GRAY + "CLICK TO OPEN WEBSITE (Click)")));
        player.spigot().sendMessage(textComponent);
    }

    public void removeContent(String name) {
        server.removeContext("/" + name);
    }

    static class StringUploadHandler implements HttpHandler {
        @Setter
        public String content;

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                String response = content;
                exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
                exchange.sendResponseHeaders(200, response.getBytes("UTF-8").length);
                OutputStream output = exchange.getResponseBody();
                output.write(response.getBytes("UTF-8"));
                output.close();
            } else {
                String response = "Ungültige Anfrage.";
                exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
                exchange.sendResponseHeaders(400, response.getBytes("UTF-8").length);
                OutputStream output = exchange.getResponseBody();
                output.write(response.getBytes("UTF-8"));
                output.close();
            }
        }
    }
}
