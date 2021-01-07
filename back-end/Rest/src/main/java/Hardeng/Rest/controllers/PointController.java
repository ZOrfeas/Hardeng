package Hardeng.Rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import Hardeng.Rest.Utilities;
import Hardeng.Rest.services.PointService;

@RestController
public class PointController {
    private static final Logger log = LoggerFactory.getLogger(PointController.class);
    private static final String endpointUrl = Utilities.BASEURL;
    
    @Autowired
    PointService pointService;
}
