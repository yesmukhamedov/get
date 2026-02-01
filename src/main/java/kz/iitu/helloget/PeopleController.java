package kz.iitu.helloget;

import java.time.Year;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class PeopleController {

    @GetMapping("/mvc/people/{personName}")
    public String showPersonSummary(@PathVariable String personName,
                                    @RequestParam(required = false) Integer birthYear,
                                    Model model) {
        String trimmedName = validate(personName, birthYear);
        model.addAttribute("personName", trimmedName);
        model.addAttribute("birthYear", birthYear);
        return "people/summary";
    }

    @GetMapping("/api/people/{personName}")
    @ResponseBody
    public ResponseEntity<PersonResponse> getPerson(@PathVariable String personName,
                                                    @RequestParam(required = false) Integer birthYear) {
        String trimmedName = validate(personName, birthYear);
        return ResponseEntity.ok(new PersonResponse(trimmedName, birthYear));
    }

    private String validate(String personName, Integer birthYear) {
        String trimmedName = personName == null ? "" : personName.trim();
        if (trimmedName.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "personName must be non-blank");
        }
        if (birthYear == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "birthYear is required");
        }
        int currentYear = Year.now().getValue();
        if (birthYear < 1900 || birthYear > currentYear) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "birthYear must be between 1900 and " + currentYear
            );
        }
        return trimmedName;
    }
}
