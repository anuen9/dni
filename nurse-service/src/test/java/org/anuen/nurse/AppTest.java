package org.anuen.nurse;

import cn.hutool.core.util.RandomUtil;
import org.anuen.nurse.entity.dto.AddNurseDto;
import org.anuen.nurse.service.INurseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AppTest {

    @Autowired
    private INurseService nurseService;

    @Test
    public void testAdd() {
        String[] firstNames = {
                "Sophie", "Ethan", "Chloe", "Liam", "Ava",
                "Noah", "Amelia", "Mia", "Olivia", "Lucas",
                "Aiden", "Ella", "Grace", "Aria", "Isabella",
                "Jackson", "Lily", "Harper", "Sophia", "Charlie",
                "Evelyn", "Zoe", "Henry", "Benjamin", "Carter",
                "Mason", "Scarlett", "Lillian", "Michael", "James",
                "Avery", "Luna", "Eva", "Abigail", "Oliver",
                "William", "Alexander", "Hudson", "Layla", "Scarlett",
                "Evelyn", "Abigail", "Liam", "Ethan", "Aiden",
                "Mila", "Lily", "Harper", "Lucy", "Aria"
        };

        String[] lastNames = {
                "Smith", "Johnson", "Williams", "Jones", "Brown",
                "Davis", "Miller", "Wilson", "Moore", "Taylor",
                "Anderson", "Thomas", "Jackson", "White", "Harris",
                "Martin", "Thompson", "Garcia", "Martinez", "Robinson",
                "Clark", "Rodriguez", "Lewis", "Lee", "Walker",
                "Hall", "Allen", "Young", "Hernandez", "King",
                "Wright", "Hill", "Scott", "Green", "Adams",
                "Baker", "Nelson", "Carter", "Mitchell", "Perez",
                "Roberts", "Turner", "Phillips", "Campbell", "Parker",
                "Evans", "Edwards", "Collins", "Stewart", "Sanchez"
        };

        String[] emailSuffixes = {
                "@gmail.com", "@yahoo.com", "@hotmail.com", "@outlook.com", "@icloud.com", "@aol.com"
        };

        for (int i = 0; i < 50; i++) {
            int i1 = RandomUtil.randomInt(0, 50);
            int i2 = RandomUtil.randomInt(0, 50);
            int i3 = RandomUtil.randomInt(18, 35);
            int i4 = RandomUtil.randomInt(0, 2);
            String email = RandomUtil.randomString(6) + emailSuffixes[RandomUtil.randomInt(0, 6)];
            String phone = "1" + RandomUtil.randomNumbers(10);

            AddNurseDto nurseDto = AddNurseDto.builder()
                    .firstName(firstNames[i1])
                    .lastName(lastNames[i2])
                    .age(i3)
                    .gender(i4)
                    .email(email)
                    .phone(phone)
                    .isLeader(0)
                    .leaderId(null)
                    .build();

            System.out.println("nurseDto = " + nurseDto);

            nurseService.add(nurseDto);
        }

    }
}
