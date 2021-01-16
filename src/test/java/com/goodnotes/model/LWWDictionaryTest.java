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

    @Test
    public void shouldRemoveElementFromDictionary(){
        dictionary.add(1, 20);
        dictionary.add(2, 30, LocalDateTime.now());

        dictionary.remove(2);

        Assertions.assertNull(dictionary.lookup(2));
    }

    @Test
    public void shouldMergeAddMapAndRemoveMap(){
        dictionary.add(1, 20);
        dictionary.add(2, 30, LocalDateTime.now());
        dictionary.add(3, 30, LocalDateTime.now());

        dictionary.remove(2, LocalDateTime.now());
        dictionary.remove(1);

        Assertions.assertEquals(dictionary.size(), 1);
        Assertions.assertEquals(dictionary.lookup(3), 30);
    }

}
