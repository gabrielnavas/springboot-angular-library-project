package com.library.api.author_book.hateoas;

public enum AuthorBookHateoasWithRel {
    CREATE_AUTHOR_BOOK("create new author book"),
    UPDATE_PARTIALS_AUTHOR_BOOK("update partials author book"),
    GET_AUTHOR_BOOK_BY_ID("get author book by id"),
    GET_ALL_AUTHOR_BOOKS("get all author books"),
    REMOVE_AUTHOR_BOOK_BY_ID("remove author book by id"),
    SELF("self");

    private String description;

    AuthorBookHateoasWithRel(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
