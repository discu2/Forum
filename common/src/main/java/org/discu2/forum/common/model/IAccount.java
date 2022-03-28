package org.discu2.forum.common.model;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Set;

public interface IAccount extends UserDetails {

    Set<String> getRoleIds();

    String getMail();

    boolean isMailVerify();

    List<String> getRefreshTokenUUIDs();

    String getNickname();
}
