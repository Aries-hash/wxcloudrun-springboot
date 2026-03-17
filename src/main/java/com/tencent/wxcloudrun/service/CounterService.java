package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.model.Counter;

import java.util.Optional;

/**
 * 计数器服务接口
 */
public interface CounterService {

  Optional<Counter> getCounter(Integer id);

  void upsertCount(Counter counter);

  void clearCount(Integer id);
}
