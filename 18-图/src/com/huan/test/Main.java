package com.huan.test;

import com.huan.graph.Graph;
import com.huan.graph.ListGraph;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        Graph<String,Integer> graph = new ListGraph<>();
        graph.addEdge("v1","v2",3);
        graph.addEdge("v1","v0",9);
        graph.addEdge("v2","v0",2);
        graph.addEdge("v2","v3",5);
        graph.addEdge("v3","v4",1);
        graph.addEdge("v0","v4",6);
        graph.print();
        graph.removeVertex("v0");
        System.out.println("==========================");
        System.out.println("==========================");
        System.out.println("==========================");
        System.out.println("删除后");
        graph.print();
    }
}
