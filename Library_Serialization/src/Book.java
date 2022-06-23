package com.company;

import lombok.*;
import java.io.*;
import java.util.*;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Builder(builderClassName = "BookBuilder")
public @Data class Book implements Serializable, Comparable<Book> {
    private int issue;
    private @NotNull String title;
    private int yearOfPublication = CURRENT_YEAR;
    private List<Author> authors = new LinkedList<>();

    @Serial private static final long serialVersionUID = 1L;
    private transient static final int CURRENT_YEAR = 2022;

    public Book(@NotNull String title,
                int bookIssue,
                @NotNull Author... authors) {
        this.title = title;
        this.issue = bookIssue;
        this.authors.addAll(Arrays.stream(authors).toList());
    }

    public Book(@NotNull String title,
                int yearOfPublication,
                int bookIssue,
                @NotNull Author... authors) {
        this.title = title;
        this.yearOfPublication = yearOfPublication;
        this.issue = bookIssue;
        this.authors.addAll(Arrays.stream(authors).toList());
    }

    public boolean checkIsAuthor(@NotNull Author author) {
        return this.authors.contains(author);
    }

    @Override
    public int compareTo(@NotNull Book o) {
        return Comparator
                .comparing(Book::getTitle)
                .thenComparing(Book::getYearOfPublication)
                .compare(this, o);
    }
}
