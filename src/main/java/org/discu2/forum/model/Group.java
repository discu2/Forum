package org.discu2.forum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@Document
public class Group {

    @Id
    private String id;

    @Indexed(unique = true)
    private String name;
    private List<Role> permissions;

}
