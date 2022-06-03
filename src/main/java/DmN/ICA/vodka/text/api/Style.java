package DmN.ICA.vodka.text.api;

import DmN.ICA.vodka.util.Formatting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Style {
    public static final Style EMPTY = new Style(null, false, false, false, false, false);
    public final @Nullable Formatting color;
    public final boolean bold;
    public final boolean italic;
    public final boolean underlined;
    public final boolean strikethrough;
    public final boolean obfuscated;

    public Style(@Nullable Formatting color, boolean bold, boolean italic, boolean underlined, boolean strikethrough, boolean obfuscated) {
        this.color = color;
        this.bold = bold;
        this.italic = italic;
        this.underlined = underlined;
        this.strikethrough = strikethrough;
        this.obfuscated = obfuscated;
    }

    public @NotNull Style withColor(@Nullable Formatting color) {
        return new Style(color, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated);
    }

    public @NotNull Style withBold(boolean bold) {
        return new Style(this.color, bold, this.italic, this.underlined, this.strikethrough, this.obfuscated);
    }

    public @NotNull Style withItalic(boolean italic) {
        return new Style(this.color, this.bold, italic, this.underlined, this.strikethrough, this.obfuscated);
    }

    public @NotNull Style withUnderlined(boolean underlined) {
        return new Style(this.color, this.bold, this.italic, underlined, this.strikethrough, this.obfuscated);
    }

    public @NotNull Style withStrikethrough(boolean strikethrough) {
        return new Style(this.color, this.bold, this.italic, this.underlined, strikethrough, this.obfuscated);
    }

    public @NotNull Style withObfuscated(boolean obfuscated) {
        return new Style(this.color, this.bold, this.italic, this.underlined, this.strikethrough, obfuscated);
    }

    public Style withFormatting(Formatting formatting) {
        Formatting textColor = this.color;
        boolean bold = this.bold;
        boolean italic = this.italic;
        boolean strikethrough = this.strikethrough;
        boolean underlined = this.underlined;
        boolean obfuscated = this.obfuscated;
        switch (formatting) {
            case OBFUSCATED:
                obfuscated = true;
                break;
            case BOLD:
                bold = true;
                break;
            case STRIKETHROUGH:
                strikethrough = true;
                break;
            case UNDERLINE:
                underlined = true;
                break;
            case ITALIC:
                italic = true;
                break;
            case RESET:
                return EMPTY;
            default:
                textColor = formatting;
        }

        return new Style(textColor, bold, italic, strikethrough, underlined, obfuscated);
    }

    public Formatting toFormatting() {
        if (this.color != null)
            return this.color;
        if (this.bold)
            return Formatting.BOLD;
        if (this.obfuscated)
            return Formatting.OBFUSCATED;
        if (this.strikethrough)
            return Formatting.STRIKETHROUGH;
        if (this.underlined)
            return Formatting.UNDERLINE;
        if (this.italic)
            return Formatting.ITALIC;
        return Formatting.RESET;
    }
}
