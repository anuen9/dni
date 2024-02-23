package org.anuen.patient;

import cn.hutool.core.util.RandomUtil;
import org.anuen.patient.config.DefaultInfoProperties;
import org.anuen.patient.entity.dto.PatientDto;
import org.anuen.patient.service.IPatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootTest
public class PatientApplicationTest {

    @Autowired
    private IPatientService patientService;


    @Autowired
    private DefaultInfoProperties defaultInfoProperties;

    @Test
    public void testYaml() {
        System.out.println(defaultInfoProperties.getPassword());
        System.out.println(defaultInfoProperties.getUserType());
    }

    @Test
    public void savePatientBatch() {
        List<String> emails = generateRandomEmails(100);
//        emails.forEach(System.out::println);
        String[] lastNamesArray = {
                "Smith", "Johnson", "Williams", "Jones", "Brown",
                "Davis", "Miller", "Wilson", "Moore", "Taylor",
                "Anderson", "Thomas", "Jackson", "White", "Harris",
                "Martin", "Thompson", "Garcia", "Martinez", "Robinson",
                "Clark", "Rodriguez", "Lewis", "Lee", "Walker",
                "Hall", "Allen", "Young", "Hernandez", "King",
                "Wright", "Lopez", "Hill", "Scott", "Green",
                "Adams", "Baker", "Gonzalez", "Nelson", "Carter",
                "Mitchell", "Perez", "Roberts", "Turner", "Phillips",
                "Campbell", "Parker", "Evans", "Edwards", "Collins",
                "Stewart", "Sanchez", "Morris", "Rogers", "Reed",
                "Cook", "Morgan", "Bell", "Murphy", "Bailey",
                "Rivera", "Cooper", "Richardson", "Cox", "Howard",
                "Ward", "Torres", "Peterson", "Gray", "Ramirez",
                "James", "Watson", "Brooks", "Kelly", "Sanders",
                "Price", "Bennett", "Wood", "Barnes", "Ross",
                "Henderson", "Coleman", "Jenkins", "Perry", "Powell",
                "Long", "Patterson", "Hughes", "Flores", "Washington",
                "Butler", "Simmons", "Foster", "Gonzales", "Bryant",
                "Alexander", "Russell", "Griffin", "Diaz", "Hayes"
        };

        List<String> lastNameList = new ArrayList<>();

        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            String lastName = lastNamesArray[random.nextInt(lastNamesArray.length)];
            lastNameList.add(lastName);
        }

        List<String> firstNames = new ArrayList<>();
        String[] names = {"Alice", "Bob", "Charlie", "David", "Emma", "Frank", "Grace", "Henry", "Ivy", "Jack", "Kate", "Liam", "Mia", "Noah", "Olivia", "Peter", "Quinn", "Ryan", "Sophia", "Thomas", "Ursula", "Vincent", "Willow", "Xander", "Yasmine", "Zane"};


        for (int i = 1; i <= 100; i++) {
            String firstName = names[random.nextInt(names.length)];
            firstNames.add(firstName);
        }


        List<String> phones = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            String phone = "1" + RandomUtil.randomNumbers(10);
            phones.add(phone);
        }
        phones.forEach(System.out::println);

        System.out.println(
                "first name number: " + firstNames.size()
                        + ", last name number: " + lastNameList.size()
                        + ", email number: " + emails.size()
                        + ", phone numbers: " + phones.size());

        for (int i = 0; i < 100; i++) {
            PatientDto dto = PatientDto
                    .builder()
                    .firstName(firstNames.get(i))
                    .lastName(lastNameList.get(i))
                    .age(RandomUtil.randomInt(18, 100))
                    .gender(RandomUtil.randomInt(0, 1))
                    .email(emails.get(i))
                    .phone(phones.get(i))
                    .build();
            patientService.save(dto);
        }
    }


    public static List<String> generateRandomEmails(int count) {
        Random random = new Random();
        String[] domains = {"gmail.com", "yahoo.com", "hotmail.com", "outlook.com", "example.com"};
        List<String> emails = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            String username = generateRandomString(8); // 生成8位随机用户名
            String domain = domains[random.nextInt(domains.length)]; // 随机选择域名
            String email = username + "@" + domain;
            emails.add(email);
        }

        return emails;
    }

    private static String generateRandomString(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder stringBuilder = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            stringBuilder.append(characters.charAt(index));
        }

        return stringBuilder.toString();
    }
}
