package org.discu2.forum.packet;

import lombok.Data;
import org.discu2.forum.model.Account;

import java.util.ArrayList;
import java.util.List;

@Data
public class AccountPacket {

    private String id;
    private String username;
    private String nickname;
    private List<String> roles;

    public AccountPacket(Account account) {
        this.id = account.getId();
        this.username = account.getUsername();
        this.nickname = account.getNickname();
        this.roles = new ArrayList<>();
        account.getAuthorities().forEach(authority -> roles.add(authority.getAuthority()));
    }
}
