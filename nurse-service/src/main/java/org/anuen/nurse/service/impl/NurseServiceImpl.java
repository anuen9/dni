package org.anuen.nurse.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.anuen.api.client.UserClient;
import org.anuen.common.entity.ResponseEntity;
import org.anuen.common.entity.dto.UserDto;
import org.anuen.common.utils.UserContextHolder;
import org.anuen.nurse.dao.NurseMapper;
import org.anuen.nurse.entity.dto.AddNurseDto;
import org.anuen.nurse.entity.dto.NameSuggestion;
import org.anuen.nurse.entity.po.Nurse;
import org.anuen.nurse.entity.po.Schedule;
import org.anuen.nurse.entity.po.Shift;
import org.anuen.nurse.entity.vo.ScheduleVo;
import org.anuen.nurse.service.INurseService;
import org.anuen.nurse.service.IScheduleService;
import org.anuen.nurse.service.IShiftService;
import org.anuen.utils.SysUserUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static org.anuen.common.enums.ResponseStatus.*;

@Service
@RequiredArgsConstructor
public class NurseServiceImpl
        extends ServiceImpl<NurseMapper, Nurse> implements INurseService {

    private final UserClient userClient;

    private final SysUserUtil sysUserUtil;

    private final IShiftService shiftService;

    private final IScheduleService scheduleService;

    @Override
    public ResponseEntity<?> add(AddNurseDto nurseDto) {
        Nurse dbNurse = lambdaQuery()
                .eq(Nurse::getFirstName, nurseDto.getFirstName())
                .eq(Nurse::getLastName, nurseDto.getLastName())
                .one();
        if (Objects.nonNull(dbNurse)) {
            return ResponseEntity.fail(USER_EXISTS);
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

    @Override
    public ResponseEntity<?> scheduling() {
        String currUser = UserContextHolder.getUser();
        Nurse currNurse = this.baseMapper.selectOne(
                new LambdaQueryWrapper<Nurse>()
                        .eq(Nurse::getUid, currUser));
        if (Objects.isNull(currNurse) || currNurse.getIsLeader().equals(0)) {
            return ResponseEntity.fail(NURSE_OPERATE_DENY);
        }

        List<Nurse> nurses = this.baseMapper.selectList(null);
        Queue<Nurse> nQueue = new ArrayDeque<>(nurses);
        List<Shift> shifts = shiftService.list();
        Integer allNeedNum = 0;
        for (Shift shift : shifts) {
            Integer need = shift.getNeedCount();
            allNeedNum += need;
        }
        if (allNeedNum > nurses.size()) {
            return ResponseEntity.fail(NURSE_NUMBER_CONFLICT);
        }

        List<Schedule> schedules = new ArrayList<>();
        for (Shift shift : shifts) {
            for (int i = 0; i < shift.getNeedCount(); i++) {
                Nurse n = nQueue.poll();
                if (n != null) {
                    Schedule schedule = getSchedule(shift, n);
                    schedules.add(schedule);
                }
            }
        }
        Schedule latestSchedule = scheduleService.getBaseMapper().selectOne(
                new LambdaQueryWrapper<Schedule>()
                        .orderByDesc(Schedule::getDate)
                        .last("limit 1"));
        if (latestSchedule.getDate().equals(getTomorrow())) {
            return ResponseEntity.fail(SCHEDULE_EXIST);
        }
        scheduleService.saveBatch(schedules);
        return ResponseEntity.success();
    }

    @Override
    public ResponseEntity<?> getSchedule() {
        List<ScheduleVo> allVo = scheduleService.getAllVo();
        return ResponseEntity.success(allVo);
    }

    private static Schedule getSchedule(Shift s, Nurse n) {
        Schedule schedule = new Schedule();
        schedule.setNurseId(n.getNurseId());
        schedule.setShiftId(s.getShiftId());
        Date tomorrow = getTomorrow();
        schedule.setDate(tomorrow);
        return schedule;
    }

    private static Date getTomorrow() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

}
