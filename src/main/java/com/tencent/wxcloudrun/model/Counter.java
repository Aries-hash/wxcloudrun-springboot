package com.tencent.wxcloudrun.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 计数器实体
 */
@Data
public class Counter implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private Integer id;
  private Integer count;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
