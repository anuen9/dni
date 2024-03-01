package org.anuen.nurse.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.anuen.api.client.UserClient;
import org.anuen.common.entity.ResponseEntity;
import org.anuen.common.entity.dto.UserDto;
import org.anuen.common.enums.ResponseStatus;
import org.anuen.nurse.dao.NurseMapper;
import org.anuen.nurse.entity.dto.AddNurseDto;
import org.anuen.nurse.entity.dto.NameSuggestion;
import org.anuen.nurse.entity.po.Nurse;
import org.anuen.nurse.service.INurseService;
import org.anuen.utils.SysUserUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NurseServiceImpl
        extends ServiceImpl<NurseMapper, Nurse> implements INurseService {

    private final UserClient userClient;

    private final SysUserUtil sysUserUtil;

    @Override
    public ResponseEntity<?> add(AddNurseDto nurseDto) {
        Nurse dbNurse = lambdaQuery()
                .eq(Nurse::getFirstName, nurseDto.getFirstName())
                .eq(Nurse::getLastName, nurseDto.getLastName())
                .one();
        if (Objects.nonNull(dbNurse)) {
            return ResponseEntity.fail(ResponseStatus.USER_EXISTS);
        }

        Nurse nurse = Nurse.newInstance();
        BeanUtils.copyProperties(nurseDto, nurse);
        nurse.setUid(IdUtil.fastUUID());

        this.baseMapper.insert(nurse);

        UserDto dto4Save = sysUserUtil.getUserDto4Save(nurse);
        userClient.add(dto4Save);
        return ResponseEntity.success();
    }

    @Override
    public ResponseEntity<?> fetchSuggestionsByName(String name) {
        List<NameSuggestion> suggestions = this.baseMapper.selectNamesByInput("%" + name + "%");
        if (CollectionUtils.isEmpty(suggestions)) {
            return ResponseEntity.success(new ArrayList<>());
        }
        return ResponseEntity.success(suggestions);
    }

}
