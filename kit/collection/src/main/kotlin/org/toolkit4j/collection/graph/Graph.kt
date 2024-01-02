package org.toolkit4j.collection.graph

data class Edge<T>(val source: T, val target: T, val weight: Double? = null)

interface Graph<T> {

    /**
     * @param vertex Vertex of graph
     */
    fun addVertex(vertex: T)
    fun addEdge(from: T, to: T, weight: Double? = null)
    fun getNeighbors(vertex: T): List<Edge<T>>
    fun dfs(startVertex: T, visited: MutableSet<T> = mutableSetOf())
    fun bfs(startVertex: T)
    fun removeVertex(vertex: T)
    fun removeEdge(from: T, to: T)
    fun containsVertex(vertex: T): Boolean
    fun getAllVertices(): Set<T>
    fun getAllEdges(): List<Edge<T>>
}