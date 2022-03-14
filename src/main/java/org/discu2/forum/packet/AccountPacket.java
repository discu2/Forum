package org.discu2.forum.packet;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.discu2.forum.model.Account;

@Data
public class AccountPacket {

    private String id;
    private String username;
    private String nickname;

    public AccountPacket(Account account) {
        this.id = account.getId();
        this.username = account.getUsername();
        this.nickname = account.getNickname();
    }
}
