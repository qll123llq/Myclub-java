package com.jingdianjichi.oss.common;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ImageResult {
    private Integer errno;
    private ImageData data;


    public ImageResult(Integer errno, ImageData data) {
        this.errno = errno;
        this.data = data;
    }
}
