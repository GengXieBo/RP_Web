package com.huaiguang.rpweb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 切片表
 * </p>
 *
 * @author lixu
 * @since 2020-12-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Slide对象", description="切片表")
public class Slide implements Serializable {

    @ApiModelProperty(value = "切片id")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "用户id")
    private String userid;

    @ApiModelProperty(value = "切片地址")
    private String path;

    @ApiModelProperty(value = "计算结果地址")
    private String result;

    @ApiModelProperty(value = "计算标志(0:未计算, 1:正在计算, 2:已计算完)")
    private String flag;


}
