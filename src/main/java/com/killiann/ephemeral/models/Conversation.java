package com.killiann.ephemeral.models;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "conversation")
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(mappedBy = "conversations")
    private Set<UserModel> users;


    @OneToOne(mappedBy = "conversation")
    private Event event;

    @OneToMany(mappedBy = "conversation")
    private Set<Message> messages;

    private Long createdAt;

    /* Default constructor */
    public Conversation() {}

    public Conversation(Set<UserModel> users, Event event, Set<Message> messages, Long createdAt) {
        this.users = users;
        this.event = event;
        this.messages = messages;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Set<UserModel> getUsers() {
        return users;
    }

    public void setUsers(Set<UserModel> users) {
        this.users = users;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                '}';
    }
}
