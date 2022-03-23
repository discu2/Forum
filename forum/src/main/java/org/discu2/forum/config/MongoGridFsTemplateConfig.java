package org.discu2.forum.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

@Configuration
@AllArgsConstructor
public class MongoGridFsTemplateConfig {

    private MappingMongoConverter mongoConverter;
    private MongoDatabaseFactory mongoDatabaseFactory;

    @Bean
    public GridFsTemplate gridFsTemplate() {
        return new GridFsTemplate(mongoDatabaseFactory, mongoConverter);
    }
}
