package org.discu2.forum.account.controller;

import lombok.AllArgsConstructor;
import org.discu2.forum.account.service.AccountService;
import org.discu2.forum.common.exception.BadPacketFormatException;
import org.discu2.forum.common.model.Avatar;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/avatar")
@AllArgsConstructor
public class AvatarController {

    private AccountService accountService;

    @GetMapping
    public void getAvatar(@RequestParam(name = "id", defaultValue = "") String avatarId, HttpServletResponse response) throws IOException {

        var pic = accountService.loadAvatarById(avatarId);

        response.setContentType(pic.getContentType());
        response.getOutputStream().write(pic.getPicture());
    }

    @PreAuthorize("authentication.name == #username or hasRole('ADMIN')")
    @PostMapping(value = "/{username}")
    public void uploadProfilePicture(@PathVariable String username, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        accountService.addAvatar(username,
                Optional.ofNullable(request.getPart("Image"))
                        .orElseThrow(() -> new BadPacketFormatException("Missing \"Image\" key in body")));
    }

    @GetMapping(value = "/{username}")
    public void getProfilePicture(@PathVariable String username, @RequestParam(name = "size",defaultValue = "0") int sizeId,
                                  HttpServletResponse response) throws IOException {

        Avatar.Size size = null;

        for (var s : Avatar.Size.values())
            if (s.getId() == sizeId) size = s;

        var pic = accountService.loadAvatarByUsername(username, size);

        response.setContentType(pic.getContentType());
        response.getOutputStream().write(pic.getPicture());
    }
}
