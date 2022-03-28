package org.discu2.forum.common.packet;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class TextBlockRequestPacket {

    private String content;

    @NoArgsConstructor
    public static class Post extends TextBlockRequestPacket {

        @Getter
        @Setter
        String title;

    }

}
