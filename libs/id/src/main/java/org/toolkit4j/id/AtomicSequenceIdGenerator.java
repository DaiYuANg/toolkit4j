package org.toolkit4j.id;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicSequenceIdGenerator {
  private final AtomicLong counter;

  /**
   * 从0开始计数
   */
  public AtomicSequenceIdGenerator() {
    this(0L);
  }

  /**
   * 指定起始值
   * @param initialValue 起始值
   */
  public AtomicSequenceIdGenerator(long initialValue) {
    this.counter = new AtomicLong(initialValue);
  }

  /**
   * 获取下一个自增ID，线程安全
   * @return 下一个ID
   */
  public long nextId() {
    return counter.getAndIncrement();
  }

  /**
   * 重置计数器
   * @param newValue 新值
   */
  public void reset(long newValue) {
    counter.set(newValue);
  }
}
