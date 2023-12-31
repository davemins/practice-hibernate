package exercise;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import exercise.config.TestPersistenceConfig;
import exercise.model.Department;
import exercise.repository.DepartmentRepository;

// Dependency Injection with SpringExtension
// https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html#testcontext-junit-jupiter-di

// Constructor injection for test classes must not be used
//    in conjunction with JUnit Jupiter’s @TestInstance(PER_CLASS) support
//    if @DirtiesContext is used to close the test’s ApplicationContext
//    before or after test methods.

@SpringJUnitConfig(TestPersistenceConfig.class)
@TestConstructor(autowireMode = AutowireMode.ALL)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

@lombok.extern.slf4j.Slf4j
public class DepartmentRepositoryTest {

    private final DepartmentRepository departmentRepository;

    DepartmentRepositoryTest( DepartmentRepository departmentRepository ) {
        this.departmentRepository = departmentRepository;
    }

    @Test
    void test_count() {
        var cnt = departmentRepository.count();

        assertThat(cnt).isEqualTo(4L);
    }

    @Test
    void test_findAll() {
        var cnt = departmentRepository.count();

        var departments = departmentRepository.findAll();
        assertThat(departments.size()).isEqualTo(cnt);

        assertThat(departments).extracting("id").contains(1L, 2L, 3L, 4L);
        assertThat(departments).extracting("name").contains("Development", "Management", "Marketing", "Personnel");
    }

    @Test
    void test_findById() {
        var department = departmentRepository.findById(1L);

        assertThat(department.getName()).isEqualTo("Development");
    }

}

