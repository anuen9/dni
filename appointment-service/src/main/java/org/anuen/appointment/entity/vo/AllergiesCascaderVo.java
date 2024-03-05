package org.anuen.appointment.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllergiesCascaderVo {
    public String value;
    public String label;
    public List<AllergiesCascaderVo> children;
}

