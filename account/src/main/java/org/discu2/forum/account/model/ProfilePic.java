package org.discu2.forum.account.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
public class ProfilePic {

    @Id
    private String filename;
    private String contentType;
    private byte[] picture;

    @Getter
    @AllArgsConstructor
    public enum ProfilePicSize {
        NORMAL(0,400), MEDIUM(1,200), SMALL(2,50);

        private int id;
        private int size;
    }
}
