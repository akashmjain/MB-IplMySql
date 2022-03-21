-- Copy data from csv to postgreSql
CREATE TABLE matches(id int primary key not null, season int, city text,match_date text,team1 text,team2 text,toss_winner text,toss_decision text,result text,dl_applied int,winner text,win_by_runs int, win_by_wickets int,player_of_match text,venue text,umpire1 text,umpire2 text,umpire3 text);
COPY matches from '/home/akash/IPLProject/dataset/matches.csv' DELIMITER ',' CSV HEADER;


CREATE TABLE deliveries(match_id int, inning int,batting_team text, bowling_team text,over int, ball int, batsman text, non_striker text,bowler text,is_super_over int,wide_runs int,bye_runs int,legbye_runs int,noball_runs int,penalty_runs int,batsman_runs int,extra_runs int,total_runs int,player_dismissed text,dismissal_kind text,fielder text);
copy deliveries from '/home/akash/IPLProject/dataset/deliveries.csv' DELIMITER ',' CSV HEADER;

-- 1st findNumberOfMatchesWonPerTeamsOverAllYears
SELECT COUNT(id),winner FROM matches GROUP BY winner;
-- 2nd findNumberOfMatchesPlayedPerYearForAllYears
SELECT COUNT(id),season FROM matches GROUP BY season;
-- 3rd findYearWiseExtraRunConcededPerTeam
SELECT batting_team,SUM(extra_runs) FROM deliveries WHERE match_id IN (SELECT id FROM matches WHERE season=2016) GROUP BY batting_team;
-- 4th Economical Bowler in year 2015
SELECT cnt_table.bowler, runs_table.total_runs::decimal/(cnt_table.ball_count::decimal/6) as eco_rate from (select bowler, count(bowler) as ball_count from (select d.* from deliveries d, matches m where d.match_id=m.id and m.season=2015) as del group by bowler) as cnt_table, (select bowler, sum(total_runs) as total_runs from (select d.* from deliveries d, matches m where d.match_id=m.id and m.season=2015) as del group by bowler) as runs_table where cnt_table.bowler = runs_table.bowler order by eco_rate LIMIT 5;
-- 5th findTopMostCatchesInHistoryPlayers
SELECT fielder, COUNT(dismissal_kind) AS catch_count FROM deliveries WHERE dismissal_kind='caught' GROUP BY fielder ORDER BY catch_count DESC LIMIT 5;
