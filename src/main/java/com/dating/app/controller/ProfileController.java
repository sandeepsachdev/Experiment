package com.dating.app.controller;

import com.dating.app.model.Gender;
import com.dating.app.model.Profile;
import com.dating.app.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profiles")
@CrossOrigin(origins = "*")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public List<Profile> getAllProfiles() {
        return profileService.getAllProfiles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profile> getProfile(@PathVariable Long id) {
        return profileService.getProfileById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/gender/{gender}")
    public List<Profile> getByGender(@PathVariable String gender) {
        return profileService.getProfilesByGender(Gender.valueOf(gender.toUpperCase()));
    }

    @GetMapping("/search")
    public List<Profile> searchByAge(
        @RequestParam(defaultValue = "18") int minAge,
        @RequestParam(defaultValue = "99") int maxAge) {
        return profileService.getProfilesByAgeRange(minAge, maxAge);
    }

    @PostMapping
    public ResponseEntity<Profile> createProfile(@Valid @RequestBody Profile profile) {
        return ResponseEntity.ok(profileService.createProfile(profile));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Profile> updateProfile(@PathVariable Long id,
                                                  @Valid @RequestBody Profile profile) {
        return profileService.updateProfile(id, profile)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable Long id) {
        return profileService.deleteProfile(id)
            ? ResponseEntity.noContent().build()
            : ResponseEntity.notFound().build();
    }
}
