package com.hashtagplus.model;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class HtplUser extends User {

  @GraphId
  private String id;

  @Relationship(type = "TEAMMATE", direction = Relationship.UNDIRECTED)
  public Set<HtplUser> friends;



  public HtplUser(String username, String password, String id, Collection<? extends GrantedAuthority> authorities) {
    super(username, password, authorities);
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void addFriend(HtplUser person) {
    if (friends == null) {
      friends = new HashSet<>();
    }
    friends.add(person);
  }
}
