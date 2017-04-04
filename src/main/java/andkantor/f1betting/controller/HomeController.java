package andkantor.f1betting.controller;

import andkantor.f1betting.controller.user.BaseController;
import andkantor.f1betting.entity.*;
import andkantor.f1betting.model.Leaderboard;
import andkantor.f1betting.model.setting.ConfigurationManager;
import andkantor.f1betting.model.user.UserProvider;
import andkantor.f1betting.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Controller
public class HomeController extends BaseController {

    @Autowired
    SeasonRepository seasonRepository;

    @Autowired
    ConfigurationManager configurationManager;

    @Autowired
    RaceRepository raceRepository;

    @Autowired
    UserProvider userProvider;

    @Autowired
    RacePointRepository racePointRepository;

    @Autowired
    BetRepository betRepository;

    @Autowired
    WatchRepository watchRepository;

    @RequestMapping(value = {"/", "/home"})
    public String home(Model model) {
        Long activeSeason = configurationManager.getConfiguration().getActiveSeason();
        List<User> users = userProvider.getActiveUsers();
        Map<User, CumulativePoint> cumulativePoints;

        if (activeSeason != 0) {
            Season season = seasonRepository.findOne(activeSeason);
            List<Race> races = raceRepository.findBySeason(season);
            cumulativePoints = racePointRepository.sumUserPoints(users, season);

            races.sort((race1, race2) -> race1.getStartDateTime().compareTo(race2.getStartDateTime()));
            Race nextRace = races.stream()
                    .filter(Race::canBeBetOn)
                    .findFirst()
                    .orElse(null);

            model.addAttribute("season", season);
            model.addAttribute("races", races);
            model.addAttribute("nextRace", nextRace);

            if (userLoggedIn()) {
                User user = getUser();
                List<String> watchList = watchRepository.findByWatcher(user).stream()
                        .map(watch -> watch.getWatched().getUsername())
                        .collect(toList());
                model.addAttribute("watchList", watchList);

                if (nextRace != null) {
                    List<Bet> bets = betRepository.findByUserAndRace(user, nextRace);
                    model.addAttribute("showBetWarning", bets.isEmpty());
                }
            }
        } else {
            cumulativePoints = users.stream()
                    .collect(Collectors.toMap(
                            user -> user,
                            user -> new CumulativePoint(user.getUsername(), BigDecimal.ZERO)));
        }

        model.addAttribute("leaderboard", new Leaderboard(users, cumulativePoints));

        return "home";
    }



}
