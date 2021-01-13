package Hardeng.Rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import Hardeng.Rest.exceptions.BadRequestException;
import Hardeng.Rest.services.ProviderService;
import Hardeng.Rest.services.ProviderServiceImpl.SessProvObject;

@RestController
public class ProviderController{
    private static final Logger log = LoggerFactory.getLogger(StationController.class);

    @Autowired
    ProviderService providerService;

    @GetMapping(value = "/SessionsPerProvider/{providerId}/{dateFrom}/{dateTo}",
    produces = {"application/json", "text/csv"})
    public SessProvObject sessionsPerProvider(@PathVariable(required = false) Integer providerId,
    @PathVariable(required = false) String dateFrom, @PathVariable(required = false) String dateTo)
    {
        log.info("Sessions Per Provider Requested");
        if(providerId == null || dateFrom == null || dateTo == null)throw new BadRequestException();
        return providerService.sessionsPerProvider(providerId, dateFrom, dateTo);
    }


}