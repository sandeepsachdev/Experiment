package com.dating.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Min(18)
    @Max(99)
    private int age;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Gender gender;

    @Column(length = 1000)
    private String bio;

    private String interests;

    private String location;

    @Enumerated(EnumType.STRING)
    private Gender lookingFor;

    private String photoUrl;

    public Profile() {}

    public Profile(String name, int age, Gender gender, String bio,
                   String interests, String location, Gender lookingFor, String photoUrl) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.bio = bio;
        this.interests = interests;
        this.location = location;
        this.lookingFor = lookingFor;
        this.photoUrl = photoUrl;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getInterests() { return interests; }
    public void setInterests(String interests) { this.interests = interests; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Gender getLookingFor() { return lookingFor; }
    public void setLookingFor(Gender lookingFor) { this.lookingFor = lookingFor; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
}
