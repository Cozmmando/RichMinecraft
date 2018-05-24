package io.github.xdotdash.richminecraft;

import club.minnced.discord.rpc.DiscordRichPresence;
import io.github.xdotdash.richminecraft.config.Config;
import io.github.xdotdash.richminecraft.config.DisplayDataManager;
import io.github.xdotdash.richminecraft.config.display.ServerDisplay;
import io.github.xdotdash.richminecraft.config.display.SmallDataDisplay;
import io.github.xdotdash.richminecraft.util.MiscUtil;

import static io.github.xdotdash.richminecraft.util.TimeUtil.epochSecond;

public class PresenceManager {

    private final DisplayDataManager dataManager;
    private final Config config;

    private final DiscordRichPresence loadingGame = new DiscordRichPresence();
    private final DiscordRichPresence mainMenu = new DiscordRichPresence();
    private final DiscordRichPresence inGame = new DiscordRichPresence();

    public boolean currentlyIngame = false;

    public PresenceManager(DisplayDataManager dataManager, Config config) {
        this.dataManager = dataManager;
        this.config = config;

        loadingGame.state = "Loading the Game";
        loadingGame.largeImageKey = "state-loading";
        loadingGame.largeImageText = "CozRPG 2";

        mainMenu.state = "In the Main Menu";
        mainMenu.largeImageKey = "state-menu";
        mainMenu.largeImageText = "CozRPG 2";

        SmallDataDisplay smallData = dataManager.getSmallDataDisplays().get(this.config.displayData.smallDataUid);

        if (smallData == null) {
            return;
        }

        loadingGame.smallImageKey = smallData.key;
        loadingGame.smallImageText = smallData.name;

        mainMenu.smallImageKey = smallData.key;
        mainMenu.smallImageText = smallData.name;

        inGame.smallImageKey = smallData.key;
        inGame.smallImageText = smallData.name;
    }

    public DiscordRichPresence loadingGame() {
        loadingGame.startTimestamp = epochSecond();
        return loadingGame;
    }

    public DiscordRichPresence mainMenu() {
        mainMenu.startTimestamp = epochSecond();
        return mainMenu;
    }

    public DiscordRichPresence ingameMP(String ip, int playerCount, int maxPlayers) {
        ServerDisplay server = dataManager.getServerDisplays().get(ip);

        if (server != null) {
            inGame.largeImageKey = server.key;
            inGame.largeImageText = "IP: " + server.name;
        } else if (this.config.displayData.hideUnknownIps) {
            inGame.largeImageKey = "state-multiplayer";
            inGame.largeImageText = "Unknown Server";
        } else {
            inGame.largeImageKey = "state-multiplayer";
            inGame.largeImageText = "IP: " + ip;
        }

        inGame.state = "In a Server";
        inGame.details = "IGN: " + MiscUtil.getIGN();
        inGame.startTimestamp = epochSecond();
        inGame.partyId = ip;
        inGame.partySize = playerCount;
        inGame.partyMax = maxPlayers;

        return inGame;
    }

    public DiscordRichPresence updatePlayerCount(int playerCount) {
        inGame.partySize = playerCount;

        return inGame;
    }

    public DiscordRichPresence ingameSP(String world) {
        inGame.state = "In Singleplayer";
        inGame.details = "IGN: " + MiscUtil.getIGN();
        inGame.startTimestamp = epochSecond();
        inGame.largeImageKey = "state-singleplayer";
        inGame.largeImageText = "World: " + world;
        inGame.partyId = "";
        inGame.partySize = 0;
        inGame.partyMax = 0;

        return inGame;
    }
}
