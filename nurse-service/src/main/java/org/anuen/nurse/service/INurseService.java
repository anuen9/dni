package org.anuen.nurse.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.anuen.common.entity.ResponseEntity;
import org.anuen.nurse.entity.dto.AddNurseDto;
import org.anuen.nurse.entity.po.Nurse;

public interface INurseService extends IService<Nurse> {
    ResponseEntity<?> add(AddNurseDto nurseDto);
}
