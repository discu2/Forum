package org.discu2.forum.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Account {

    @Id
    private String id;
    private String userName;
    private String mail;
    private int passWordHash;

    public Account(String userName, String mail, int passWordHash) {
        this.userName = userName;
        this.mail = mail;
        this.passWordHash = passWordHash;
    }
}
