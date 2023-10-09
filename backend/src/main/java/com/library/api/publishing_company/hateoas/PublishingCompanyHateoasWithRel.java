package com.library.api.publishing_company.hateoas;

public enum PublishingCompanyHateoasWithRel {
    CREATE_PUBLISHING_COMPANY("create new publishing company"),
    UPDATE_PARTIALS_PUBLISHING_COMPANY("update partials publishing company"),
    GET_PUBLISHING_COMPANY_BY_ID("get publishing company by id"),
    GET_ALL_PUBLISHING_COMPANIES("get all publishing companies"),
    REMOVE_PUBLISHING_COMPANY_BY_ID("remove publishing company by id"),
    SELF("self");

    private String description;

    PublishingCompanyHateoasWithRel(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
