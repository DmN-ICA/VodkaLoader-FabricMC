package DmN.ICA.vodka.util;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;

/**
 * An enum holding formattings.
 *
 * <p>There are two types of formattings, color and modifier. Color formattings
 * are associated with a specific color, while modifier formattings modify the
 * style, such as by bolding the text. {@link #RESET} is a special formatting
 * and is not classified as either of these two.
 */
public enum Formatting {
    BLACK("BLACK", '0', 0, 0),
    DARK_BLUE("DARK_BLUE", '1', 1, 170),
    DARK_GREEN("DARK_GREEN", '2', 2, 43520),
    DARK_AQUA("DARK_AQUA", '3', 3, 43690),
    DARK_RED("DARK_RED", '4', 4, 11141120),
    DARK_PURPLE("DARK_PURPLE", '5', 5, 11141290),
    GOLD("GOLD", '6', 6, 16755200),
    GRAY("GRAY", '7', 7, 11184810),
    DARK_GRAY("DARK_GRAY", '8', 8, 5592405),
    BLUE("BLUE", '9', 9, 5592575),
    GREEN("GREEN", 'a', 10, 5635925),
    AQUA("AQUA", 'b', 11, 5636095),
    RED("RED", 'c', 12, 16733525),
    LIGHT_PURPLE("LIGHT_PURPLE", 'd', 13, 16733695),
    YELLOW("YELLOW", 'e', 14, 16777045),
    WHITE("WHITE", 'f', 15, 16777215),
    OBFUSCATED("OBFUSCATED", 'k', true),
    BOLD("BOLD", 'l', true),
    STRIKETHROUGH("STRIKETHROUGH", 'm', true),
    UNDERLINE("UNDERLINE", 'n', true),
    ITALIC("ITALIC", 'o', true),
    RESET("RESET", 'r', -1, null);

    public static final char FORMATTING_CODE_PREFIX = '§';
    public static final Map<String, Formatting> BY_NAME = Arrays.stream(values()).collect(Collectors.toMap((f) -> sanitize(f.name), (f) -> f));
    public static final Pattern FORMATTING_CODE_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]");
    public final String name;
    public final char code;
    public final boolean modifier;
    public final String stringValue;
    public final int colorIndex;
    @Nullable
    public final Integer colorValue;

    public static String sanitize(String name) {
        return name.toLowerCase(Locale.ROOT).replaceAll("[^a-z]", "");
    }

    Formatting(String name, char code, int colorIndex, @Nullable Integer colorValue) {
        this(name, code, false, colorIndex, colorValue);
    }

    Formatting(String name, char code, boolean modifier) {
        this(name, code, modifier, -1, null);
    }

    Formatting(String name, char code, boolean modifier, int colorIndex, @Nullable Integer colorValue) {
        this.name = name;
        this.code = code;
        this.modifier = modifier;
        this.colorIndex = colorIndex;
        this.colorValue = colorValue;
        this.stringValue = "§" + code;
    }

    /**
     * {@return the code to be placed after the {@value FORMATTING_CODE_PREFIX} when this format is converted to a string}
     */
    public char getCode() {
        return this.code;
    }

    /**
     * {@return the color index for the formatting, or {@code -1} to indicate no color}
     *
     * @apiNote This is also used to calculate scoreboard team display slot IDs.
     */
    public int getColorIndex() {
        return this.colorIndex;
    }

    /**
     * {@return true if the formatting is a modifier, false otherwise}
     */
    public boolean isModifier() {
        return this.modifier;
    }

    /**
     * {@return true if the formatting is associated with a color, false otherwise}
     */
    public boolean isColor() {
        return !this.modifier && this != RESET;
    }

    /**
     * {@return the color of the formatted text, or {@code null} if the formatting
     * has no associated color}
     */
    @Nullable
    public Integer getColorValue() {
        return this.colorValue;
    }

    /**
     * {@return the name of the formatting}
     */
    public String getName() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    public String toString() {
        return this.stringValue;
    }

    /**
     * {@return the {@code text} with all formatting codes removed}
     *
     */
    @Nullable
    public static String strip(@Nullable String string) {
        return string == null ? null : FORMATTING_CODE_PATTERN.matcher(string).replaceAll("");
    }

    /**
     * {@return the formatting with the name {@code name}, or {@code null} if there is none}
     */
    @Nullable
    public static Formatting byName(@Nullable String name) {
        return name == null ? null : BY_NAME.get(sanitize(name));
    }

    /**
     * {@return the formatting with the color index {@code colorIndex},
     * or {@code null} if there is none}
     */
    @Nullable
    public static Formatting byColorIndex(int colorIndex) {
        if (colorIndex < 0) {
            return RESET;
        } else {
            Formatting[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                Formatting formatting = var1[var3];
                if (formatting.getColorIndex() == colorIndex) {
                    return formatting;
                }
            }

            return null;
        }
    }

    /**
     * {@return the formatting with the code {@code code}, or {@code null} if there is none}
     */
    @Nullable
    public static Formatting byCode(char code) {
        char c = Character.toString(code).toLowerCase(Locale.ROOT).charAt(0);
        Formatting[] var2 = values();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Formatting formatting = var2[var4];
            if (formatting.code == c) {
                return formatting;
            }
        }

        return null;
    }

    /**
     * {@return the list of formattings matching the given condition}
     *
     * @param colors whether or not to include color formattings
     * @param modifiers whether or not to include modifier formattings
     */
    public static Collection<String> getNames(boolean colors, boolean modifiers) {
        List<String> list = Lists.newArrayList();
        Formatting[] var3 = values();
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Formatting formatting = var3[var5];
            if ((!formatting.isColor() || colors) && (!formatting.isModifier() || modifiers)) {
                list.add(formatting.getName());
            }
        }

        return list;
    }
}

