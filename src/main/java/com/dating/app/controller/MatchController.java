package com.dating.app.controller;

import com.dating.app.model.Profile;
import com.dating.app.service.MatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping("/likes")
    public ResponseEntity<Map<String, Object>> likeProfile(@RequestBody Map<String, Long> body) {
        Long likerId = body.get("likerId");
        Long likedId = body.get("likedId");
        if (likerId == null || likedId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "likerId and likedId are required"));
        }
        try {
            return ResponseEntity.ok(matchService.likeProfile(likerId, likedId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/likes")
    public ResponseEntity<Object> unlikeProfile(@RequestBody Map<String, Long> body) {
        Long likerId = body.get("likerId");
        Long likedId = body.get("likedId");
        if (likerId == null || likedId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "likerId and likedId are required"));
        }
        return matchService.unlike(likerId, likedId)
            .map(p -> ResponseEntity.ok().body((Object) Map.of("message", "Unliked successfully")))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/matches/{profileId}")
    public List<Profile> getMatches(@PathVariable Long profileId) {
        return matchService.getMatches(profileId);
    }

    @GetMapping("/likes/{profileId}/received")
    public List<Profile> getLikesReceived(@PathVariable Long profileId) {
        return matchService.getLikesReceived(profileId);
    }
}
