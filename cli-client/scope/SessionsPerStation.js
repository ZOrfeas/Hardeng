const axios = require("axios");
const { isValidDate, tokenFileExists, getToken, errorHandler } = require("../utils");

exports.command = 'SessionsPerStation'

exports.desc = 'Get charging sessions of a charging station for a specified time period, groupped by charging point'

exports.builder = {
    station: {
      describe: 'Pass the charging station ID',
      demand: true
    },
    datefrom: {
      describe: 'Pass the starting date',
      demand: true
    },
    dateto: {
        describe: 'Pass the ending date',
        demand: true
    },
    format: {
      demand: true,
      describe: 'Choose output format',
      choices: ['csv', 'json']
    }
}

exports.handler = function(argv) {

    if (tokenFileExists()) {
        const token = getToken();
        
        const stationID = argv.station;

        if (!isValidDate(argv.datefrom.toString()))
        {
            console.log('Invalid datefrom format: Use YYYYMMDD format instead');
            return;
        }

        if (!isValidDate(argv.dateto.toString()))
        {
            console.log('Invalid dateto format: Use YYYYMMDD format instead');
            return;
        }

        const periodFrom = argv.datefrom;
        const periodTo = argv.dateto;

        const endpoint = '/SessionsPerStation' + '/' + stationID + '/' + periodFrom + '/' + periodTo;
        axios.get(endpoint, {
            params: {
                format: argv.format
            },
            headers: {
                'X-OBSERVATORY-AUTH': token.toString()
            }
        })
        .then(res => {
            console.log(res.data);
        })
        .catch(err => {
            errorHandler(err);
        })
    }
    else {
        console.log('You are not logged in. Please log in first');
    } 
}