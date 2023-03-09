package com.scb.tes.utils;

import java.util.List;

public interface FeedWriter<T> {
    String writeToCsv(List<T> trades);
}
