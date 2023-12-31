package exercise;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.PersistenceException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import exercise.config.TestPersistenceConfig;
import exercise.model.Employee;
import exercise.repository.EmployeeRepository;

@SpringJUnitConfig(TestPersistenceConfig.class)
@TestConstructor(autowireMode = AutowireMode.ALL)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class EmployeeRepositoryEntityLifeCycleTest {

    private final EmployeeRepository employeeRepository;

    EmployeeRepositoryEntityLifeCycleTest( EmployeeRepository employeeRepository ) {
        this.employeeRepository = employeeRepository;
    }

    @Test
    void test_findAll() {
        var employees = employeeRepository.findAll();
        assertThat(employees.size()).isEqualTo(11);
    }

    @Test
    void test_create_and_findAll() {
        var cnt = employeeRepository.count();

        var employee = new Employee("Sidney");
        employeeRepository.persist(employee);

        var employees = employeeRepository.findAll();
        assertThat(employees.size()).isEqualTo(cnt + 1);
    }

    // -------------------------------------------------------------------------
    // Hibernate: save, persist, update, merge, saveOrUpdate
    // https://www.baeldung.com/hibernate-save-persist-update-merge-saveorupdate
    // -------------------------------------------------------------------------

    @Test
    public void whenPersistTransient_thenSavedToDatabaseOnCommit() {

        Employee employee = new Employee("John");
        employeeRepository.persist(employee);

        assertNotNull(employeeRepository.find(employee.getId()));

    }

    @Test
    public void whenPersistTransient_thenIdGeneratedImmediately() {

        Employee employee = new Employee("John");
        assertNull(employee.getId());

        employeeRepository.persist(employee);
        Long id = employee.getId();
        assertNotNull(id);

        assertNotNull(employeeRepository.find(employee.getId()));
    }

    @Test
    @Transactional
    public void whenPersistPersistent_thenNothingHappens() {

        Employee employee = new Employee("John");

        employeeRepository.persist(employee);
        Long id1 = employee.getId();

        employeeRepository.persist(employee);
        Long id2 = employee.getId();

        assertEquals(id1, id2);
    }

    @Test
    public void whenPersistDetached_thenThrowsException() {

        Employee employee = new Employee("John");
        employeeRepository.persist(employee);

        Throwable exception = assertThrows(PersistenceException.class, () -> {
            employeeRepository.persist(employee);
        });

        assertEquals(exception.getMessage(),
                "Converting `org.hibernate.PersistentObjectException` to JPA `PersistenceException` : " +
                "detached entity passed to persist: exercise.model.Employee");
    }

    @Test
    public void whenMergeDetached_thenEntityUpdatedFromDatabase() {

        Employee employee = new Employee("John");
        employeeRepository.persist(employee);

        employee.setName("Mary");
        Employee mergedEmployee = employeeRepository.merge(employee);

        assertNotSame(employee, mergedEmployee);
        assertEquals("Mary", mergedEmployee.getName());

    }

    @Test
    public void whenMergeTransient_thenNewEntitySavedToDatabase() {

        Employee employee = new Employee("John");
        Employee mergedEmployee = employeeRepository.merge(employee);

        assertNull(employee.getId());
        assertNotNull(mergedEmployee.getId());

    }

    @Test
    public void whenMergeDetached_thenReturnsEqualObject() {

        Employee employee = new Employee("John");
        employeeRepository.persist(employee);

        Employee mergedEmployee = employeeRepository.merge(employee);

        //assertSame(employee, mergedEmployee);
        assertEquals(employee, mergedEmployee);

    }

}

