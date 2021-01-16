package com.goodnotes.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class LWWDictionaryTest {
    @Test
    public void shouldAddElementToDictionary() {
        LWWDictionary dictionary = new LWWDictionary();
        dictionary.add(1, 5, LocalDateTime.now());

        Assertions.assertEquals(1, dictionary.size());
        Assertions.assertEquals(5, dictionary.lookup(1));
    }
}
