package models;

import com.github.javafaker.Faker;
import constants.Genders;
import constants.Roles;

public class PlayerCreateRequestDto {
    private String age;
    private String gender;
    private String login;
    private String password;
    private String role;
    private String screenName;

    private PlayerCreateRequestDto() {
    }

    public String getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getScreenName() {
        return screenName;
    }

    public static class Builder {
        private final PlayerCreateRequestDto instance;

        public Builder() {
            instance = new PlayerCreateRequestDto();
        }

        public Builder age(String age) {
            instance.age = age;
            return this;
        }

        public Builder gender(String gender) {
            instance.gender = gender;
            return this;
        }

        public Builder login(String login) {
            instance.login = login;
            return this;
        }

        public Builder password(String password) {
            instance.password = password;
            return this;
        }

        public Builder role(String role) {
            instance.role = role;
            return this;
        }

        public Builder screenName(String screenName) {
            instance.screenName = screenName;
            return this;
        }

        public PlayerCreateRequestDto build() {
            return instance;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static PlayerCreateRequestDto randomValidPlayer() {
        Faker faker = new Faker();
        return PlayerCreateRequestDto.builder()
                .age(String.valueOf(faker.number().numberBetween(17, 59)))
                .gender(faker.options().option("MALE", "FEMALE"))
                .login("login_" + faker.name().username())
                .password(faker.internet().password(7, 15, true, true))
                .role(faker.options().option("USER", "ADMIN"))
                .screenName(faker.name().fullName())
                .build();
    }

    public static PlayerCreateRequestDto randomPlayerWithRole(Roles role) {
        Faker faker = new Faker();
        return PlayerCreateRequestDto.builder()
                        .age(String.valueOf(faker.number().numberBetween(17, 59)))
                        .gender(Genders.MALE.getValue())
                        .login("login_" + faker.name().username())
                        .password(faker.internet().password(7, 15, true, true))
                        .role(role.getValue())
                        .screenName(faker.name().fullName())
                        .build();
    }

    @Override
    public String toString() {
        return "PlayerCreateRequestDto{" +
                "age='" + age + '\'' +
                ", gender='" + gender + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", screenName='" + screenName + '\'' +
                '}';
    }
}
