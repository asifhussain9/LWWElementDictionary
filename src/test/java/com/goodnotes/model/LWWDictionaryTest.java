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
    public void shouldAddNullElementToDictionary() {
        dictionary.add(null, 5, LocalDateTime.now());

        Assertions.assertEquals(1, dictionary.size());
        Assertions.assertEquals(5, dictionary.lookup(null));
    }

    @Test
    public void shouldLookupKeyAndReturnValue(){
        dictionary.add(1, 20, LocalDateTime.now());

        Assertions.assertEquals(dictionary.lookup(1), 20);
    }

    @Test
    public void shouldLookupAndReturnNullForInvalidKey(){
        dictionary.add(1, 20, LocalDateTime.now());

        Assertions.assertEquals(dictionary.lookup(2), null);
    }

    @Test
    public void shouldRemoveElementFromDictionary(){
        dictionary.add(1, 20);
        dictionary.add(2, 30, LocalDateTime.now());

        dictionary.remove(2);

        Assertions.assertNull(dictionary.lookup(2));
    }

    @Test
    public void shouldNotRemoveIfElementRemovedBeforeAdd(){
        dictionary.remove(2, LocalDateTime.now().minusMinutes(1));

        dictionary.add(1, 20);
        dictionary.add(2, 30, LocalDateTime.now());

        Assertions.assertNotNull(dictionary.lookup(2));
    }

    @Test
    public void shouldPreferRemoveIfRemoveAndAddTimestampsAreSame(){
        LocalDateTime timestamp = LocalDateTime.now();
        dictionary.remove(2, timestamp);

        dictionary.add(1, 20);
        dictionary.add(2, 30, timestamp);

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

    @Test
    public void shouldUpdateKeyValue(){
        dictionary.add(1, 20);
        dictionary.add(2, 30, LocalDateTime.now());

        dictionary.update(1, 3);

        Assertions.assertNull(dictionary.lookup(1));
        Assertions.assertEquals(dictionary.lookup(3), 20);
    }

    @Test
    public void shouldAddLatestKeyValue(){
        dictionary.add(1, 30, LocalDateTime.now());
        dictionary.add(1, 20, LocalDateTime.now().minusMinutes(1));

        Assertions.assertEquals(30, dictionary.lookup(1));
    }

    @Test
    public void shouldNotRemoveIfRemoveOperationOlderThanAdd(){
        dictionary.add(1, 20, LocalDateTime.now());
        dictionary.remove(1, LocalDateTime.now().minusMinutes(1));

        Assertions.assertEquals(20, dictionary.lookup(1));
    }

    @Test
    public void shouldNotUpdateIfUpdateOperationIsOld(){
        dictionary.add(1, 20);
        dictionary.update(1, 2, LocalDateTime.now().minusSeconds(1));

        Assertions.assertEquals(20, dictionary.lookup(1));
    }
}
