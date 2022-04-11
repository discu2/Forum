package org.discu2.forum.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
public class Avatar {

    @Id
    private String filename;
    private String contentType;

    @Indexed(unique = true)
    private String uuid;
    private byte[] picture;

    @Getter
    @AllArgsConstructor
    public enum Size {
        NORMAL(0,400), MEDIUM(1,200), SMALL(2,50);

        private int id;
        private int size;
    }
}
