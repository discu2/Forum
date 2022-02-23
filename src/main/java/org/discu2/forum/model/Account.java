package org.discu2.forum.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
public class Account {

    @Id
    private String id;

    @Indexed(unique = true)
    private String mail;

    @Indexed(unique = true)
    private String name;
    private String passWordHash;

    private boolean mailVerify;

    public Account(String mail, String name, String passWordHash, boolean mailVerify) {
        this.mail = mail;
        this.name = name;
        this.passWordHash = passWordHash;
        this.mailVerify = mailVerify;
    }
}
