package com.huan;

/**
     * 访问操作函数定义
     * @param <E>
     */
public  abstract class Visitor<E>{
    //是否停止访问
    boolean stop;

    /**
     * 访问操作
     */
    abstract boolean visit(E element);
}