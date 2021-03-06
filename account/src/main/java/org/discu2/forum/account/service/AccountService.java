package org.discu2.forum.account.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.discu2.forum.account.model.Account;
import org.discu2.forum.common.model.Avatar;
import org.discu2.forum.account.repository.AccountRepository;
import org.discu2.forum.account.repository.AvatarRepository;
import org.discu2.forum.common.exception.AlreadyExistException;
import org.discu2.forum.common.exception.BadPacketFormatException;
import org.discu2.forum.common.exception.DataNotFoundException;
import org.discu2.forum.common.exception.IllegalFileException;
import org.discu2.forum.common.packet.AccountUpdateRequestPacket;
import org.imgscalr.Scalr;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.servlet.http.Part;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;


@Service
@AllArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final AvatarRepository avatarRepository;

    private static final Set<String> BLACK_LIST_USERNAMES = Sets.newHashSet("login", "refresh_token", "register");
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[A-Z0-9._].{4,16}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[A-Z])(?=\\S+$).{8,64}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern MAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    /**
     *
     * @param roleId - Leave this null or empty String to use DEFAULT role
     * @param nickname - Leave this null or empty String to use username for nickname
     */
    public Account registerNewAccount(@NonNull String username, @NonNull String password, String roleId, @NonNull String mail, String nickname)
            throws AlreadyExistException, DataNotFoundException, BadPacketFormatException {

        validateUsername(username);
        validatePassword(password);
        validateMail(mail);

        if (BLACK_LIST_USERNAMES.contains(username)) throw new AlreadyExistException(Account.class);

        var account = new Account(
                username,
                passwordEncoder.encode(password),
                Strings.isNullOrEmpty(roleId) ? Sets.newHashSet(roleService.loadRoleByName("DEFAULT").getId()) : Sets.newHashSet(roleId),
                true,
                true,
                true,
                true,
                mail,
                false,
                Lists.newArrayList(),
                Strings.isNullOrEmpty(nickname) ? username : nickname,
                new EnumMap<>(Avatar.Size.class)
        );

        try {
            return accountRepository.insert(account);
        } catch (Exception e) {
            throw new AlreadyExistException(Account.class);
        }

    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {

        var dataNotFound = new UsernameNotFoundException(String.format("Username %s not found", username));

        if (MAIL_PATTERN.matcher(username).find()) {
            return getAccountByMail(username).orElseThrow(() -> dataNotFound);
        }

        return getAccountByName(username).orElseThrow(() -> dataNotFound);
    }

    public Account editAccount(@NonNull String username ,@NonNull AccountUpdateRequestPacket packet)
            throws UsernameNotFoundException, IllegalArgumentException {

        var account = (Account) loadUserByUsername(username);

        if (!Strings.isNullOrEmpty(packet.getNickname())) account.setNickname(packet.getNickname());

        return accountRepository.save(account);
    }

    public Boolean isRefreshTokenUUIDValid(@NonNull UserDetails account, String uuid) {

        if (Strings.isNullOrEmpty(uuid)) return false;

        if (!((Account) account).getRefreshTokenUUIDs().contains(uuid)) return false;

        return true;
    }

    public void addNewRefreshTokenUUID(@NonNull Account account, @NonNull String uuid) {

        var uuids = account.getRefreshTokenUUIDs();

        if (uuids.size() >= Account.MAX_REFRESH_TOKENS) uuids.remove(0);
        uuids.add(uuid);

        accountRepository.save(account);
    }

    public void addAvatar(@NonNull String username, @NonNull Part part) throws IOException {

        Optional.ofNullable(part.getContentType())
                .filter(p -> p.startsWith("image/"))
                .orElseThrow(() -> new BadPacketFormatException("File type must be image"));

        var account = (Account)loadUserByUsername(username);
        if (part.getSize() >= 10_000_000) throw new IllegalFileException("Profile pic shouldn't larger than 10MB");

        var result = new EnumMap<Avatar.Size, String>(Avatar.Size.class);
        result.put(Avatar.Size.NORMAL, avatarRepository.save(createAvatars(username, part.getInputStream(), Avatar.Size.NORMAL)).getUuid());
        result.put(Avatar.Size.MEDIUM, avatarRepository.save(createAvatars(username, part.getInputStream(), Avatar.Size.MEDIUM)).getUuid());
        result.put(Avatar.Size.SMALL, avatarRepository.save(createAvatars(username, part.getInputStream(), Avatar.Size.SMALL)).getUuid());

        account.setAvatarIds(result);
        accountRepository.save(account);
    }

    public Avatar loadAvatarByUsername(@NonNull String username, Avatar.Size size) throws DataNotFoundException {
        return avatarRepository.findByFilename(username + "_" + (size == null ? Avatar.Size.NORMAL.name() : size.name()))
                .orElseThrow(() -> new DataNotFoundException(Avatar.class, "username", username));

    }

    public Avatar loadAvatarById(@NonNull String id) throws DataNotFoundException {
        return avatarRepository.findByUuid(id)
                .orElseThrow(() -> new DataNotFoundException(Avatar.class, "id", id));

    }

    private Optional<Account> getAccountByMail(@NonNull String mail) {
        return accountRepository.findAccountByMail(mail);
    }

    private Optional<Account> getAccountByName(@NonNull String name) {
        return accountRepository.findAccountByUsername(name);
    }

    private void validateUsername(String string) throws BadPacketFormatException {
        if (!USERNAME_PATTERN.matcher(string).find())
            throw new BadPacketFormatException("username does not match " + USERNAME_PATTERN.pattern());
    }

    private void validatePassword(String string) throws BadPacketFormatException {
        if (!PASSWORD_PATTERN.matcher(string).find())
            throw new BadPacketFormatException("password does not match " + PASSWORD_PATTERN.pattern());
    }

    private void validateMail(String string) throws BadPacketFormatException {
        if (!MAIL_PATTERN.matcher(string).find())
            throw new BadPacketFormatException("mail does not match " + MAIL_PATTERN.pattern());
    }

    private Avatar createAvatars(String username, InputStream inputStream, Avatar.Size targetSize) throws IOException {

        var image = ImageIO.read(inputStream);

        image = Scalr.resize(image, targetSize.getSize());

        return new Avatar(username + "_" + targetSize.name(), "image/jpeg", UUID.randomUUID().toString(), createCompressedImage(image, Avatar.Size.NORMAL));
    }

    /**
     * <a href="https://stackoverflow.com/a/37726626/18152420">Credit to...</a>
     */
    private byte[] createCompressedImage(BufferedImage image, Avatar.Size targetSize) throws IOException {

        var compressed = new ByteArrayOutputStream();

        try (var outputStream = new MemoryCacheImageOutputStream(compressed)) {

            var jpgWriter = ImageIO.getImageWritersByFormatName("JPEG").next();

            var jpgWriteParam = jpgWriter.getDefaultWriteParam();
            jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            jpgWriteParam.setCompressionQuality(0.7f);

            jpgWriter.setOutput(outputStream);
            jpgWriter.write(null, new IIOImage(image, null, null), jpgWriteParam);
            jpgWriter.dispose();
        }

        return compressed.toByteArray();
    }

}
