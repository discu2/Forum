package org.discu2.forum.service;

import lombok.AllArgsConstructor;
import org.discu2.forum.repository.*;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@AllArgsConstructor
public class ForumPermissionService implements PermissionEvaluator {

    private final PostRepository postRepository;
    private final TopicRepository topicRepository;
    private final BoardRepository boardRepository;
    private final RoleRepository roleRepository;
    private final CommentRepository commentRepository;


    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if ((authentication == null) || (targetDomainObject == null) || !(permission instanceof String)) return false;

        if (authentication.getAuthorities().contains("ADMIN")) return true;

        return hasPermission(authentication, targetDomainObject.toString(), targetDomainObject.getClass().toString(), permission);
    }

    /**
     * @param targetType - A String of the model class name
     */
    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if ((authentication == null) || (targetId == null) || (targetType == null) || !(permission instanceof String))
            return false;

        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) return true;

        switch (targetType) {

            case "Board" -> {
                return hasBoardPermission(authentication, (String) targetId, (String) permission);
            }

            case "Topic" -> {
                return hasTopicPermission(authentication, (String) targetId, (String) permission);
            }

            case "Post" -> {
                return hasPostPermission(authentication, (String) targetId, (String) permission);
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

        var board = boardRepository.findById(boardId);
        var result = new AtomicBoolean(false);

        if (board.isEmpty()) return false;

        for (var a : auth.getAuthorities()) {
            var role = roleRepository.findByName(a.getAuthority());
            role.ifPresent(r -> result.set(board.get().getPermissions().get(permission).contains(r.getId())));
        }

        return result.get();
    }

    private boolean hasTopicPermission(Authentication auth, String topicId, String permission) {

        var topic = topicRepository.findById(topicId);

        if (topic.isEmpty()) return false;

        return hasBoardPermission(auth, topic.get().getBoardId(), permission);
    }

    private boolean hasPostPermission(Authentication auth, String postId, String permission) {

        var post = postRepository.findById(postId);

        if (post.isEmpty()) return false;

        return hasTopicPermission(auth, post.get().getTopicId(), permission);
    }

    private boolean hasCommentPermission(Authentication auth, String commentId, String permission) {

        var comment = commentRepository.findById(commentId);

        if (comment.isEmpty()) return false;

        return hasPostPermission(auth, comment.get().getPostId(), permission);
    }
}
