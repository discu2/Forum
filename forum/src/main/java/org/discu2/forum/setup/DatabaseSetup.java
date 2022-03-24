package org.discu2.forum.setup;

import lombok.AllArgsConstructor;
import org.discu2.forum.api.exception.AlreadyExistException;
import org.discu2.forum.api.exception.DataNotFoundException;
import org.discu2.forum.repository.BoardRepository;
import org.discu2.forum.service.BoardService;
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
