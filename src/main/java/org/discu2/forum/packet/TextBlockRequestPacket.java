package org.discu2.forum.packet;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public abstract class TextBlockRequestPacket {

    String boardGroupName;
    String boardName;
    String content;

    @NoArgsConstructor
    public static class Post extends TextBlockRequestPacket {

        @Getter
        @Setter
        String title;

    }

    public static class Reply extends TextBlockRequestPacket {

    }

    public static class Comment extends TextBlockRequestPacket {

    }

}
