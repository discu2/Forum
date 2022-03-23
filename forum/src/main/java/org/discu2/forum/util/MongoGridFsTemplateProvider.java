package org.discu2.forum.util;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MongoGridFsTemplateProvider {

    private MappingMongoConverter mongoConverter;
    private MongoDatabaseFactory mongoDatabaseFactory;

    @Bean
    public GridFsTemplate gridFsTemplate() {
        return new GridFsTemplate(mongoDatabaseFactory, mongoConverter);
    }
}
