package org.discu2.forum.file;

import com.google.common.base.Strings;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.discu2.forum.common.exception.BadPacketFormatException;
import org.discu2.forum.common.exception.DataNotFoundException;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FileService {

    private final GridFsTemplate gridFsTemplate;


    public String saveFileForUser(InputStream inputStream, String username, String fileName, String fileType, String metaType, Integer sizeLimit)
            throws IOException {

        return fileWriter(inputStream, fileName, fileType, sizeLimit, new UserFileMetadata(metaType, username));
    }

    public String saveFileForBoard(InputStream inputStream, String username, String boardId, String fileName, String fileType, String metaType, Integer sizeLimit)
            throws IOException {

        return fileWriter(inputStream, fileName, fileType, sizeLimit, new BoardFileMetadata(metaType, username, boardId));
    }

    public GridFSFile loadFile(Query query) throws DataNotFoundException {

        var file = Optional.ofNullable(gridFsTemplate.findOne(query));

        return file.orElseThrow(() -> new DataNotFoundException(GridFSFile.class, "query", query.toString()));
    }

    public void deleteFile(Query query) {
        gridFsTemplate.delete(query);
    }

    public void writeGridFSFileToHttpServletResponse(GridFSFile file, HttpServletResponse response) throws IOException {

        var resource = gridFsTemplate.getResource(file);

        response.setContentType(resource.getContentType());
        resource.getInputStream().transferTo(response.getOutputStream());
    }

    private String fileWriter(InputStream inputStream, String fileName, String fileType, Integer sizeLimit, Object metadata)
            throws IOException {

        var bufferedStream = new BufferedInputStream(inputStream);
        var contentType = URLConnection.guessContentTypeFromStream(bufferedStream);

        if (Strings.isNullOrEmpty(contentType) || !contentType.startsWith(fileType) || inputStream.available() > sizeLimit)
            throw new BadPacketFormatException(String.format("Only allow %s <= %s byte(s)", fileType, sizeLimit));

        return gridFsTemplate.store(bufferedStream, fileName, contentType, metadata).toString();
    }

    @AllArgsConstructor
    @Data
    class UserFileMetadata {
        private String type;
        private String username;
    }

    @AllArgsConstructor
    @Data
    class BoardFileMetadata {
        private String type;
        private String username;
        private String boardId;
    }
}
