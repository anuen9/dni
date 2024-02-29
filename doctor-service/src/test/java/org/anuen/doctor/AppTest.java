package org.anuen.doctor;

import cn.hutool.core.util.RandomUtil;
import org.anuen.doctor.entity.dto.AddDoctorDto;
import org.anuen.doctor.service.IDoctorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AppTest {

    @Autowired
    private IDoctorService doctorService;

    @Test
    public void testAddDoctor() {
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

        String[] firstNames = {
                "Emma", "Liam", "Olivia", "Noah", "Ava",
                "Isabella", "Sophia", "Jackson", "Mia", "Lucas",
                "Harper", "Evelyn", "Alexander", "Abigail", "Ella",
                "Michael", "William", "James", "Aiden", "Ethan",
                "Elijah", "Scarlett", "Grace", "Carter", "Chloe",
                "Lily", "Aria", "Zoe", "Lillian", "Charlie",
                "Ella", "Avery", "Mason", "Layla", "Scarlett",
                "Evelyn", "Abigail", "Henry", "Hudson", "Amelia",
                "Harper", "Luna", "Eva", "Aiden", "Benjamin",
                "Lucy", "Mila", "Liam", "Oliver", "Ethan"
        };

        String[] medicalSpecialties = {
                "Cardiology", "Dermatology", "Endocrinology", "Gastroenterology", "Hematology",
                "Neurology", "Orthopedics", "Oncology", "Pediatrics", "Psychiatry",
                "Radiology", "Surgery", "Urology", "Ophthalmology", "Nephrology"
        };

        String[] emailSuffixes = {
                "@gmail.com", "@yahoo.com", "@hotmail.com", "@outlook.com", "@icloud.com", "@aol.com"
        };

        for (int i = 0; i < 50; i++) {
            int randomInt1 = RandomUtil.randomInt(0, 50);
            int randomInt2 = RandomUtil.randomInt(0, 50);
            int randomInt3 = RandomUtil.randomInt(0, 15);
            int randomInt4 = RandomUtil.randomInt(0, 6);
            String phone = "1" + RandomUtil.randomNumbers(10);
            String email = RandomUtil.randomString(6) + emailSuffixes[randomInt4];
            final String address ="Hangzhou City, Zhejiang Province, China";

            AddDoctorDto doctorDto = AddDoctorDto.builder()
                    .firstName(firstNames[randomInt1])
                    .lastName(lastNames[randomInt2])
                    .specialty(medicalSpecialties[randomInt3])
                    .phone(phone)
                    .email(email)
                    .address(address)
                    .otherDetails("None")
                    .build();

            System.out.println("doctorDto = " + doctorDto);
            doctorService.add(doctorDto);
        }


    }

}
