package org.anuen.appointment.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.anuen.appointment.dao.AllergiesMapper;
import org.anuen.appointment.entity.po.Allergies;
import org.anuen.appointment.entity.vo.AllergiesCascaderVo;
import org.anuen.appointment.service.IAllergiesService;
import org.anuen.common.entity.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AllergiesServiceImpl
        extends ServiceImpl<AllergiesMapper, Allergies> implements IAllergiesService {

    @Override
    public ResponseEntity<?> getFormatList() {
        List<Allergies> allergiesList = lambdaQuery().list(); // get all allergies from database
        assert CollectionUtil.isNotEmpty(allergiesList);

        /*
         * partition allergies by parent id:
         * part1 -> parent id = x,
         * part2 -> parent id = y,
         * part3 -> parent id = z...
         * notes that all part is a type of Allergies List -> List<Allergies>
         * */
        final Map<Integer, List<Allergies>> partitionAllergies = allergiesList.stream()
                .collect(Collectors.groupingBy(x -> {
                    if (x.getParentId().equals(0)) {
                        return 0;
                    } else {
                        return x.getParentId();
                    }
                }));

        List<AllergiesCascaderVo> cascaderVos = new ArrayList<>(); // used to storage vo
        partitionAllergies.get(0).forEach(root -> { // get(0) mean get all root(top level) allergies
            AllergiesCascaderVo vo = new AllergiesCascaderVo();
            vo.setValue(root.getName());
            vo.setLabel(root.getName());
            vo.setChildren(
                    partitionAllergies.get(root.getAllergiesId())
                            .stream()
                            .map(sub -> new AllergiesCascaderVo(
                                    sub.getName(),
                                    sub.getName(),
                                    new ArrayList<>() // avoid NPE
                            )).toList()
            );
            cascaderVos.add(vo);
        });

        return ResponseEntity.success(cascaderVos);
    }
}
