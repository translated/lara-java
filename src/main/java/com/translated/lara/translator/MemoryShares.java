package com.translated.lara.translator;

import java.util.List;

public class MemoryShares {
    private final Memory memory;
    private final ResourceShareEntry account;
    private final List<ResourceShareEntry> groups;
    private final List<ResourceShareEntry> users;

    public MemoryShares(Memory memory, ResourceShareEntry account, List<ResourceShareEntry> groups, List<ResourceShareEntry> users) {
        this.memory = memory;
        this.account = account;
        this.groups = groups;
        this.users = users;
    }

    public Memory getMemory() { return memory; }
    public ResourceShareEntry getAccount() { return account; }
    public List<ResourceShareEntry> getGroups() { return groups; }
    public List<ResourceShareEntry> getUsers() { return users; }
}
