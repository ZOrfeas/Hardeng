package Hardeng.Rest.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
import Hardeng.Rest.repositories.ChargingPointRepository;
import Hardeng.Rest.repositories.ChargingSessionRepository;
import Hardeng.Rest.repositories.ChargingStationRepository;
import Hardeng.Rest.models.ChargingSession;
import Hardeng.Rest.models.PricePolicy;
import Hardeng.Rest.models.ChargingStation;
import Hardeng.Rest.models.ChargingPoint;
import Hardeng.Rest.models.EnergyProvider;

@Service
public class ProviderServiceImpl implements ProviderService{
    private static final Logger log = LoggerFactory.getLogger(PointServiceImpl.class);

    @Autowired
    private EnergyProviderRepository EPrepo;
    @Autowired 
    private ChargingSessionRepository CsessRepo;
    @Autowired 
    private ChargingStationRepository CstatRepo;
    @Autowired
    private ChargingPointRepository CpointRepo;

    /** Helper class for PricePolicy info string creation */
    public static class PricePolicyRef {
        private String companyName;
        private Integer kWh;
        private Float costPerKWh;

        PricePolicyRef(PricePolicy pPolicy) {
            this.companyName = pPolicy.getAdmin().getCompanyName();
            this.kWh = pPolicy.getKWh();
            this.costPerKWh = pPolicy.getCostPerKWh();
        }

        @Override
        public String toString() {
            return this.companyName + " [" + this.kWh.toString() + " kWh, " + 
                    this.costPerKWh.toString() + "€ per kWh]";
        }
    }

    /** CSV-serializable DTO with SessionsPerProvider information */
    public static class SessProvObject
    {
        @JsonProperty("ProviderId")
        @CsvBindByName
        private String providerId;
        @JsonProperty("ProviderName")
        @CsvBindByName
        private String providerName;
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
        private Float totalCost;

        SessProvObject(Timestamp from, Timestamp to,
        EnergyProvider eprov, ChargingSession Csession){
            this.providerId = eprov.getId().toString();
            this.providerName = eprov.getName();
            this.stationId =  Csession.getChargingPoint().getCStation().getId().toString();
            this.sessionId = Csession.getSessionId();
            this.vehicleId = Csession.getCarDriver().getCar().getId().toString();
            this.startedOn = Utilities.TIMESTAMP_FORMAT.format(Csession.getStartedOn());
            this.finishedOn =  Utilities.TIMESTAMP_FORMAT.format(Csession.getFinishedOn());
            this.energyDelivered = Csession.getEnergyDelivered();
            this.pricePolicyRef = new PricePolicyRef(Csession.getPricePolicy()).toString();
            this.costPerKWh = Csession.getPricePolicy().getCostPerKWh();
            this.totalCost = this.costPerKWh * this.energyDelivered;
        }
        public String getProviderId(){return this.providerId;}
        public String getProviderName(){return this.providerName;}
        public String getStationId(){return this.stationId;}
        public Integer getSessionId(){return this.sessionId;}
        public String getVehicleId(){return this.vehicleId;}
        public String getStartedOn(){return this.startedOn;}
        public String getFinishedOn(){return this.finishedOn;}
        public Float getEnergyDelivered(){return this.energyDelivered;}
        public String getPricePolicyRef(){return this.pricePolicyRef;}
        public Float getCostPerKWh(){return this.costPerKWh;}
        public Float getTotalCost(){return this.totalCost;}
    }

    @Override
    public List<SessProvObject> sessionsPerProvider(
        Integer providerId, String dateFrom, String dateTo) throws NoDataException
        {
            Timestamp queryDateFrom = Utilities.timestampFromString(
            dateFrom, Utilities.DATE_FORMAT);
            Timestamp queryDateTo = Utilities.timestampFromString(
            dateTo, Utilities.DATE_FORMAT);
            log.info("Fetching Provider entry");
            EnergyProvider queryProv = EPrepo.findById(providerId)
            .orElseThrow(()-> new EnergyProviderNotFoundException(providerId));
            log.info("Fetching Charging Stations");
            List<ChargingStation> cstat = CstatRepo.findByeProvider(queryProv);
            log.info("Fetching Charging Points");
            List<ChargingPoint> cPointList = new ArrayList<>();
            for(int i = 0; i < cstat.size(); i++)
            {
                cPointList.addAll(CpointRepo.findBycStation(cstat.get(i)));
            }

            log.info("Fetching Charging Sessions");
            List<ChargingSession> csess = new ArrayList <>();
            for(int i = 0; i < cPointList.size(); i++)
            {
                csess.addAll(CsessRepo.findByStartedOnBetweenAndChargingPoint(queryDateFrom, queryDateTo,cPointList.get(i)));
            }
            if (csess.isEmpty()) throw new NoDataException();
            List<SessProvObject> toRet = new ArrayList<>();
            for (ChargingSession co : csess) {
                toRet.add(new SessProvObject(queryDateFrom, queryDateTo, queryProv, co));
            }
            return toRet;
        }
   
}