package ac.su.suport.livescore.controller;

import ac.su.suport.livescore.constant.TournamentRound;
import ac.su.suport.livescore.domain.LiveVideoStream;
import ac.su.suport.livescore.domain.Match;
import ac.su.suport.livescore.domain.Video;
import ac.su.suport.livescore.dto.MatchModificationDTO;
import ac.su.suport.livescore.dto.MatchSummaryDTO;
import ac.su.suport.livescore.dto.TournamentMatchDTO;
import ac.su.suport.livescore.service.BracketService;
import ac.su.suport.livescore.service.LiveVideoStreamService;
import ac.su.suport.livescore.service.MatchService;
import ac.su.suport.livescore.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchService matchService;
    private final BracketService bracketService;
    private final LiveVideoStreamService liveVideoStreamService;
    private final VideoService videoService;

    // 모든 경기 데이터를 단순 리스트로 반환
    @GetMapping("/all")
    public ResponseEntity<List<MatchSummaryDTO.Response>> getAllMatches() {
        List<MatchSummaryDTO.Response> matches = matchService.getAllMatches();
        return ResponseEntity.ok(matches);
    }

    @GetMapping("/view")
    public String viewMatches(Model model) {
        List<Match> matches = matchService.getAllMatchesWithVideos();
        model.addAttribute("matches", matches);
        return "matches";
    }

    // 필터링
    @GetMapping
    public ResponseEntity<List<MatchSummaryDTO.Response>> getFilteredMatches(
            @RequestParam(required = false) String sport,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        log.info("Received request - sport: {}, date: {}", sport, date);
        List<MatchSummaryDTO.Response> matches = matchService.getFilteredMatches(sport, date, null);
        log.info("Returning {} matches", matches.size());
        return new ResponseEntity<>(matches, HttpStatus.OK);
    }

    // 특정 경기 데이터 반환
    @GetMapping("/{id}")
    public ResponseEntity<MatchSummaryDTO.Response> getMatchById(@PathVariable Long id) {
        MatchSummaryDTO.Response match = matchService.getMatchById(id);
        return new ResponseEntity<>(match, HttpStatus.OK);
    }

    // 매치 생성
    @PostMapping
    public ResponseEntity<MatchModificationDTO> createMatch(@RequestBody MatchModificationDTO matchModificationDTO) {
        MatchModificationDTO createdMatch = matchService.createMatch(matchModificationDTO);
        return new ResponseEntity<>(createdMatch, HttpStatus.CREATED);
    }

    // 매치 수정
    @PutMapping("/{id}")
    public ResponseEntity<MatchModificationDTO> updateMatch(@PathVariable Long id, @RequestBody MatchModificationDTO matchModificationDTO) {
        MatchModificationDTO updatedMatch = matchService.updateMatch(id, matchModificationDTO);
        return new ResponseEntity<>(updatedMatch, HttpStatus.OK);
    }

    // 매치 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable Long id) {
        if (matchService.deleteMatch(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
