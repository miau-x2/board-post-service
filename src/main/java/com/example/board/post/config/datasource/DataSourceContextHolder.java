package com.example.board.post.config.datasource;

import java.util.Optional;

public interface DataSourceContextHolder {
    Optional<DataSourceType> get();
}
