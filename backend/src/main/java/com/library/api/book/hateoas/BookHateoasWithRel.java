package com.library.api.book.hateoas;

public enum BookHateoasWithRel {
    CREATE_BOOK("create new book"),
    UPDATE_PARTIALS_BOOK("update partials book"),
    GET_BOOK_BY_ID("get book by id"),
    GET_ALL_BOOKS("get all books"),
    REMOVE_BOOK_BY_ID("remove book by id"),
    SELF("self");

    private String description;

    BookHateoasWithRel(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
