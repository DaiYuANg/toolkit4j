package org.toolkit4j.collection.graph

/**
 * Un directed graph
 */
class UndirectedGraph<T> : Graph<T> {
    private val adjacencyList: MutableMap<T, MutableList<Edge<T>>> = mutableMapOf()

    override fun addVertex(vertex: T) {
        // 添加顶点，如果图中不包含该顶点
        adjacencyList.computeIfAbsent(vertex) { mutableListOf() }
    }

    override fun addEdge(from: T, to: T, weight: Double?) {
        // 添加边，同时确保顶点存在
        addVertex(from).also { addVertex(to) }

        // 创建边并添加到邻接表中
        val edge = Edge(from, to, weight)
        adjacencyList[from]?.add(edge)
        adjacencyList[to]?.add(edge)
    }

    override fun getNeighbors(vertex: T): List<Edge<T>> {
        // 获取指定顶点的所有邻居边
        return adjacencyList[vertex] ?: emptyList()
    }

    override fun dfs(startVertex: T, visited: MutableSet<T>) {
        fun dfsRecursive(currentVertex: T, visited: Set<T>): Set<T> {
            val neighbors = getNeighbors(currentVertex)
                .flatMap { listOf(it.source, it.target) }
                .distinct()
                .filter { it !in visited }

            val updatedVisited = visited + currentVertex
            return neighbors.fold(updatedVisited) { acc, neighbor ->
                dfsRecursive(neighbor, acc)
            }
        }

        dfsRecursive(startVertex, visited.toSet())
    }

    override fun bfs(startVertex: T) {
        // 广度优先搜索
        val visited = mutableSetOf<T>()
        val queue = mutableListOf<T>()

        queue.add(startVertex)
        visited.add(startVertex)

        while (queue.isNotEmpty()) {
            val currentVertex = queue.removeAt(0)
            println("BFS: Visiting $currentVertex")

            getNeighbors(currentVertex).forEach { edge ->
                val neighbor = if (edge.source == currentVertex) edge.target else edge.source
                if (neighbor !in visited) {
                    visited.add(neighbor)
                    queue.add(neighbor)
                }
            }
        }
    }

    override fun removeVertex(vertex: T) {
        // 移除顶点及其相关的边
        adjacencyList.remove(vertex)
        adjacencyList.values.forEach { edges ->
            edges.removeIf { it.source == vertex || it.target == vertex }
        }
    }

    override fun removeEdge(from: T, to: T) {
        // 移除指定的边
        adjacencyList[from]?.removeIf { it.source == to || it.target == to }
        adjacencyList[to]?.removeIf { it.source == from || it.target == from }
    }

    override fun containsVertex(vertex: T): Boolean {
        // 检查图中是否包含指定的顶点
        return adjacencyList.containsKey(vertex)
    }

    override fun getAllVertices(): Set<T> {
        // 获取图中所有顶点的集合
        return adjacencyList.keys
    }

    override fun getAllEdges(): List<Edge<T>> {
        // 获取图中所有边的集合
        return adjacencyList.values.flatten()
    }
}