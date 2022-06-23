package com.company;

import lombok.*;
import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

import org.jetbrains.annotations.*;

@NoArgsConstructor @AllArgsConstructor
@Builder(builderClassName = "LibraryBuilder")
public @Data class Library implements Serializable {

    private @NotNull String name;
    private Set<Reader> registeredReaders = new TreeSet<>();
    private transient Map<Book, Integer> storedBooks = new HashMap<>();
    @Serial private static final long serialVersionUID = 2L;

    public Library(@NotNull String name) {
        this.name = name;
    }

    public Library(@NotNull String name,
                   @NotNull Map<Book, Integer> storedBooks,
                   @Nullable Reader... libraryReaders) {
        this.name = name;
        this.storedBooks.putAll(storedBooks);
        if (libraryReaders != null)
            this.registeredReaders.addAll(Arrays.stream(libraryReaders).toList());
    }

    @SafeVarargs
    public Library(@NotNull String name,
                   @NotNull Collection<Reader> libraryReaders,
                   @Nullable Map.Entry<Book, Integer>... storedBooks) {
        this.name = name;
        this.registeredReaders.addAll(libraryReaders);
        if (storedBooks != null) {
            Arrays.stream(storedBooks).forEach(bookEntry ->
                    this.storedBooks.put(Objects.requireNonNull(
                                    bookEntry).getKey(),
                            bookEntry.getValue()));
        }
    }

    public boolean IsBookPresent(@NotNull Book book) {
        return storedBooks.containsKey(book) && storedBooks.get(book) >= 1;
    }

    public boolean registerReader(@NotNull Reader reader) {
        return this.registeredReaders.add(reader);
    }

    public void reclaimBook(@NotNull Book book) {
        if (this.storedBooks.computeIfPresent(book, (k, v) -> v + 1) == null) {
            this.storedBooks.put(book, 1);
        }
    }

    public void reclaimBook(Book @NotNull... books) {
        for (Book book : books)
            this.reclaimBook(book);
    }

    public void onBookLoan(@NotNull Book book) {
        var requestedBookAmount = this.storedBooks.computeIfPresent(book, (k, v) -> v - 1);
        if (Objects.requireNonNull(requestedBookAmount) == 0)
            this.storedBooks.remove(book);
    }


    @Serial private void writeObject(@NotNull ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(this.storedBooks);
    }

    @SuppressWarnings("unchecked")
    @Serial private void readObject(@NotNull ObjectInputStream in)
            throws IOException,ClassNotFoundException {
        in.defaultReadObject();
        this.storedBooks = (Map<Book, Integer>) in.readObject();
    }
}
