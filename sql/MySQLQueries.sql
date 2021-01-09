-- 1st findNumberOfMatchesWonPerTeamsOverAllYears
SELECT COUNT(id),winner FROM matches GROUP BY winner;
-- 2nd findNumberOfMatchesPlayedPerYearForAllYears
SELECT COUNT(id),season FROM matches GROUP BY season;
-- 3rd findYearWiseExtraRunConcededPerTeam
SELECT batting_team,SUM(extra_runs) FROM deliveries WHERE match_id IN (SELECT id FROM matches WHERE season=2016) GROUP BY batting_team;
-- 4th Economical Bowler in year 2015
SELECT cnt_table.bowler, runs_table.total_runs/(cnt_table.ball_count DIV 6) as eco_rate from (select bowler, count(bowler) as ball_count from (select d.* from deliveries d, matches m where d.match_id=m.id and m.season=2015) as del group by bowler) as cnt_table, (select bowler, sum(total_runs) as total_runs from (select d.* from deliveries d, matches m where d.match_id=m.id and m.season=2015) as del group by bowler) as runs_table where cnt_table.bowler = runs_table.bowler order by eco_rate LIMIT 5;
-- 5th findTopMostCatchesInHistoryPlayers
SELECT fielder, COUNT(dismissal_kind) AS catch_count FROM deliveries WHERE dismissal_kind='caught' GROUP BY fielder ORDER BY catch_count DESC LIMIT 5;
