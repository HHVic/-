package com.huan.graph;

import java.util.*;

/**
 * 使用邻接表实现图
 *
 * @param <V>
 * @param <E>
 */
public class ListGraph<V, E> implements Graph<V, E> {
    // 存放所有的顶点的映射
    Map<V, Vertex<V, E>> vertices = new HashMap<>();
    // 存放所有的边
    Set<Edge<V,E>> edges = new HashSet<>();

    @Override
    public void print() {
        // 遍历所有顶点打印
        vertices.forEach((v,vertex) -> {
            System.out.println(v);
            System.out.println("--------in----------");
            System.out.println(vertex.inEdges);
            System.out.println("--------out----------");
            System.out.println(vertex.outEdges);
        });
        // 遍历所有边打印
        edges.forEach(System.out::println);
    }

    @Override
    public void bfs(V start, Visitor<V> visitor) {
        Vertex<V, E> vertex = vertices.get(start);
        if(vertex == null || visitor == null) return ;
        HashSet<Vertex<V, E>> visited = new HashSet<>();
        Queue<Vertex<V,E>> queue = new LinkedList<>();
        queue.offer(vertex);
        visited.add(vertex);
        while(!queue.isEmpty()){
            vertex = queue.poll();
            if(visitor.visitor(vertex.v)) return ;
            vertex.outEdges.forEach(edge -> {
                if(!visited.contains(edge.to)){
                    queue.offer(edge.to);
                    visited.add(edge.to);
                }
            });
//            for(Edge<V,E> edge : vertex.outEdges){
//                if(!visited.contains(edge.to)){
//                    queue.offer(edge.to);
//                    visited.add(edge.to);
//                }
//            }
        }
    }

    @Override
    public void dfs(V start, Visitor<V> visitor) {
        Vertex<V, E> vertex = vertices.get(start);
        if(visitor == null || vertex == null) return ;
        dfs1(visitor,vertex,new HashSet<>());
    }

    /**
     * 递归实现
     * @param visitor
     * @param vertex
     * @param visited
     */
    public void dfs(Visitor visitor,Vertex<V,E> vertex,HashSet<Vertex<V,E>> visited){
        if(visitor.stop) return ;
        visitor.stop = visitor.visitor(vertex.v);
        visited.add(vertex);
        if(visitor.stop) return;
        vertex.outEdges.forEach(edge -> {
            if(!visited.contains(edge.to)){
                dfs(visitor,edge.to,visited);
                visited.add(edge.to);
            }
        });
    }

    /**
     * 非递归实现
     * @param visitor
     * @param vertex
     * @param visited
     */
    public void dfs1(Visitor visitor,Vertex<V,E> vertex,HashSet<Vertex<V,E>> visited){
        Stack<Vertex<V,E>> stack = new Stack<>();
        stack.push(vertex);
        visited.add(vertex);
        while(!stack.isEmpty()){
            vertex = stack.pop();
            if(visitor.visitor(vertex.v)) return ;
            vertex.outEdges.forEach(edge -> {
                if(!visited.contains(edge.to)){
                    stack.push(edge.to);
                    visited.add(edge.to);
                }
            });
        }
    }



    @Override
    public int vertexSize() {
        return vertices.size();
    }

    @Override
    public int EdgeSize() {
        return edges.size();
    }

    @Override
    public void addVertex(V v) {
        // 先看看map中是否存在该顶点
        if (!vertices.containsKey(v)) {
            vertices.put(v, new Vertex<>(v));
        }
    }

    @Override
    public void addEdge(V from, V to) {
        addEdge(from, to, null);
    }

    @Override
    public void addEdge(V from, V to, E weight) {
        // 判断两个顶点是否存在，若不存在创建
        if (!vertices.containsKey(from)) {
            vertices.put(from, new Vertex<>(from));
        }
        if (!vertices.containsKey(to)) {
            vertices.put(to, new Vertex<>(to));
        }
        Vertex<V, E> fromVertex = vertices.get(from);
        Vertex<V, E> toVertex = vertices.get(to);
        Edge<V, E> edge = new Edge<>(fromVertex, toVertex, weight);
        // 删除原来的边
        if (fromVertex.outEdges.remove(edge)) {
            toVertex.inEdges.remove(edge);
            edges.remove(edge);
        }
        // 添加边
        fromVertex.outEdges.add(edge);
        toVertex.inEdges.add(edge);
        edges.add(edge);
    }

    @Override
    public void removeVertex(V v) {
        // 删除顶点要把所有的边信息也删了
        // 删除顶点不存在直接返回
        Vertex<V, E> vertex = vertices.remove(v);
        if(vertex == null) return ;
        // 获得该顶点所有的边信息
        // 入边
        Set<Edge<V, E>> inEdges = vertex.inEdges;
        // 出边
        Set<Edge<V, E>> outEdges = vertex.outEdges;
        // 迭代器
        Iterator<Edge<V, E>> it1 = inEdges.iterator();
        Iterator<Edge<V, E>> it2 = outEdges.iterator();
        // v2 -> v0 删除v2
        while (it1.hasNext()){
            Edge<V, E> edge = it1.next();
            edge.from.outEdges.remove(edge);
            edges.remove(edge);
            it1.remove();
        }
        // v0 -> v1 删除v1
        while (it2.hasNext()){
            Edge<V, E> edge = it2.next();
            edge.to.inEdges.remove(edge);
            edges.remove(edge);
            it2.remove();
        }
    }

    @Override
    public void removeEdge(V from, V to) {
        // 删除顶点不存在直接返回
        if(!vertices.containsKey(from) || !vertices.containsKey(to)) return ;
        Vertex<V, E> fromVertex = vertices.get(from);
        Vertex<V, E> toVertex = vertices.get(to);
        Edge<V, E> edge = new Edge<V, E>(fromVertex, toVertex);
        // 删除边成功，将顶点的出入分别删了
        if(edges.remove(edge)){
            fromVertex.outEdges.remove(edge);
            toVertex.inEdges.remove(edge);
        }
    }

    /**
     * 顶点
     *
     * @param <V>
     * @param <E>
     */
    private static class Vertex<V, E> {
        V v;
        Set<Edge<V, E>> inEdges = new HashSet<>();
        Set<Edge<V, E>> outEdges = new HashSet<>();

        public Vertex(V v) {
            this.v = v;
        }

        // value相同默认相同
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Vertex<V, E> vertex = (Vertex<V, E>) o;
            return Objects.equals(v, vertex.v);
        }

        @Override
        public int hashCode() {
            return v == null ? 0 : v.hashCode();
        }

        @Override
        public String toString() {
            return "Vertex{" +
                    "v=" + v +
                    '}';
        }
    }

    /**
     * 边
     *
     * @param <V>
     * @param <E>
     */
    private static class Edge<V, E> {
        Vertex<V, E> from;
        Vertex<V, E> to;
        E weight;

        public Edge(Vertex<V, E> from, Vertex<V, E> to) {
            this(from, to, null);
        }

        public Edge(Vertex<V, E> from, Vertex<V, E> to, E weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        // 顶点相同默认相同
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Edge<V, E> edge = (Edge<V, E>) o;
            return Objects.equals(from, edge.from) && Objects.equals(to, edge.to);
        }

        @Override
        public int hashCode() {
            return from.hashCode() * 31 + to.hashCode();
        }

        @Override
        public String toString() {
            return "Edge{" +
                    "from=" + from +
                    ", to=" + to +
                    ", weight=" + weight +
                    '}';
        }
    }
}
