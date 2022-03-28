package org.discu2.forum.core.setup;

import lombok.AllArgsConstructor;
import org.discu2.forum.common.exception.AlreadyExistException;
import org.discu2.forum.common.exception.DataNotFoundException;
import org.discu2.forum.core.repository.BoardRepository;
import org.discu2.forum.core.service.BoardService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DatabaseSetup {

    private final BoardService boardService;
    private final BoardRepository boardRepository;


    @Bean
    public void initDefaults() throws AlreadyExistException, DataNotFoundException {

        if (boardRepository.findAll().isEmpty())
            boardService.createNewBoard("Default Group", "Default Board");

    }
}
