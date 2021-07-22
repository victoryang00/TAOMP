package com.github.mottox.taomp.concurrent;

import com.github.mottox.taomp.sort.SkipList;
import org.junit.Test;

public class SkipListTest {
    @Test
    public void testAdd() throws Exception {
        SkipList<String> skipList = new SkipList<>();
        for (int i = 1; i < 10; i++) {
            skipList.add(i, String.valueOf(i));
        }
        assert skipList.size() == 9;
        assert !skipList.empty();
        assert !skipList.contains(11);
        assert skipList.get(5).equals("5");
    }

    @Test
    public void testRemove() throws Exception {
        SkipList<String> skipList = new SkipList<>();
        for (int i = 1; i < 10; i++) {
            skipList.add(i, String.valueOf(i));
        }
        assert skipList.size() == 9;
        assert skipList.get(9) == null;
        skipList.remove(8);
        skipList.remove(7);
        skipList.remove(6);
        skipList.remove(5);
        skipList.remove(4);
        skipList.remove(3);
        skipList.remove(2);
        skipList.remove(1);
        assert skipList.size() == 0;
        assert skipList.empty();
    }
}
