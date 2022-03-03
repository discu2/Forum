package org.discu2.forum.service;

import lombok.AllArgsConstructor;
import org.discu2.forum.repository.BoardRepository;
import org.discu2.forum.repository.PostRepository;
import org.discu2.forum.repository.TopicRepository;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
@AllArgsConstructor
public class ForumPermissionService implements PermissionEvaluator {

    private final PostRepository postRepository;
    private final TopicRepository topicRepository;
    private final BoardRepository boardRepository;

    /**
     * @param authentication
     * @param targetDomainObject - 目標物件類型
     * @param permission         - 權限類型
     * @return
     */
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if ((authentication == null) || (targetDomainObject == null) || !(permission instanceof String)) return false;

        if (authentication.getAuthorities().contains("ADMIN")) return true;

        return hasPermission(authentication, targetDomainObject.toString(), targetDomainObject.getClass().toString(), permission);
    }

    /**
     * @param authentication
     * @param targetId       - 名稱
     * @param targetType     -
     * @param permission
     * @return
     */
    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if ((authentication == null) || (targetId == null) || (targetType == null) || !(permission instanceof String))
            return false;

        if (authentication.getAuthorities().contains("ADMIN")) return true;

        switch (targetType) {

            case "Board" -> {
                return hasBoardPermission(authentication, (String) targetId, (String) permission);
            }

            case "Comment" -> {
                return hasCommentPermission(authentication, (String) targetId, (String) permission);
            }

            default -> {
                return false;
            }
        }

    }

    private boolean hasBoardPermission(Authentication auth, String boardId, String permission) {

        var board = boardRepository.findById(boardId).get();

        if (board == null) return false;

        for (var a : auth.getAuthorities())
            if (board.getPermissions().get(permission).contains(a.getAuthority())) return true;

        return false;
    }

    private boolean hasCommentPermission(Authentication auth, String masterId, String permission) {

        var post = postRepository.findById(masterId);

        if (post == null) return false;

        var topic = topicRepository.findById(post.get().getTopicId());

        if (topic == null) return false;

        return hasBoardPermission(auth, topic.get().getBoardId(), permission);

    }
}