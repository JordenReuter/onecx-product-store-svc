package org.tkit.onecx.product.store.domain.daos;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.tkit.quarkus.jpa.exceptions.DAOException;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class ProductDAOTest {
    @Inject
    ProductDAO dao;

    @InjectMock
    EntityManager em;

    @BeforeEach
    void beforeAll() {
        Mockito.when(em.getCriteriaBuilder()).thenThrow(new RuntimeException("Test technical error exception"));
    }

    @Test
    void methodExceptionTests() {
        methodExceptionTests(() -> dao.findByProductNames(null),
                ProductDAO.ErrorKeys.ERROR_FIND_PRODUCT_BY_PRODUCT_NAMES);
        methodExceptionTests(() -> dao.findProductsByCriteria(null),
                ProductDAO.ErrorKeys.ERROR_FIND_PRODUCTS_BY_CRITERIA);
        methodExceptionTests(() -> dao.findProductByName("test"),
                ProductDAO.ErrorKeys.ERROR_FIND_PRODUCT_BY_NAME);
        methodExceptionTests(() -> dao.findAllProviders(),
                ProductDAO.ErrorKeys.ERROR_FIND_ALL_PROVIDERS);
    }

    void methodExceptionTests(Executable fn, Enum<?> key) {
        var exc = Assertions.assertThrows(DAOException.class, fn);
        Assertions.assertEquals(key, exc.key);
    }
}
