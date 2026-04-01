package org.toolkit4j.data.model.page;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PageRequestTest {

  @Test
  void getPage_returnsRawPageNumber() {
    var request = new PageRequest();
    request.setPage(2);
    request.setSize(20);

    assertEquals(2, request.getPage());
  }

  @Test
  void getOffset_computesDatabaseOffsetFromPageAndSize() {
    var request = new PageRequest();
    request.setPage(3);
    request.setSize(20);

    assertEquals(40, request.getOffset());
  }

  @Test
  void getOffset_normalizesInvalidPageAndSize() {
    var request = new PageRequest();
    request.setPage(0);
    request.setSize(0);

    assertEquals(0, request.getOffset());
  }
}
