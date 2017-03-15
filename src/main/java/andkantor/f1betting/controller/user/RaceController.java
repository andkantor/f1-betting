package andkantor.f1betting.controller.user;

import andkantor.f1betting.entity.Race;
import andkantor.f1betting.entity.User;
import andkantor.f1betting.repository.BetRepository;
import andkantor.f1betting.repository.PenaltyRepository;
import andkantor.f1betting.repository.RaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/race")
public class RaceController extends BaseController {

    @Autowired
    RaceRepository raceRepository;

    @Autowired
    PenaltyRepository penaltyRepository;

    @Autowired
    BetRepository betRepository;

    @RequestMapping(value = "/{id}/view")
    public String view(@PathVariable Long id, Model model) {
        User user = getUser();
        Race race = raceRepository.findOne(id);

        model.addAttribute("race", race);
        model.addAttribute("penalties", penaltyRepository.findByRace(race));
        model.addAttribute("bets", betRepository.findByUserAndRace(user, race));

        return "user/race/view";
    }

}