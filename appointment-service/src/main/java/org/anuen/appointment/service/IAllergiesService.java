package org.anuen.appointment.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.anuen.appointment.entity.po.Allergies;
import org.anuen.common.entity.ResponseEntity;

public interface IAllergiesService extends IService<Allergies> {
    ResponseEntity<?> getFormatList();
}
