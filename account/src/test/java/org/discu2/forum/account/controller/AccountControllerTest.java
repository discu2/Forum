package org.discu2.forum.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.discu2.forum.api.packet.RegisterRequestPacket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper mapper;
    private ObjectWriter writer;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        writer = mapper.writer().withDefaultPrettyPrinter();
    }

    @Test
    void registerAccount() throws Exception {
        var uri = "/account/register";

        var good = new RegisterRequestPacket();
        var badMail = new RegisterRequestPacket();
        var badPass = new RegisterRequestPacket();
        var badName = new RegisterRequestPacket();

        good.setUsername("goodName1");
        good.setPassword("agoodPass1");
        good.setMail("goodmail@mail.test");

        badMail.setUsername(good.getUsername());
        badMail.setPassword(good.getPassword());
        badMail.setMail("sdd@fsf");

        badPass.setUsername(good.getUsername());
        badPass.setPassword("1111");
        badPass.setMail(good.getMail());

        badName.setUsername("bad");
        badName.setPassword(good.getPassword());
        badName.setMail(good.getMail());

        mockMvc.perform(post(uri).accept(MediaType.APPLICATION_JSON).content(writer.writeValueAsString(good))).andExpect(status().isOk());
        mockMvc.perform(post(uri).accept(MediaType.APPLICATION_JSON).content(writer.writeValueAsString(badMail))).andExpect(status().isBadRequest());
        mockMvc.perform(post(uri).accept(MediaType.APPLICATION_JSON).content(writer.writeValueAsString(badName))).andExpect(status().isBadRequest());
        mockMvc.perform(post(uri).accept(MediaType.APPLICATION_JSON).content(writer.writeValueAsString(badPass))).andExpect(status().isBadRequest());
    }

    @Test
    void refreshToken() {
    }
}