package com.killiann.ephemeral.factories;

import com.killiann.ephemeral.controllers.JwtController;
import com.killiann.ephemeral.models.Venue;
import com.killiann.ephemeral.repositories.VenueRepository;
import org.instancio.Instancio;
import org.instancio.generators.Generators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.instancio.Select.field;

public class VenueFactory {
    private static final Logger logger = LoggerFactory.getLogger(JwtController.class);

    public static void main(String[] args) {
        VenueFactory fact = new VenueFactory();
        fact.generate();
    }

    public VenueFactory() {}

    public void generate() {
        Venue fakeVenue = Instancio.of(Venue.class)
                .set(field(Venue::getCity), "Nantes")
                .generate(field(Venue::getLng), gen -> gen.doubles().range(-90.0000000, 90.0000000))
                .generate(field(Venue::getLat), gen -> gen.doubles().range(-180.0000000, 180.0000000))
                .set(field(Venue::getStreet), "Boulevard Léon-Bureau")
                .set(field(Venue::getName), "Trempo")
                .set(field(Venue::getCountry), "FR").create();
        logger.debug(fakeVenue.toString());
    }
}
