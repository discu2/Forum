package org.discu2.forum.account.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
public class ProfilePic {

    @Id
    private String username;
    private String contentType;
    private byte[] picture;
}
