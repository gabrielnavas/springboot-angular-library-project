package com.library.api.classification_book.hateoas;

public enum ClassificationBookHateoasWithRel {
    CREATE_CLASSIFICATION_BOOK("create new classification book"),
    UPDATE_PARTIALS_CLASSIFICATION_BOOK("update partials classification book"),
    GET_CLASSIFICATION_BOOK_BY_ID("get classification book by id"),
    GET_ALL_CLASSIFICATION_BOOKS("get all classification books"),
    REMOVE_CLASSIFICATION_BOOK_BY_ID("remove classification book by id"),
    SELF("self");

    private String description;

    ClassificationBookHateoasWithRel(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
