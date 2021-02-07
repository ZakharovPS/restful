package com.zakharov.restful;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zakharov.restful.model.Person;
import com.zakharov.restful.repository.PersonRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureMockMvc()
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WithMockUser(username="admin",roles={"ADMIN"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RestfulApplicationTests {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PersonRepository repository;
    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    public void resetDB() {
        repository.deleteAll();
    }

    @Test
    @Order(1)
    public void getAllPeopleFromEmptyDB() throws Exception {

        mockMvc.perform(
                get("/people"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(2)
    public void getOneNoExistingPerson() throws Exception {

        mockMvc.perform(
                get("/people/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(3)
    public void updateNoExistingPerson() throws Exception {
        Person person = new Person("Петр", "Петров", 1.7f, LocalDate.of(2001, 1, 1), true);
        mockMvc.perform(
                put("/people/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isNotFound());

    }

    @Test
    @Order(4)
    public void deleteNoExistingPerson() throws Exception {

        mockMvc.perform(
                delete("/people/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(5)
    public void getAllPeopleFromNoEmptyDB() throws Exception {
        Person person1 = createTestPerson("Иван", "Иванов", 1.8f, LocalDate.now(), false);
        Person person2 = createTestPerson("Петр", "Петров", 1.7f, LocalDate.now(), true);
        mockMvc.perform(
                get("/people"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(person1, person2))));
    }

    @Test
    public void getOneExistingPerson() throws Exception {

        Person person = createTestPerson("Петр", "Петров", 1.7f, LocalDate.now(), true);

        mockMvc.perform(
                get("/people/{id}", person.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(person)));
    }

    @Test
    public void createPerson() throws Exception {
        Person person = new Person("Иван", "Иванов", 1.8f, LocalDate.of(2001, 1, 1), false);
        mockMvc.perform(
                post("/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateExistingPerson() throws Exception {

        int id = createTestPerson("Иван", "Иванов", 1.8f, LocalDate.now(), false).getId();
        Person person = new Person("Петр", "Петров", 1.7f, LocalDate.of(2001, 1, 1), true);

        mockMvc.perform(
                put("/people/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isOk());

    }

    @Test
    public void deleteExistingPerson() throws Exception {

        int id = createTestPerson("Иван", "Иванов", 1.8f, LocalDate.now(), false).getId();

        mockMvc.perform(
                delete("/people/{id}", id))
                .andExpect(status().isOk());
    }

    private Person createTestPerson(String name, String surname, float growth, LocalDate birth_date, boolean married) {
        Person person = new Person(name, surname, growth, birth_date, married);
        return repository.save(person);
    }
}
