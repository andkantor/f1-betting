package andkantor.f1betting.controller.admin;

import andkantor.f1betting.model.race.*;
import andkantor.f1betting.repository.FinalPositionRepository;
import andkantor.f1betting.repository.RaceRepository;
import andkantor.f1betting.repository.DriverRepository;
import andkantor.f1betting.repository.SeasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static andkantor.f1betting.model.race.Position.createPosition;

@Controller
@RequestMapping(value = "/admin/season/{seasonId}/race")
public class RaceController {

    @Autowired
    SeasonRepository seasonRepository;

    @Autowired
    RaceRepository raceRepository;

    @Autowired
    DriverRepository driverRepository;

    @Autowired
    FinalPositionRepository finalPositionRepository;

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String getCreate(Race race, Model model) {
        model.addAttribute("race", race);
        return "admin/race/create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String postCreate(@PathVariable Long seasonId, @Valid Race race) {
        Season season = seasonRepository.findOne(seasonId);
        race.setSeason(season);
        // TODO set with form value
        race.setStartDateTime(LocalDateTime.now());
        raceRepository.save(race);
        return "redirect:/admin/season/" + seasonId + "/view";
    }

    @RequestMapping("/{id}/view")
    public String view(@PathVariable Long id, Model model) {
        Race race = raceRepository.findOne(id);
        Iterable<Driver> drivers = driverRepository.findAll();
        List<FinalPosition> finalPositionList = finalPositionRepository.findByRace(race);

        List<FinalPosition> finalPositions = StreamSupport.stream(drivers.spliterator(), false)
                .map(driver -> finalPositionList.stream()
                            .filter(finalPosition -> finalPosition.getDriver() == driver)
                            .findAny()
                            .orElse(new FinalPosition(race, driver, createPosition(0))))
                .collect(Collectors.toList());

        model.addAttribute("race", race);
        model.addAttribute("raceResult", new RaceResult(race, finalPositions));

        return "admin/race/view";
    }

    @RequestMapping("/{id}/saveRaceResult")
    public String saveRaceResult(
            @PathVariable Long seasonId,
            @PathVariable Long id,
            @Valid RaceResult raceResult
    ) {
        Race race = raceRepository.findOne(id);
        raceResult.getFinalPositions().forEach(finalPosition -> {
            finalPosition.setRace(race);
            finalPositionRepository.save(finalPosition);
        });

        return "redirect:/admin/season/" + seasonId + "/race/" + id + "/view";
    }
}