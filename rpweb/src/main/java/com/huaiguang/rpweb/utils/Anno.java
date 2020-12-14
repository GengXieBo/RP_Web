package com.huaiguang.rpweb.utils;

import com.sun.jna.Structure;

@Structure.FieldOrder({"id", "x", "y", "type", "score"})
public class Anno extends Structure {
    public int id;
    public int x;
    public int y;
    public int type;
    public double score;

    public static class ByReference extends Anno implements Structure.ByReference{

    }
    public static class ByValue extends Anno implements Structure.ByValue{

    }

}
