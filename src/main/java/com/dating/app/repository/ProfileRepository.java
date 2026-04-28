package com.dating.app.repository;

import com.dating.app.model.Gender;
import com.dating.app.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    List<Profile> findByGender(Gender gender);
    List<Profile> findByLookingFor(Gender lookingFor);
    List<Profile> findByAgeGreaterThanEqualAndAgeLessThanEqual(int minAge, int maxAge);
}
