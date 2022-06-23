package com.company;

import lombok.*;
import java.io.*;
import org.jetbrains.annotations.*;

@NoArgsConstructor
@AllArgsConstructor
public @Data class Author implements Serializable {

    private @NotNull String fullName = "Unnamed";

    @Serial private static final long serialVersionUID = 4L;

    @Contract(mutates = "param")
    public boolean CreditAsCoAuthor(@NotNull Book book) {
        return book.getAuthors().add(this);
    }
}
