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
class SlotDAOTest {
    @Inject
    SlotDAO dao;

    @InjectMock
    EntityManager em;

    @BeforeEach
    void beforeAll() {
        Mockito.when(em.getCriteriaBuilder()).thenThrow(new RuntimeException("Test technical error exception"));
    }

    @Test
    void methodExceptionTests() {
        methodExceptionTests(() -> dao.findByProductNameAppId("test", "test", "test"),
                SlotDAO.ErrorKeys.ERROR_FIND_SLOT_PRODUCT_NAME_APP_ID_NAME);
        methodExceptionTests(() -> dao.findByCriteria(null),
                SlotDAO.ErrorKeys.ERROR_FIND_SLOTS_BY_CRITERIA);
        methodExceptionTests(() -> dao.loadByCriteria(null),
                SlotDAO.ErrorKeys.ERROR_FIND_SLOTS_BY_CRITERIA);
        methodExceptionTests(() -> dao.deleteByProductName(null),
                SlotDAO.ErrorKeys.ERROR_DELETE_BY_PRODUCT_NAME);
        methodExceptionTests(() -> dao.updateByProductName(null, null),
                SlotDAO.ErrorKeys.ERROR_UPDATE_BY_PRODUCT_NAME);
    }

    void methodExceptionTests(Executable fn, Enum<?> key) {
        var exc = Assertions.assertThrows(DAOException.class, fn);
        Assertions.assertEquals(key, exc.key);
    }
}
