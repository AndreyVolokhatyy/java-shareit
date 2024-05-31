package ru.practicum.shareit.pagination;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.heandler.exception.PaginationException;
import ru.practicum.shareit.request.page.PageRequestManager;

public class PaginationTest {

    @Test
    void paginationTest() {
        Assertions.assertThrows(PaginationException.class, () -> PageRequestManager.form(-1,1, null, null));
        Assertions.assertThrows(PaginationException.class, () -> PageRequestManager.form(1,0, null, null));
    }
}
