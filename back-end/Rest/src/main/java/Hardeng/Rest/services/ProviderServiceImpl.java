package Hardeng.Rest.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.opencsv.bean.CsvBindByName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Hardeng.Rest.Utilities;
import Hardeng.Rest.exceptions.EnergyProviderNotFoundException;
import Hardeng.Rest.exceptions.NoDataException;
import Hardeng.Rest.repositories.EnergyProviderRepository;
import Hardeng.Rest.repositories.ChargingSessionRepository;
import Hardeng.Rest.models.ChargingSession;
import Hardeng.Rest.models.ChargingStation;
import Hardeng.Rest.models.Car;
import Hardeng.Rest.models.EnergyProvider;

import Hardeng.Rest.services.EVServiceImpl.PricePolicyRef;

@Service
public class ProviderServiceImpl implements ProviderService{
    private static final Logger log = LoggerFactory.getLogger(PointServiceImpl.class);

    @Autowired
    private EnergyProviderRepository EPrepo;
    @Autowired 
    private ChargingSessionRepository CsessRepo;

    public static class ProvObject
    {
        @JsonProperty("StationId")
        @CsvBindByName
        private String stationId;
        @JsonProperty("SessionId")
        @CsvBindByName
        private Integer sessionId;
        @JsonProperty("VehicleId")
        @CsvBindByName
        private String vehicleId;
        @JsonProperty("StartedOn")
        @CsvBindByName
        private String startedOn;
        @JsonProperty("FinishedOn")
        @CsvBindByName
        private String finishedOn;
        @JsonProperty("EnergyDelivered")
        @CsvBindByName
        private Float energyDelivered;
        @JsonProperty("PricePolicyRef")
        @CsvBindByName
        private String pricePolicyRef;
        @JsonProperty("CostPerKWh")
        @CsvBindByName
        private Float costPerKWh;
        @JsonProperty("TotalCost")
        @CsvBindByName
        private Float totalcost;

        ProvObject(ChargingSession Csession)
        {
            this.stationId =  Csession.getChargingPoint().getCStation().getId().toString();
            this.sessionId = Csession.getSessionId();
            this.vehicleId = Csession.getCarDriver().getCar().getId().toString();
            this.startedOn = Utilities.TIMESTAMP_FORMAT.format(Csession.getStartedOn());
            this.finishedOn =  Utilities.TIMESTAMP_FORMAT.format(Csession.getFinishedOn());
            this.energyDelivered = Csession.getEnergyDelivered();
            this.pricePolicyRef = new PricePolicyRef(Csession.getPricePolicy()).toString();
            this.costPerKWh = Csession.getPricePolicy().getCostPerKWh();
            this.totalcost = this.costPerKWh * this.energyDelivered;
        }
        public String getStationId(){return this.stationId;}
        public Integer getSessionId(){return this.sessionId;}
        public String getVehicleId(){return this.vehicleId;}
        public String getStartedOn(){return this.startedOn;}
        public String getFinishedOn(){return this.finishedOn;}
        public Float getEnergyDelivered(){return this.energyDelivered;}
        public String getPricePolicy(){return this.pricePolicyRef;}
        public Float getCostperKWh(){return this.costPerKWh;}
        public Float getCost(){return this.totalcost;}
        @Override
        public String toString() {
            return this.stationId.toString() +'|'+ this.sessionId.toString() +'|'+
            this.vehicleId +'|'+ this.startedOn +'|'+ this.finishedOn +'|'+
            this.energyDelivered.toString() +'|'+ this.pricePolicyRef +'|'+ 
            this.costPerKWh.toString() +'|'+ this.totalcost.toString();
        }
    }

    public static class SessProvObject
    {
        @JsonProperty("ProviderId")
        @CsvBindByName
        private String providerId;
        @JsonProperty("ProviderName")
        @CsvBindByName
        private String ProviderName;
        @JsonProperty("ProviderList")
        @CsvBindByName(column = "STATIONID|SESSIONID|VEHICLEID|STARTEDON|FINISHEDON|ENERGYDELIVERED|PRICEPOLICY|COSTPERKWH|TOTALCOST")
        private List<ProvObject> provList = new ArrayList<>();

        SessProvObject(Timestamp from, Timestamp to,
        EnergyProvider eprov, List<ChargingSession> csess){
            this.providerId = eprov.getId().toString();
            this.ProviderName = eprov.getName();
            for(int i = 0; i < csess.size(); i++)
            {
                this.provList.add(new ProvObject(csess.get(i)));
            }
        }
        public String getproviderId(){return this.providerId;}
        public String getproviderName(){return this.ProviderName;}
        @JsonIgnore
        public String getProviderList(){return this.provList.toString();}
    }

    @Override
    public SessProvObject sessionsPerProvider(
        Integer providerId, String dateFrom, String dateTo) throws NoDataException
        {
            Timestamp queryDateFrom = Utilities.timestampFromString(
            dateFrom, Utilities.DATE_FORMAT);
            Timestamp queryDateTo = Utilities.timestampFromString(
            dateTo, Utilities.DATE_FORMAT);
            log.info("Fetching Provider entry");
            EnergyProvider queryProv = EPrepo.findById(providerId)
            .orElseThrow(()-> new EnergyProviderNotFoundException(providerId));
            log.info("Fetching Provider Data");
            List<ChargingSession> csess = CsessRepo.findByStartedOnBetweenAndEnergyProvider(queryDateFrom, 
            queryDateTo,queryProv);
            if (csess.isEmpty()) throw new NoDataException();
            return new SessProvObject(queryDateFrom, queryDateTo, queryProv, csess);

        }
   
}