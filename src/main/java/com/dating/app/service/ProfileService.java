package com.dating.app.service;

import com.dating.app.model.Gender;
import com.dating.app.model.Profile;
import com.dating.app.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    public Optional<Profile> getProfileById(Long id) {
        return profileRepository.findById(id);
    }

    public List<Profile> getProfilesByGender(Gender gender) {
        return profileRepository.findByGender(gender);
    }

    public List<Profile> getProfilesByAgeRange(int minAge, int maxAge) {
        return profileRepository.findByAgeGreaterThanEqualAndAgeLessThanEqual(minAge, maxAge);
    }

    public Profile createProfile(Profile profile) {
        return profileRepository.save(profile);
    }

    public Optional<Profile> updateProfile(Long id, Profile updated) {
        return profileRepository.findById(id).map(existing -> {
            existing.setName(updated.getName());
            existing.setAge(updated.getAge());
            existing.setGender(updated.getGender());
            existing.setBio(updated.getBio());
            existing.setInterests(updated.getInterests());
            existing.setLocation(updated.getLocation());
            existing.setLookingFor(updated.getLookingFor());
            existing.setPhotoUrl(updated.getPhotoUrl());
            return profileRepository.save(existing);
        });
    }

    public boolean deleteProfile(Long id) {
        if (profileRepository.existsById(id)) {
            profileRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
