package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.CounterRequest;
import com.tencent.wxcloudrun.model.Counter;
import com.tencent.wxcloudrun.service.CounterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * 计数器控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/count")
@RequiredArgsConstructor
public class CounterController {

  private final CounterService counterService;

  /**
   * 获取当前计数
   */
  @GetMapping
  public ApiResponse get() {
    log.info("/api/count get request");
    Optional<Counter> counter = counterService.getCounter(1);
    Integer count = counter.map(Counter::getCount).orElse(0);
    return ApiResponse.ok(count);
  }

  /**
   * 更新计数，自增或者清零
   */
  @PostMapping
  public ApiResponse create(@RequestBody CounterRequest request) {
    log.info("/api/count post request, action: {}", request.getAction());
    String action = request.getAction();

    if ("inc".equals(action)) {
      Integer count = counterService.getCounter(1)
          .map(c -> c.getCount() + 1)
          .orElse(1);
      Counter counter = new Counter();
      counter.setId(1);
      counter.setCount(count);
      counterService.upsertCount(counter);
      return ApiResponse.ok(count);
    }

    if ("clear".equals(action)) {
      counterService.clearCount(1);
      return ApiResponse.ok(0);
    }

    return ApiResponse.error("参数action错误");
  }
}
