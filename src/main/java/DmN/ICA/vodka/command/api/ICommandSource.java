package DmN.ICA.vodka.command.api;

import DmN.ICA.vodka.server.api.IMinecraftServer;
import DmN.ICA.vodka.text.api.IText;

public interface ICommandSource {
    IMinecraftServer getServer();

    void sendFeedback(IText text);
}
