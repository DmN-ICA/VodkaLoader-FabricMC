package DmN.ICA.vodka.entity.api;

import DmN.ICA.vodka.text.api.IText;

public interface IPlayerEntity extends ILivingEntity {
    IText getName();

    void sendMessage(IText text);
}
