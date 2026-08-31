package com.translated.lara.translator;

import java.util.List;

public class GlossaryShares {
    private final Glossary glossary;
    private final ResourceShareEntry account;
    private final List<ResourceShareEntry> groups;
    private final List<ResourceShareEntry> users;

    public GlossaryShares(Glossary glossary, ResourceShareEntry account, List<ResourceShareEntry> groups, List<ResourceShareEntry> users) {
        this.glossary = glossary;
        this.account = account;
        this.groups = groups;
        this.users = users;
    }

    public Glossary getGlossary() { return glossary; }
    public ResourceShareEntry getAccount() { return account; }
    public List<ResourceShareEntry> getGroups() { return groups; }
    public List<ResourceShareEntry> getUsers() { return users; }
}
