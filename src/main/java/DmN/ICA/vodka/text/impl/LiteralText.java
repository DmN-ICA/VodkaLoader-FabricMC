package DmN.ICA.vodka.text.impl;

import DmN.ICA.vodka.text.api.IText;
import DmN.ICA.vodka.text.api.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LiteralText implements IText {
    public final @NotNull String text;
    public final List<IText> siblings = new ArrayList<>();
    public final @NotNull Style style;

    public LiteralText(@Nullable String text, @Nullable Style style) {
        this.text = text == null ? "" : text;
        this.style = style == null ? Style.EMPTY : style;
    }

    public LiteralText(@NotNull IText text, @Nullable Style style) {
        this.text = text.toString();
        this.style = style == null ? text.getStyle() : style;
    }

    @Override
    public @NotNull Style getStyle() {
        return this.style;
    }

    @Override
    public @NotNull String toString() {
        StringBuilder sb = new StringBuilder(this.text);
        for (IText text : this.siblings)
            sb.append(text.toString());
        return sb.toString();
    }

    @Override
    public @NotNull String asString() {
        StringBuilder sb = new StringBuilder(this.text);
        for (IText text : this.siblings)
            sb.append(text.asString());
        return sb.toString();
    }
}
