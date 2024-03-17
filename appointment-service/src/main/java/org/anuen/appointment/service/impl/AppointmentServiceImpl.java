package org.anuen.appointment.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.anuen.api.client.AdviceClient;
import org.anuen.api.client.UserClient;
import org.anuen.appointment.AppointmentStatusEnum;
import org.anuen.appointment.dao.AppointmentMapper;
import org.anuen.appointment.entity.dto.AddApptDto;
import org.anuen.appointment.entity.dto.ModifyApptDto;
import org.anuen.appointment.entity.dto.UpdateStatusDto;
import org.anuen.appointment.entity.po.Appointment;
import org.anuen.appointment.entity.vo.BindApptVo;
import org.anuen.appointment.entity.vo.DetailsApptVo;
import org.anuen.appointment.entity.vo.SimpleApptVo;
import org.anuen.appointment.service.IAppointmentService;
import org.anuen.common.entity.ResponseEntity;
import org.anuen.common.entity.json.JsonAdvice;
import org.anuen.common.exception.DatabaseException;
import org.anuen.common.utils.UserContextHolder;
import org.anuen.utils.RPCRespResolver;
import org.anuen.utils.SysUserUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static org.anuen.common.enums.ResponseStatus.*;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl
        extends ServiceImpl<AppointmentMapper, Appointment> implements IAppointmentService {

    private final UserClient userClient;

    private final RPCRespResolver respResolver;

    private final SysUserUtil sysUserUtil;

    private final AdviceClient adviceClient;

    @Override
    public ResponseEntity<?> add(AddApptDto addApptDto) {
        String currUserUid = UserContextHolder.getUser(); // check current user are doctor?
        Integer userType = sysUserUtil.getUserType(currUserUid);
        if (userType.equals(-1)) {
            return ResponseEntity.fail(REMOTE_PROCEDURE_CALL_ERROR);
        }
        if (!userType.equals(2)) { // if current user are not doctor -> return
            return ResponseEntity.fail(UNAUTHORIZED);
        }

        Appointment appointment = Appointment.newInstance(); // insert appt into database
        BeanUtils.copyProperties(addApptDto, appointment);
        final Date now = new Date(System.currentTimeMillis()); // get current date time

        appointment.setDoctorUid(currUserUid)
                .setAdviceId(0)
                .setAppointmentDate(now);

        this.baseMapper.insert(appointment);

        return ResponseEntity.success();
    }

    @Override
    public ResponseEntity<?> getListByPatient(@NonNull String pUid) {
        List<Appointment> appointments = lambdaQuery() // use patient uid find appt records
                .eq(Appointment::getPatientUid, pUid)
                .list();

        if (CollectionUtils.isEmpty(appointments)) { // if no records of this patient uid -> return
            return ResponseEntity.success(new ArrayList<>());
        }

        //change po -> simple vo
        List<SimpleApptVo> simpleVos = new ArrayList<>();
        appointments.forEach(appt -> {
            SimpleApptVo vo = SimpleApptVo.newInstance();
            BeanUtils.copyProperties(appt, vo);
            simpleVos.add(vo);
        });

        return ResponseEntity.success(simpleVos); // end
    }

    @Override
    public ResponseEntity<?> getDetailsByApptId(Integer apptId) {
        Appointment dbAppt = lambdaQuery() // query DB
                .eq(Appointment::getAppointmentId, apptId)
                .one();
        if (Objects.isNull(dbAppt)) {
            return ResponseEntity.fail(DATABASE_NO_RECORD);
        }

        List<String> uidList = Arrays.asList( // get names by uid list of patient, doctor, nurse
                dbAppt.getPatientUid(),
                dbAppt.getDoctorUid(),
                dbAppt.getNurseUid());
        ResponseEntity<?> resp = userClient.getNamesByUidList(uidList);
        List<String> names = respResolver.getRespDataOfList(resp, String.class);
        if (CollectionUtil.isEmpty(names)) {
            return ResponseEntity.fail(REMOTE_PROCEDURE_CALL_ERROR);
        }

        DetailsApptVo details = DetailsApptVo.newInstance();
        BeanUtils.copyProperties(dbAppt, details);
        details.setPatientName(names.get(0)); // the order of get is the same as the order of declaration
        details.setDoctorName(names.get(1));
        details.setNurseName(names.get(2));

        return ResponseEntity.success(details);
    }

    @Override
    public ResponseEntity<?> modify(ModifyApptDto modifyApptDto) {
        Integer apptId = modifyApptDto.getAppointmentId();
        Appointment dbAppt = lambdaQuery() // find DB record
                .eq(Appointment::getAppointmentId, apptId)
                .one();
        String currDoctorUid = UserContextHolder.getUser(); // determine whether the operator has permission
        if (!currDoctorUid.equals(dbAppt.getDoctorUid())) {
            return ResponseEntity.fail(DOCTOR_OPERATE_DENY);
        }

        BeanUtils.copyProperties(modifyApptDto, dbAppt); // copy dto -> po and update into DB
        dbAppt.setUpdatedTime(new Date(System.currentTimeMillis()));

        this.baseMapper.updateById(dbAppt);

        return ResponseEntity.success();
    }

    @Override
    public Boolean isAppointmentExist(Integer apptId) {
        Appointment dbAppt = lambdaQuery()
                .select(Appointment::getAppointmentId)
                .eq(Appointment::getAppointmentId, apptId)
                .one();
        if (Objects.isNull(dbAppt)) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public ResponseEntity<?> bindWithAdvice(
            @NonNull Integer apptId, @NonNull Integer adviceId) {
        if (!isAppointmentExist(apptId)
                || !adviceClient.isAdviceExist(adviceId)) { // advice are not exist
            throw new DatabaseException("record not exists");
        }

        Appointment dbAppt = lambdaQuery().eq(Appointment::getAppointmentId, apptId).one();
        dbAppt.setUpdatedTime(new Date(System.currentTimeMillis()));
        dbAppt.setAdviceId(adviceId);

        this.baseMapper.updateById(dbAppt);

        return ResponseEntity.success();
    }

    @Override
    public ResponseEntity<?> getNotBoundListByPatient(@NonNull String pUid) {
        String currUserUid = UserContextHolder.getUser(); // whether is a doctor
        Integer userType = sysUserUtil.getUserType(currUserUid);
        if (userType.equals(-1) || !userType.equals(2)) {
            return ResponseEntity.fail(PERMISSION_DENY);
        }

        List<Appointment> apptList = lambdaQuery() // select list of: not bound / created by current doctor / status equals progress
                .eq(Appointment::getPatientUid, pUid)
                .eq(Appointment::getAdviceId, 0)
                .eq(Appointment::getDoctorUid, currUserUid)
                .eq(Appointment::getAppointmentStatus, AppointmentStatusEnum.PROGRESS.getStatus())
                .orderByDesc(Appointment::getAppointmentDate)
                .list();
        if (CollectionUtil.isEmpty(apptList)) {
            return ResponseEntity.success(new ArrayList<>());
        }

        List<BindApptVo> bindVoList = new ArrayList<>(); // po -> vo
        apptList.forEach(appt -> {
            BindApptVo bindVo = BindApptVo.newInstance();
            bindVo.setAppointmentId(appt.getAppointmentId());
            bindVo.setAppointmentDate(appt.getAppointmentDate());
            bindVo.setDiagnosis(appt.getDiagnosis());
            bindVo.setPrescription(appt.getPrescription());
            bindVoList.add(bindVo);
        });

        return ResponseEntity.success(bindVoList);
    }

    @Override
    public ResponseEntity<?> listNeedAdmit() {
        String currUsr = UserContextHolder.getUser();
        List<Appointment> list = this.baseMapper.selectList(new LambdaQueryWrapper<Appointment>()
                .eq(Appointment::getPatientUid, currUsr)
                .eq(Appointment::getAppointmentStatus, "Progress")
                .ne(Appointment::getAdviceId, 0));
        if (CollectionUtil.isEmpty(list)) {
            return ResponseEntity.success();
        }
        List<SimpleApptVo> needAdmitList = list.stream().filter(x -> {
            Integer aId = x.getAdviceId();
            String json = adviceClient.fetchOneOfJson(aId);
            JsonAdvice jAdv = JSONObject.parseObject(json, JsonAdvice.class);
            if (Objects.isNull(jAdv)) {
                return false;
            }
            return jAdv.getNeedNursing().equals(1);
        }).map(AppointmentServiceImpl::convertToVo).toList();
        return ResponseEntity.success(needAdmitList);
    }

    private static SimpleApptVo convertToVo(Appointment x) {
        SimpleApptVo v = SimpleApptVo.newInstance();
        BeanUtils.copyProperties(x, v);
        return v;
    }

    @Override
    public ResponseEntity<?> updateStatus(UpdateStatusDto us) {
        Integer aId = us.getAppointmentId();
        String st = us.getStatus();
        Appointment appt = this.baseMapper.selectOne(
                new LambdaQueryWrapper<Appointment>()
                        .eq(Appointment::getAppointmentId, aId));
        appt.setAppointmentStatus(st);
        appt.setUpdatedTime(new Date(System.currentTimeMillis()));
        this.baseMapper.updateById(appt);
        return ResponseEntity.success();
    }

    @Override
    public ResponseEntity<?> listCanDischarge() {
        String cu = UserContextHolder.getUser();
        List<Appointment> list = this.baseMapper.selectList(new LambdaQueryWrapper<Appointment>()
                .eq(Appointment::getPatientUid, cu)
                .eq(Appointment::getAppointmentStatus, "Admitted"));
        if (CollectionUtil.isEmpty(list)) {
            return ResponseEntity.success();
        }
        List<SimpleApptVo> res = list.stream().map(AppointmentServiceImpl::convertToVo).toList();
        return ResponseEntity.success(res);
    }

}
