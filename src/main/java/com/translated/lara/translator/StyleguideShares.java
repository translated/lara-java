package com.translated.lara.translator;

import java.util.List;

public class StyleguideShares {
    private final Styleguide styleguide;
    private final ResourceShareEntry account;
    private final List<ResourceShareEntry> groups;
    private final List<ResourceShareEntry> users;

    public StyleguideShares(Styleguide styleguide, ResourceShareEntry account, List<ResourceShareEntry> groups, List<ResourceShareEntry> users) {
        this.styleguide = styleguide;
        this.account = account;
        this.groups = groups;
        this.users = users;
    }

    public Styleguide getStyleguide() { return styleguide; }
    public ResourceShareEntry getAccount() { return account; }
    public List<ResourceShareEntry> getGroups() { return groups; }
    public List<ResourceShareEntry> getUsers() { return users; }
}
