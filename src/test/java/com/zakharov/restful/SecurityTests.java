package com.zakharov.restful;

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
@AutoConfigureMockMvc()
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SecurityTests {

    @Autowired
    private PersonRepository repository;
    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    @BeforeAll
    public void resetDB() {
        repository.deleteAll();
    }

    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    @Test
    public void getAllPeopleWithAuthorization() throws Exception {
        createTestPerson("Иван", "Иванов", 20, 1.8f, new Date(), false);
        createTestPerson("Петр", "Петров", 10, 1.7f, new Date(), true);
        mockMvc.perform(
                get("/people"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getAllPeopleWithoutAuthorization() throws Exception {
        createTestPerson("Иван", "Иванов", 20, 1.8f, new Date(), false);
        createTestPerson("Петр", "Петров", 10, 1.7f, new Date(), true);
        mockMvc.perform(
                get("/people"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    @Test
    public void getOnePersonWithAuthorization() throws Exception {
        int id = createTestPerson("Иван", "Иванов", 20, 1.8f, new Date(), false).getId();

        mockMvc.perform(
                get("/people/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getOnePersonWithoutAuthorization() throws Exception {
        int id = createTestPerson("Иван", "Иванов", 20, 1.8f, new Date(), false).getId();

        mockMvc.perform(
                get("/people/{id}", id))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username="admin",roles={"ADMIN"})
    @Test
    public void createPersonWithAuthorization() throws Exception {
        mockMvc.perform(
                post("/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {"name":"Иван","surname":"Иванов","age":20,"growth":1.8,"birth_date":"2001-01-01","married":false}
                        """))
                .andExpect(status().isCreated());
    }

    @Test
    public void createPersonWithoutAuthorization() throws Exception {
        mockMvc.perform(
                post("/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {"name":"Иван","surname":"Иванов","age":20,"growth":1.8,"birth_date":"2001-01-01","married":false}
                        """))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser("USER")
    @Test
    public void createPersonWithRoleUser() throws Exception {
        mockMvc.perform(
                post("/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {"name":"Иван","surname":"Иванов","age":20,"growth":1.8,"birth_date":"2001-01-01","married":false}
                        """))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username="admin",roles={"ADMIN"})
    @Test
    public void updatePersonWithAuthorization() throws Exception {
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
    public void updatePersonWithoutAuthorization() throws Exception {
        int id = createTestPerson("Иван", "Иванов", 20, 1.8f, new Date(), false).getId();

        mockMvc.perform(
                put("/people/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {"name":"Петр","surname":"Петров","age":10,"growth":1.7,"birth_date":"2001-01-01","married":true}
                        """))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser("USER")
    @Test
    public void updatePersonWithRoleUser() throws Exception {
        int id = createTestPerson("Иван", "Иванов", 20, 1.8f, new Date(), false).getId();

        mockMvc.perform(
                put("/people/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {"name":"Петр","surname":"Петров","age":10,"growth":1.7,"birth_date":"2001-01-01","married":true}
                        """))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username="admin",roles={"ADMIN"})
    @Test
    public void deletePersonWithAuthorization() throws Exception {
        int id = createTestPerson("Иван", "Иванов", 20, 1.8f, new Date(), false).getId();

        mockMvc.perform(
                delete("/people/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    public void deletePersonWithoutAuthorization() throws Exception {
        int id = createTestPerson("Иван", "Иванов", 20, 1.8f, new Date(), false).getId();

        mockMvc.perform(
                delete("/people/{id}", id))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser("USER")
    @Test
    public void deletePersonWithRoleUser() throws Exception {
        int id = createTestPerson("Иван", "Иванов", 20, 1.8f, new Date(), false).getId();

        mockMvc.perform(
                delete("/people/{id}", id))
                .andExpect(status().isForbidden());
    }

    private Person createTestPerson(String name, String surname, int age, float growth, Date birth_date, boolean married) {
        Person person = new Person(name, surname, age, growth, birth_date, married);
        return repository.save(person);
    }
}
