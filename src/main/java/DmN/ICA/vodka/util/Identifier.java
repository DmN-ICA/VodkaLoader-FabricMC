package DmN.ICA.vodka.util;

import org.jetbrains.annotations.Nullable;

/**
 * The namespace and path must contain only lowercase letters ([a-z]), digits ([0-9]), or the characters '_', '.', and '-'. The path can also contain the standard path separator '/'.
 */
public class Identifier implements Comparable<Identifier> {
    public final String namespace;
    public final String path;

    public Identifier(String[] id) {
        this.namespace = id[0].isEmpty()? "minecraft" : id[0];
        this.path = id[1];
        if (!isNamespaceValid(this.namespace)) {
            throw new InvalidIdentifierException("Non [a-z0-9_.-] character in namespace of location: " + this.namespace + ":" + this.path);
        } else if (!isPathValid(this.path)) {
            throw new InvalidIdentifierException("Non [a-z0-9/._-] character in path of location: " + this.namespace + ":" + this.path);
        }
    }

    /**
     * <p>Takes a string of the form {@code <namespace>:<path>}, for example {@code minecraft:iron_ingot}.
     * <p>The string will be split (on the {@code :}) into an identifier with the specified path and namespace.
     * Prefer using the {@link net.minecraft.util.Identifier#Identifier(java.lang.String, java.lang.String) Identifier(java.lang.String, java.lang.String)} constructor that takes the namespace and path as individual parameters to avoid mistakes.
     * @throws InvalidIdentifierException if the string cannot be parsed as an identifier.
     */
    public Identifier(String id) {
        this(split(id, ':'));
    }

    public Identifier(String namespace, String path) {
        this(new String[]{namespace, path});
    }

    public static Identifier splitOn(String id, char delimiter) {
        return new Identifier(split(id, delimiter));
    }

    /**
     * <p>Parses a string into an {@code Identifier}.
     * Takes a string of the form {@code <namespace>:<path>}, for example {@code minecraft:iron_ingot}.
     * @return resulting identifier, or {@code null} if the string couldn't be parsed as an identifier
     */
    @Nullable
    public static Identifier tryParse(String id) {
        try {
            return new Identifier(id);
        } catch (InvalidIdentifierException var2) {
            return null;
        }
    }

    public static String[] split(String id, char delimiter) {
        String[] strings = new String[]{"minecraft", id};
        int i = id.indexOf(delimiter);
        if (i >= 0) {
            strings[1] = id.substring(i + 1, id.length());
            if (i >= 1) {
                strings[0] = id.substring(0, i);
            }
        }

        return strings;
    }

    public String getPath() {
        return this.path;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public String toString() {
        return this.namespace + ":" + this.path;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof Identifier identifier)) {
            return false;
        } else {
            return this.namespace.equals(identifier.namespace) && this.path.equals(identifier.path);
        }
    }

    public int hashCode() {
        return 31 * this.namespace.hashCode() + this.path.hashCode();
    }

    public int compareTo(Identifier identifier) {
        int i = this.path.compareTo(identifier.path);
        if (i == 0) {
            i = this.namespace.compareTo(identifier.namespace);
        }

        return i;
    }

    public static boolean isPathValid(String path) {
        for(int i = 0; i < path.length(); ++i) {
            if (!isPathCharacterValid(path.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public static boolean isNamespaceValid(String namespace) {
        for(int i = 0; i < namespace.length(); ++i) {
            if (!isNamespaceCharacterValid(namespace.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public static boolean isPathCharacterValid(char character) {
        return character == '_' || character == '-' || character >= 'a' && character <= 'z' || character >= '0' && character <= '9' || character == '/' || character == '.';
    }

    public static boolean isNamespaceCharacterValid(char character) {
        return character == '_' || character == '-' || character >= 'a' && character <= 'z' || character >= '0' && character <= '9' || character == '.';
    }

    public static boolean isValid(String id) {
        String[] strings = split(id, ':');
        return isNamespaceValid(strings[0].isEmpty() ? "minecraft" : strings[0]) && isPathValid(strings[1]);
    }

    public static class InvalidIdentifierException extends RuntimeException {
        public InvalidIdentifierException(String message) {
            super(message);
        }

        public InvalidIdentifierException(String message, Throwable throwable) {
            super(message, throwable);
        }
    }
}
