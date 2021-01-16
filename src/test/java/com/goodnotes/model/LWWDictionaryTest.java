package com.goodnotes.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class LWWDictionaryTest {
    private LWWDictionary<Integer, Integer> dictionary;
    @BeforeEach
    public void initialize(){
        dictionary = new LWWDictionary();
    }

    @Test
    public void shouldAddElementToDictionary() {
        dictionary.add(1, 5, LocalDateTime.now());

        Assertions.assertEquals(1, dictionary.size());
        Assertions.assertEquals(5, dictionary.lookup(1));
    }

    @Test
    public void shouldLookupKeyAndReturnValue(){
        dictionary.add(1, 20, LocalDateTime.now());

        Assertions.assertEquals(dictionary.lookup(1), 20);
    }


}
