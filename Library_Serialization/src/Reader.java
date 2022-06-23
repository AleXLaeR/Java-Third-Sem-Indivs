package com.company;

import lombok.*;
import java.io.*;
import java.util.*;
import org.jetbrains.annotations.*;

@NoArgsConstructor
public @Data class Reader implements /* Serializable, */
                                        Externalizable,
                                        Comparable<Reader> {
    private int registrationID;
    private @NotNull String fullName;
    private transient List<Book> loanedBooks = new LinkedList<>();
    @Serial private static final long serialVersionUID = 3L;

    public Reader(@NotNull String name,
                  int registrationID) {
        this.fullName = name;
        this.registrationID = registrationID;
    }

    public Reader(@NotNull String name,
                  int registrationID,
                  @Nullable Book... loanedBooks) {
        this.fullName = name;
        this.registrationID = registrationID;
        if (loanedBooks != null)
            this.loanedBooks.addAll(Arrays.stream(loanedBooks).toList());
    }

    public boolean hasBook(@NotNull Book book) {
        return this.loanedBooks.contains(book);
    }

    @Contract(mutates = "param1")
    public boolean loanBookFrom(@NotNull Library library,
                                @NotNull Book book) {
        if (!library.IsBookPresent(book)) return false;

        this.loanedBooks.add(book);
        library.onBookLoan(book);
        return true;
    }

    @Contract(mutates = "param1")
    public void returnBookTo(@NotNull Library library,
                             @NotNull Book book) {
        this.loanedBooks.remove(book);
        library.reclaimBook(book);
    }

    @Override
    public int compareTo(@NotNull Reader o) {
        return Comparator.comparing(Reader::getFullName)
                .thenComparing(Reader::getRegistrationID)
                .compare(this, o);
    }

    // serializable uses reflection api; memory's being allocated, then fields are init with values from a stream
    // externalizable implementation requires def constructor call, and then readExternal() is being called
    // using externalizable for static variables is allowed, however it comes with sneaky errors
    // using externalizable for final variables is prohibited because they must be initialized in def constructor
    @Override
    public void writeExternal(@NotNull ObjectOutput out) throws IOException {
        out.writeObject(this.fullName);
        out.writeObject(this.registrationID);
        out.writeObject(this.loanedBooks);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void readExternal(@NotNull ObjectInput in) throws IOException,
                                                             ClassNotFoundException {
        this.fullName = (String) in.readObject();
        this.registrationID = (int) in.readObject();
        this.loanedBooks = (List<Book>) in.readObject();
    }
}

