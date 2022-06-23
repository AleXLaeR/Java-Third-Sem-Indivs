package com.company;

import java.util.*;
import org.jetbrains.annotations.NotNull;

public class Main {
    public static void main(String[] args) {

        var J_K_Rowling = new Author("J. K. Rowling");
        var harryPotterBook = Book.builder()
                .title("Harry Potter")
                .yearOfPublication(1997)
                .authors(List.of(J_K_Rowling, new Author("Jack Thorne")))
                .issue(3)
                .build();

        var unknownBook = new Book("Unknown", 1872, 1, new Author());
        var reader = new Reader("Adam Smith", 303);

        var localLibrary = Library.builder()
                .name("Local Library")
                .registeredReaders(new TreeSet<>())
                .storedBooks(new HashMap<>(Map.ofEntries(
                        Map.entry(harryPotterBook, 5),
                        Map.entry(unknownBook, 2))))
                .build();

        localLibrary.registerReader(reader);
        reader.loanBookFrom(localLibrary, unknownBook);
        localLibrary.reclaimBook(harryPotterBook, harryPotterBook, unknownBook);

        compareDeserializedObjectWithOriginal(localLibrary, "library.out");
        compareDeserializedObjectWithOriginal(harryPotterBook, "book.out");
    }

    private static void compareDeserializedObjectWithOriginal(@NotNull Object object,
                                                              @NotNull String fileName) {
        SerializationDriver.writeObject(object, fileName);
        var restoredObject = SerializationDriver.readObject(fileName);

        System.out.println(object);
        System.out.println(restoredObject + "\n");
    }
}
