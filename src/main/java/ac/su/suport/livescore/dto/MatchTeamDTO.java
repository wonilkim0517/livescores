package ac.su.suport.livescore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MatchTeamDTO {
    private Long matchId;

    private String team1Department;
    private int team1Wins;
    private int team1Draws;
    private int team1Losses;

    private String team2Department;
    private int team2Wins;
    private int team2Draws;
    private int team2Losses;

    private int matchWins;
    private int matchDraws;
    private int matchLosses;
}
