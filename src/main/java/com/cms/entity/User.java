package com.cms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Component
@Document(collection = "users")
@Data

public class User {
    @Id
    private String id;
   // @Indexed(unique = true)
    @NonNull
    private String username;
    @NonNull
    private String password;
    @DBRef
    private UserProfile userProfileId;

    @CreatedDate
    private Date creationDate;

    @LastModifiedDate
    private Date lastModifiedDate;

    @DBRef
    private List<Post> posts = new ArrayList<>();
    // "ROLE_USER,ROLE_ADMIN"
    private String roles = "User_Role";
    // Additional properties for user profile
    private boolean makeProfileStatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id); // Use unique identifier
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Use unique identifier
    }


    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username='" + username + '\'' + '}';
    }

}

