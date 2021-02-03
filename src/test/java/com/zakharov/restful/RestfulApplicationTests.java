package com.zakharov.restful;

import com.zakharov.restful.exception.EmptyDBException;
import com.zakharov.restful.exception.EntityNotFoundException;
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
import java.util.Date;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RestfulApplicationTests {

    @Autowired
    private PersonRepository repository;
    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    @BeforeAll
    public void resetDB() {
        repository.deleteAll();
    }

    @Test
    public void getAllPeopleFromEmptyDB() throws Exception {

        mockMvc.perform(
                get("/people"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAllPeopleFromNoEmptyDB() throws Exception {
        createTestPerson("Иван", "Иванов", 20, 1.8f, new Date(), false);
        createTestPerson("Петр", "Петров", 10, 1.7f, new Date(), true);
        mockMvc.perform(
                get("/people"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void createPerson() throws Exception {
        mockMvc.perform(
                post("/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {"name":"Иван","surname":"Иванов","age":20,"growth":1.8,"birth_date":"2001-01-01","married":false}
                        """))
                .andExpect(status().isCreated());
    }

    @Test
    public void requestWithNullFieldInJSON() throws Exception {
        mockMvc.perform(
                post("/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {"surname":"Иванов","age":20,"growth":1.8,"birth_date":"2001-01-01","married":false}
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void requestWithMalformedJSON() throws Exception {
        mockMvc.perform(
                post("/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {"name":"Иван","surname":"Иванов","age":"двадцать","growth":1.8,"birth_date":"2001-01-01","married":false}
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void requestWithIncorrectArgument() throws Exception {
        mockMvc.perform(
                get("/people/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getOneExistingPerson() throws Exception {

        int id = createTestPerson("Иван", "Иванов", 20, 1.8f, new Date(), false).getId();

        mockMvc.perform(
                get("/people/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getOneNoExistingPerson() throws Exception {

        mockMvc.perform(
                get("/people/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateExistingPerson() throws Exception {

        int id = createTestPerson("Иван", "Иванов", 20, 1.8f, new Date(), false).getId();

        mockMvc.perform(
                put("/people/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {"name":"Петр","surname":"Петров","age":10,"growth":1.7,"birth_date":"2001-01-01","married":true}
                        """))
                .andExpect(status().isOk());

    }

    @Test
    public void updateNoExistingPerson() throws Exception {

        mockMvc.perform(
                put("/people/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {"name":"Петр","surname":"Петров","age":10,"growth":1.7,"birth_date":"2001-01-01","married":true}
                        """))
                .andExpect(status().isNotFound());

    }

    @Test
    public void deleteExistingPerson() throws Exception {

        int id = createTestPerson("Иван", "Иванов", 20, 1.8f, new Date(), false).getId();

        mockMvc.perform(
                delete("/people/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteNoExistingPerson() throws Exception {

        mockMvc.perform(
                delete("/people/1"))
                .andExpect(status().isNotFound());
    }

    private Person createTestPerson(String name, String surname, int age, float growth, Date birth_date, boolean married) {
        Person person = new Person(name, surname, age, growth, birth_date, married);
        return repository.save(person);
    }
}
