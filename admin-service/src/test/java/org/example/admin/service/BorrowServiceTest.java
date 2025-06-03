package org.example.admin.service;

import org.example.admin.pojo.query.PageQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class BorrowServiceTest {

    @Autowired
    private IBorrowService borrowService;
    @Test
    void testBorrowRegister() {
        PageQuery pageQuery = new PageQuery();
        pageQuery.setCurrent(1L);
        pageQuery.setSize(10L);

        assertFalse(borrowService.pageQuery(pageQuery).getRecords().isEmpty());
    }
}