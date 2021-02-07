package com.zakharov.restful;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zakharov.restful.model.Person;
import com.zakharov.restful.repository.PersonRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureMockMvc()
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SecurityTests {

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

    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    @Test
    @Order(1)
    public void getAllPeopleWithAuthorization() throws Exception {
        Person person1 = createTestPerson("Иван", "Иванов", 1.8f, LocalDate.now(), false);
        Person person2 = createTestPerson("Петр", "Петров",  1.7f, LocalDate.now(), true);
        mockMvc.perform(
                get("/people"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(person1, person2))));
    }

    @Test
    public void getAllPeopleWithoutAuthorization() throws Exception {
        createTestPerson("Иван", "Иванов", 1.8f, LocalDate.now(), false);
        createTestPerson("Петр", "Петров", 1.7f, LocalDate.now(), true);
        mockMvc.perform(
                get("/people"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    @Test
    public void getOnePersonWithAuthorization() throws Exception {
        Person person = createTestPerson("Иван", "Иванов", 1.8f, LocalDate.now(), false);

        mockMvc.perform(
                get("/people/{id}", person.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(person)));
    }

    @Test
    public void getOnePersonWithoutAuthorization() throws Exception {
        int id = createTestPerson("Иван", "Иванов", 1.8f, LocalDate.now(), false).getId();

        mockMvc.perform(
                get("/people/{id}", id))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username="admin",roles={"ADMIN"})
    @Test
    public void createPersonWithAuthorization() throws Exception {
        Person person = new Person("Иван", "Иванов", 1.8f, LocalDate.of(2001, 1, 1), false);
        mockMvc.perform(
                post("/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isCreated());
    }

    @Test
    public void createPersonWithoutAuthorization() throws Exception {
        Person person = new Person("Иван", "Иванов", 1.8f, LocalDate.of(2001, 1, 1), false);
        mockMvc.perform(
                post("/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser("USER")
    @Test
    public void createPersonWithRoleUser() throws Exception {
        Person person = new Person("Иван", "Иванов", 1.8f, LocalDate.of(2001, 1, 1), false);
        mockMvc.perform(
                post("/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username="admin",roles={"ADMIN"})
    @Test
    public void updatePersonWithAuthorization() throws Exception {
        int id = createTestPerson("Иван", "Иванов", 1.8f, LocalDate.now(), false).getId();
        Person person = new Person("Петр", "Петров", 1.7f, LocalDate.of(2001, 1, 1), true);
        mockMvc.perform(
                put("/people/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isOk());
    }

    @Test
    public void updatePersonWithoutAuthorization() throws Exception {
        int id = createTestPerson("Иван", "Иванов", 1.8f, LocalDate.now(), false).getId();
        Person person = new Person("Петр", "Петров", 1.7f, LocalDate.of(2001, 1, 1), true);
        mockMvc.perform(
                put("/people/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser("USER")
    @Test
    public void updatePersonWithRoleUser() throws Exception {
        int id = createTestPerson("Иван", "Иванов", 1.8f, LocalDate.now(), false).getId();
        Person person = new Person("Петр", "Петров", 1.7f, LocalDate.of(2001, 1, 1), true);
        mockMvc.perform(
                put("/people/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username="admin",roles={"ADMIN"})
    @Test
    public void deletePersonWithAuthorization() throws Exception {
        int id = createTestPerson("Иван", "Иванов", 1.8f, LocalDate.now(), false).getId();

        mockMvc.perform(
                delete("/people/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    public void deletePersonWithoutAuthorization() throws Exception {
        int id = createTestPerson("Иван", "Иванов", 1.8f, LocalDate.now(), false).getId();

        mockMvc.perform(
                delete("/people/{id}", id))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser("USER")
    @Test
    public void deletePersonWithRoleUser() throws Exception {
        int id = createTestPerson("Иван", "Иванов", 1.8f, LocalDate.now(), false).getId();

        mockMvc.perform(
                delete("/people/{id}", id))
                .andExpect(status().isForbidden());
    }

    private Person createTestPerson(String name, String surname, float growth, LocalDate birth_date, boolean married) {
        Person person = new Person(name, surname, growth, birth_date, married);
        return repository.save(person);
    }
}
