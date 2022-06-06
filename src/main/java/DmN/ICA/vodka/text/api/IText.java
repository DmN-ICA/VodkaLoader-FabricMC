package DmN.ICA.vodka.text.api;

import DmN.ICA.vodka.text.impl.LiteralText;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IText {
    IText EMPTY = new LiteralText("", Style.EMPTY);

    @NotNull Style getStyle();

    @NotNull static IText ofString(@Nullable  String text, @Nullable Style style) {
        return new LiteralText(text, style);
    }

    @NotNull String asString();

    @NotNull String toString();
}
