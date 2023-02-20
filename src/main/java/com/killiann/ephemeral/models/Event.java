package com.killiann.ephemeral.models;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String facebookId;

    private String name;

    @ManyToOne()
    @JoinColumn(name = "events", nullable = false)
    private Venue venue;

    @ManyToOne()
    @JoinColumn(name="owner", nullable=false)
    private UserModel owner;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "event", referencedColumnName = "id")
    private Conversation conversation;

    @ManyToMany(mappedBy = "savedEvents")
    private Set<UserModel> attendees = new HashSet<>();

    private Long occurDateTime;

    /* Default constructor */
    public Event(){}
    public Event(String facebookId, String name, Venue venue, UserModel owner, Long occurDateTime) {
        this.facebookId = facebookId;
        this.name = name;
        this.venue = venue;
        this.owner = owner;
        this.occurDateTime = occurDateTime;
    }

    public Long getId() {
        return id;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Venue getLocation() {
        return venue;
    }

    public void setLocation(Venue venue) {
        this.venue = venue;
    }

    public UserModel getOwner() {
        return owner;
    }

    public void setOwner(UserModel owner) {
        this.owner = owner;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public Long getOccurDateTime() {
        return occurDateTime;
    }

    public void setOccurDateTime(Long occurDateTime) {
        this.occurDateTime = occurDateTime;
    }

    public Set<UserModel> getAttendees() {
        return attendees;
    }

    public void setAttendees(Set<UserModel> attendees) {
        this.attendees = attendees;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", facebookId='" + facebookId + '\'' +
                ", name='" + name + '\'' +
                ", location='" + venue.toString() + '\'' +
                ", owner=" + owner +
                ", occurDateTime=" + occurDateTime +
                '}';
    }
}
