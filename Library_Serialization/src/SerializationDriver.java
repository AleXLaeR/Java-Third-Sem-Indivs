package com.company;

import java.io.*;
import org.jetbrains.annotations.*;

public final class SerializationDriver {
    @Nullable
    public static Object readObject(@NotNull String fileName) {
        try (var objectInputStream = new ObjectInputStream(
                new FileInputStream(fileName))) {
            return objectInputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeObject(@NotNull Object object,
                                   @NotNull String fileName) {
        try (var objectOutputStream = new ObjectOutputStream(
                new FileOutputStream(fileName))) {
            objectOutputStream.writeObject(object);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
