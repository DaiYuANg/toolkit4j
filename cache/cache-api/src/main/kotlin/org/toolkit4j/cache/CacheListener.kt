package org.toolkit4j.cache

interface CacheListener<K, V> {
    /**
     * 当缓存中添加新键值对时触发.
     *
     * @param key 新添加的键
     * @param value 对应的值
     */
    fun onAdd(key: K, value: V?)

    /**
     * 当缓存中更新键值对时触发.
     *
     * @param key 更新的键
     * @param oldValue 更新前的值
     * @param newValue 更新后的值
     */
    fun onUpdate(key: K, oldValue: V?, newValue: V?)

    /**
     * 当缓存中移除键值对时触发.
     *
     * @param key 移除的键
     * @param value 被移除的值
     */
    fun onRemove(key: K, value: V?)

    /**
     * 当缓存中清空所有数据时触发.
     */
    fun onClear()

    /**
     * 当缓存中发生刷新操作时触发.
     *
     * @param keys 刷新的键集合
     */
    fun onRefresh(keys: Collection<K>)
}