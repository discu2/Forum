package org.discu2.forum.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.UUID;

@Data
public class Account {

    @Id
    private String id;

    @Indexed(unique = true)
    private UUID uuid;

    @Indexed(unique = true)
    private String mail;

    @Indexed(unique = true)
    private String name;
    private String passWordHash;

    private boolean mailVerify;

    public Account(UUID uuid, String mail, String name, String passWordHash, boolean mailVerify) {
        this.uuid = uuid;
        this.mail = mail;
        this.name = name;
        this.passWordHash = passWordHash;
        this.mailVerify = mailVerify;
    }

}
